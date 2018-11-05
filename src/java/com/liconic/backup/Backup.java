package com.liconic.backup;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.mail.Authenticator;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Timer;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.firebirdsql.management.FBBackupManager;

public class Backup {
    private static Backup instance;    
    private String DBUser = "";
    private String DBPswd = "";
    private String DBHost = "";
    private String DBPath = "";
    private String DBBackupPath = "";
    private String DB_DRIVER = "";
    public ArrayList<String> li = new ArrayList<String>();
    public ArrayList<String> listE = new ArrayList<String>();
   
/*        
    private String SMTPAddress = "";
    private String SMTPPort = "";
    private String SMTPUser = "";
    private String EMMailFrom = "";
*/       
    private List<String> BackupPaths = new ArrayList<>();    
 
    public static Backup getInstance(){
        
        if(instance == null)
            instance = new Backup();
        return instance;
    }
    
    public void setDBUser(String DBUser) {
        this.DBUser = DBUser;
    }

    public void setDBPswd(String DBPswd) {
        this.DBPswd = DBPswd;
    }

    public void setDBHost(String DBHost) {
        this.DBHost = DBHost;
    }

    public void setDBPath(String DBPath) {
        this.DBPath = DBPath;
    }

    public void setBackupPaths(List<String> BackupPaths) {
        this.BackupPaths = BackupPaths;
    }

    public String getDBUser() {
        return DBUser;
    }

    public String getDBPswd() {
        return DBPswd;
    }

    public String getDBHost() {
        return DBHost;
    }

    public String getDBPath() {
        return DBPath;
    }

    public List<String> getBackupPaths() {
        return BackupPaths;
    }

    public String getDBBackupPath() {
        return DBBackupPath;
    }

    public void setDBBackupPath(String DBBackupPath) {
        this.DBBackupPath = DBBackupPath;
    }

    public String getDB_DRIVER() {
        return DB_DRIVER;
    }

    public void setDB_DRIVER(String DB_DRIVER) {
        this.DB_DRIVER = DB_DRIVER;
    }
    
  public String[] getTime()
  {
      String list[] = new String[10];
            int i=0;
      try{
              
          Class.forName("org.firebirdsql.jdbc.FBDriver");
          Connection con = DriverManager.getConnection("jdbc:firebirdsql:localhost/3050:C:/Liconic/DB/BCKUP.GDB", "SYSDBA", "masterkey");
            
             Statement stmt = con.createStatement();
            
                ResultSet rt = stmt.executeQuery("select * from stime;");
             while(rt.next())
             {
                list[i]=rt.getString(1);
                i++;
             }
             con.close();
                 }
                  catch(Exception E) {
                 System.out.println(E.getMessage());  
  }
  
  return list;
  }
  
   public String[] getEmail()
  {
      String list[] = new String[10];
            int i=0;
      try{
          Class.forName("org.firebirdsql.jdbc.FBDriver");
           Connection con = DriverManager.getConnection("jdbc:firebirdsql:localhost/3050:C:/Liconic/DB/BCKUP.GDB", "SYSDBA", "masterkey");
            
             Statement stmt = con.createStatement();
            
                ResultSet rt = stmt.executeQuery("select * from notify;");
             while(rt.next())
             {
                list[i]=rt.getString(1);
                i++;
             }
             con.close();
                 }
                  catch(Exception E) {
                 System.out.println(E.getMessage());  
  }
  
  return list;
  }
   
 
     
    public void RunBackUp(){
        
        if (DBUser.isEmpty()){            
            System.out.println("BACKUP ERROR: DBUser is empty");
            return;
        }
        
        if (DBPswd.isEmpty()){
            System.out.println("BACKUP ERROR: DBPswd is empty");
            return;
        }
        
        if (DBHost.isEmpty()){
            System.out.println("BACKUP ERROR: DBHost is empty");
            return;
        }

        if (DBPath.isEmpty()){
            System.out.println("BACKUP ERROR: DBPath is empty");
            return;            
        }
        
        if (DBBackupPath.isEmpty()){
            System.out.println("BACKUP ERROR: DBBackupPath is empty");
            return;
        }        
        
        if (DB_DRIVER.isEmpty()){            
            System.out.println("BACKUP ERROR: DBDriver is empty");
            return;
        }        
        
     
      
        Calendar cal = Calendar.getInstance();        
        try{
            Timer deleteTimer = new Timer();
            DeleteBackup delete = new DeleteBackup(this);
            deleteTimer.schedule(delete, cal.getTime(),1000*60*60*24);
        }
        catch(Exception e){
            System.out.println("Delete DB backup error: " + e.getMessage());
        }
        
          Calendar now = Calendar.getInstance();
         try
         {
             Class.forName("org.firebirdsql.jdbc.FBDriver");
            Connection con = DriverManager.getConnection("jdbc:firebirdsql:localhost/3050:C:/Liconic/DB/BCKUP.GDB", "SYSDBA", "masterkey");
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("Select tid from stime");
          
             while (rs.next()) 
             {
             String time = rs.getString(1);
             String[] part = time.split("\\:");
             int hour,minute;
             hour= Integer.parseInt(part[0]);
             minute=Integer.parseInt(part[1]);
             Calendar calendar = Calendar.getInstance();
             calendar.set(Calendar.HOUR_OF_DAY, hour);
             calendar.set(Calendar.MINUTE, minute);
             calendar.set(Calendar.SECOND, 0);        

            if (now.get(Calendar.HOUR_OF_DAY) >= hour){
                calendar.add(Calendar.DATE, 1);
            }            
                  System.out.println("BACKUP next backup at: "+calendar.getTime());   
                  BackupTimer backupTimer1 = new BackupTimer(this);
                  Timer timer = new Timer();
                  timer.schedule(backupTimer1, calendar.getTime(),1000*60*60*12);
            }
             con.close();
        }
            catch(Exception e) {
             System.out.println(e.getMessage());
            }
            
          
    }    
    
    synchronized public void MakeBackup(String fileName) throws Exception{
        
        try{
            
            //String FileName = new SimpleDateFormat("YYYY_MM_DD_HH_mm_ss_SSS").format(new Date())+".fbk";
            //String PathToTheFile = DBBackupPath+File.separator+fileName;
            
            System.out.println("BACKUP "+new Date()+" Start backup: DB="+DBPath+" to "+fileName);
                        
            Class.forName(DB_DRIVER);
            FBBackupManager bBackupManager = new FBBackupManager();
        
            bBackupManager.setBackupPath(fileName);

            bBackupManager.setHost(DBHost);
            bBackupManager.setPort(3050);
            bBackupManager.setDatabase(DBPath);
        
            bBackupManager.setUser(DBUser);
            bBackupManager.setPassword(DBPswd);
//            bBackupManager.setLogger(System.out);
//            bBackupManager.setVerbose(true);
        
            bBackupManager.backupDatabase();            
            
        
//FBBackupManager backupManager = new FBBackupManager();
//backupManager.setHost("192.168.0.20");
//backupManager.setPort(3050);
//backupManager.setUser("SYSDBA");
//backupManager.setPassword("123456");
//backupManager.setDatabase("/dbases/mydb.fdb");
//backupManager.setBackupPath("/dbases/mybackup.fbk");
//backupManager.setLogger(System.out);
// backupManager.setVerbose(true);        
        
            
        } catch (Exception E) {
            
            System.out.println("BACKUP backup error:"+E.getMessage());  
            
            for(String object:listE)
            {
            SendEmail(object, "Backup error", " Error of backup database: "+DBPath+"\r to file: "+fileName+"\r Error: "+E.getMessage());
            }
            throw E;
        }  
        
        
    }

    synchronized public void MakeRestore(String fileBackup, String filetoRestore) throws Exception{
        
        try{
            
            if(filetoRestore.isEmpty()){                
                filetoRestore = fileBackup;
                filetoRestore = filetoRestore.replace(".fbk", ".gdb");
            }
            
            
            System.out.println("Restore "+new Date()+" Start restore: BackupFile="+fileBackup+" to "+filetoRestore);
                        
            
            Class.forName(DB_DRIVER);
            FBBackupManager bBackupManager = new FBBackupManager();
        
            bBackupManager.setBackupPath(fileBackup);

            bBackupManager.setHost(DBHost);
            bBackupManager.setPort(3050);
            bBackupManager.setDatabase(filetoRestore);
        
            bBackupManager.setUser(DBUser);
            bBackupManager.setPassword(DBPswd);
        
            bBackupManager.restoreDatabase();            
            
        } catch (Exception E) {
            
            System.out.println("BACKUP restore error:"+E.getMessage()); 
            
            for(String object:listE)
            {
            SendEmail(object, "Restore database error", " Error of restore backup database: "+fileBackup+"\r to: "+filetoRestore+"\r Error: "+E.getMessage());
            }
            throw E;
        }  
        
        
    }    
    

    public void SendEmail(String address, String subject, String content){
        
        try{
            Properties props = new Properties();
            props.setProperty("mail.transport.protocol", "smtp");
            props.setProperty("mail.smtp.port", "25"); //TLS Port
            props.setProperty("mail.host", "secure7.regeneron.com");
            props.setProperty("mail.user", "liconic@regeneron.com");
//            props.setProperty("mail.password", "hkovalov123");
//            props.setProperty("mail.smtp.auth", "true"); 
//            props.setProperty("mail.smtp.starttls.enable", "true"); //enable STARTTLS

            Authenticator auth = new Authenticator() {
                //override the getPasswordAuthentication method
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("liconic@regeneron.com", "R3g3n3ron775!");
                }
            };         
            
            
            Session mailSession = Session.getDefaultInstance(props, auth);
            //Session mailSession = Session.getDefaultInstance(props, null);
            Transport transport = mailSession.getTransport();

            MimeMessage message = new MimeMessage(mailSession);
            message.setSubject(subject);
            message.setContent(content, "text/plain");
            message.setFrom(new InternetAddress("liconic@regeneron.com"));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(address));

            transport.connect();                                  
            
            transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
            transport.close();
            
            System.out.println("BACKUP send e-mail:" +address+", "+subject+", "+content);
            
        }catch(Exception E){
            System.out.println("BACKUP send e-mail error:"+E.getMessage());
        }
              
    }
    
}
