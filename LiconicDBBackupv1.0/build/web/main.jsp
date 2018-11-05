<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.liconic.backup.Backup"%>
<%@page import="com.liconic.backup.report.ReportBackup"%>
<%@page import="com.liconic.backup.report.ReportRestore"%>
<%@page import="com.liconic.backup.report.ReportPath"%>
<%@ page import ="java.sql.*" %>

<%

    Backup backup = null;

    if (application.getAttribute("LiconicDBBackup") != null) {
        backup = (Backup) application.getAttribute("LiconicDBBackup");
    }
    
    ReportBackup reportBackup = null;
    
    if(session.getAttribute("BackupReport") != null){
        reportBackup = (ReportBackup)session.getAttribute("BackupReport");
    }
    
    ReportRestore reportRestore = null;
    
    if(session.getAttribute("RestoreReport") != null){
        reportRestore = (ReportRestore)session.getAttribute("RestoreReport");
    }    

%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Liconic Backup Service</title>
        <link rel="stylesheet" type="text/css" href="CSS/style.css">
    
    </head>
    <body>
        <div class="header">
		<img src="images/Logo.png" alt="Liconic" />
		<h1>Liconic - Backup Database</h1><br>
	</div>
	<br/><br/><br/><br/><br/>
        <hr>
        
       
        <%    if (backup != null) {%>

        <table style="float:left; margin-right:10px;">
            <tr>
                <th>DB Driver</th> 
                <td><%out.println(backup.getDB_DRIVER());%></td>
            <tr>
                <th>Host</th> 
                <td><%out.println(backup.getDBHost());%></td>
            <tr>
                <th>DB Path</th> 
                <td><%out.println(backup.getDBPath());%></td>
            <tr>
                <th>User</th> 
                <td><%out.println(backup.getDBUser());}%></td>
            </tr>
            <tr>
              <%  if (!backup.getDBPswd().isEmpty()) {%>
                <th>Password </th>
                <td>******</td>
                <%}%>
            </tr>
            <tr>
                <th>Local backup path</th> 
                <td><%out.println(backup.getDBBackupPath());%></td>
            </tr>
            <tr>
                <%for (String path : backup.getBackupPaths()) {%>
               <tr> <th>Remote backup path</th>  
                <td><%out.println(path);
                }%></td></tr>
            
        
            <form method="POST" action="dobackup">
                <tr><td colspan = "2" align ="center"> <input type="submit" value="  Backup  "></td></tr></table>
                    </form>
             
                <table>
                  
                <form method="POST" action="dorestore">
                    <tr>
                        <th>Backup file</td>
                        <td><input type="text" name="backupFile" size="30"></td>
                    </tr>
                    <tr>
                        <th>Restore to database</td>
                        <td> <input type="text" name="backupDatabase" size="30"></td>
                    </tr>
                    <tr>
                        <td colspan="2" align ="center"><input type="submit" value="  Restore  "></td>
                    </tr>
                </form>
                </table>
               <br/>
               <table style="float:left;  margin-right:10px;" >
                 
                   <tr>
                       <th colspan="2">Schedule backup time</th>
                   </tr>
                   <form action="email-time.jsp" method="post" >              
                       <tr>  <td><input type="text" name="time"></td></tr>
                       
                       <tr><td><input type="submit" value="  Add  " name="addtime">   <input type="submit" value="  Delete  " name="deletetime"> <!--<input type="submit" value="  View  " name="viewschedule">--></td> </tr>
                   
                   </form>     
               </table>
                 
                   <table style="float:left;">
                       <form action="email-time.jsp" method="post" >    
                       <tr>
                           <th colspan="2">Email Notification</th>
                       </tr>
                      <tr>
                          <td><input type="text" name="eMail"></td>
                      </tr>
                      <tr>
                         <td><input type="submit" value="  Add  " name="AddEmail">   <input type="submit" value="  Delete  " name="deleteEmail"><!--<input type="submit" value="  View  " name="viewemail">--></td>
                      </tr>
                   
                       </form>
                       
                   </table> <br/><br/> <br/><br/><br/><br/><br/><br/>
               <br/><br/><hr>
                <%    
            if (reportBackup != null){%>
            <br/><table>              
                <tr>
                    <td>Backup</td> 
                    <td>  <%out.println(reportBackup.getBackupDB());%></td>
                </tr>
                <tr>
                    <%if (reportBackup.getReportPath().getError().isEmpty()){%>
                    
                    <td>Backup to</td>
                    <td><%out.println(reportBackup.getReportPath().getPath());%> - Done</td>
                <%}else{%>
                <tr>
                    <td> Backup to </td>
                    <td><%out.println(reportBackup.getReportPath().getPath());%> - Error: <%out.println(reportBackup.getReportPath().getError());} %></td>
                </tr>
                
                
               <% if (!reportBackup.getRemoteBackup().isEmpty()){%>
               <tr>
                   <td>Backup copy</td>
                   <td>
                   <%} for (ReportPath rp : reportBackup.getRemoteBackup()){

                    if (rp.getError().isEmpty()){%>
                    Copy to: <% out.println(rp.getPath());%> - Done </br>
                    <%}else{%>
                     <br/> Copy to: <% out.println(rp.getPath());%> - Error: <% out.println(rp.getError());
                    }                    
                } %> <br/><br/> </td>  
           <% session.removeAttribute("BackupReport");
            }
        %>    
                   </tr>
            </table><br/>
          
                
        <%  
            if (reportRestore != null){%>
            <table>
            <tr>
                <td>Restore file </td>
                <td><%out.println(reportRestore.getPathToBackup());%></td>     
            </tr>
             
            <% if (reportRestore.getError().isEmpty()){%>
            <tr>
                <td>Restore to</td>
                <td><%out.println(reportRestore.getPathToDatabase());%> - Done</td>
            </tr>
             <%}else{%>
              <tr>
                <td>Restore to </td>
                <td><%out.println(reportRestore.getPathToDatabase());%> - Error: <%out.println(reportRestore.getError());%></tc
             <%   }
                session.removeAttribute("RestoreReport");
            }%> 
              </tr>
            </table>
    </body>
</html>
    
