/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.liconic.backup;

import com.liconic.binding.conffiles.Parameter;
import com.liconic.binding.conffiles.ParameterGroup;
import com.liconic.binding.conffiles.Parameters;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

/**
 * Web application lifecycle listener.
 *
 * @author GKo
 */
public class AppListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        
        ServletContext context = sce.getServletContext();
               
        String ParamFile = "";

        String KIWIDBDriver = "";
    
        String DBUser = "";
        String DBPswd = "";
        String DBHost = "";
        String DBPath = "";
        String DBBackupPath = "";
                
        //String KIWIConnectString = "";       
        
        String SMTPAddress = "";
        String SMTPPort = "";
        String SMTPUser = "";
        String EMMailFrom = "";
       
        List<String> BackupPaths = new ArrayList<>();

        
        ParamFile = context.getInitParameter("ConfigFile");
        
        System.out.println("REPORT read config: Reading ConfFile:"+ParamFile);
        
       try 
       {


            JAXBContext jaxbContent = JAXBContext.newInstance(com.liconic.binding.conffiles.ObjectFactory.class);

            Unmarshaller um = jaxbContent.createUnmarshaller();

            FileInputStream fis = new FileInputStream(ParamFile);

            Parameters params = (Parameters) um.unmarshal(fis);

            for(int i=0; i<params.getParameterGroup().size(); i++){
                                
                ParameterGroup paramGroup = (ParameterGroup)params.getParameterGroup().get(i);
                
                if (paramGroup.getName().equals("KIWI Database")){
                    
                    for(int j=0; j<paramGroup.getParameter().size(); j++)
                    {
                        Parameter param = (Parameter)paramGroup.getParameter().get(j);
                        
                        if (param.getName().equals("KIWIDBDriver"))
                        {
                            KIWIDBDriver = param.getValue();
                        }   
                        else
                        if (param.getName().equals("HostName"))
                        {
                            DBHost = param.getValue();
                        }
                        else
                        if (param.getName().equals("Path"))
                        {
                            DBPath = param.getValue();
                        }     
                       
                    }                    
                    
                } else if (paramGroup.getName().equals("E-mail")){
                    
                    for (ParameterGroup reportGroup : paramGroup.getParameterGroup()){
                        
                        for(Parameter param : reportGroup.getParameter()){                                                       
                            
                            if (param.getName().equals("SMTPAddress")){
                                SMTPAddress = param.getValue();
                            }else
                            if (param.getName().equals("SMTPPort")){
                                SMTPPort = param.getValue();    
                            }else
                            if (param.getName().equals("SMTPUser")){
                                SMTPUser = param.getValue();
                            }else
                            if (param.getName().equals("E-mailFrom")){
                                EMMailFrom = param.getValue();
                            }
                            
                        }
                        
                    }
                }      
                
                
                for (ParameterGroup pg : paramGroup.getParameterGroup()){
                    
                    if(pg.getName().equals("Backup")){
                        
                        for(Parameter param : pg.getParameter()){
                            
                            if (param.getName().equals("UserName")){
                                DBUser = param.getValue();
                            }else if (param.getName().equals("Password")){
                                DBPswd = param.getValue();
                            }else if (param.getName().equals("Path")){
                                DBBackupPath = param.getValue();
                            }
                        }
                    }
                    
                    for(ParameterGroup pg1 : pg.getParameterGroup()){
                        
                        if(pg1.getName().equals("Path")){
                            
                            for(Parameter param : pg1.getParameter()){
                                
                               if(param.getName().equals("Path")) {
                                   BackupPaths.add(param.getValue());
                               }                                
                            }                            
                        }                        
                    }
                }                
            }
        } 
        catch (Exception ex)
        {
            System.out.println("BACKUP read config file error: "+ex.getMessage());
            ex.printStackTrace();
        }
       
        // STX DataBase
       
        System.out.println("");       
        System.out.println("");                                       
       
        System.out.println("BACKUP read config: STX Database Settings --------------------");                
        System.out.println("BACKUP read config: DBDriver: "+KIWIDBDriver);
        System.out.println("BACKUP read config: DBHost: "+DBHost);
        System.out.println("BACKUP read config: DBPath: "+DBPath);
        System.out.println("BACKUP read config: DBUser: "+DBUser);
        
        if (!DBPswd.isEmpty())
            System.out.println("BACKUP read config: DBPswd: ...");

        System.out.println("BACKUP read config: DBBackupPath: "+DBBackupPath);        
        
        for(String path : BackupPaths){
            System.out.println("BACKUP read config: Backup Path: "+path);
        }        
        
        System.out.println("");       
        System.out.println("");                                       
               
        Backup backup = Backup.getInstance();
        
        backup.setDBHost(DBHost);
        backup.setDBPath(DBPath);
        backup.setDBUser(DBUser);
        backup.setDBPswd(DBPswd);
        backup.setDBBackupPath(DBBackupPath);
        backup.setDB_DRIVER(KIWIDBDriver);
        
        backup.setBackupPaths(BackupPaths);
        
        context.setAttribute("LiconicDBBackup", backup);
        
        backup.RunBackUp();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        
    }
}
