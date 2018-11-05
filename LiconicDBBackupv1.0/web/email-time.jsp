<%-- 
    Document   : email-time
    Created on : Nov 17, 2015, 12:48:28 PM
    Author     : Rucha Deshpande
--%>

<%@page import="com.liconic.backup.Backup"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import ="java.sql.*" %>
<%@ page import ="java.util.*" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
<%
        String []list = new String[20];
         try{
              Backup backup = Backup.getInstance();
         Class.forName("org.firebirdsql.jdbc.FBDriver");
Connection con = DriverManager.getConnection("jdbc:firebirdsql:localhost/3050:C:/Liconic/DB/BCKUP.GDB", "SYSDBA", "masterkey");
DatabaseMetaData md = con.getMetaData();
         Statement stmt = con.createStatement();
         String e= request.getParameter("email");
         String t= request.getParameter("time");
        
         ResultSet rt = md.getTables(null, null, "stime", null);
         ResultSet rs = md.getTables(null, null, "Email", null);
         
         if(request.getParameter("AddEmail") != null) 
         {
            if (rs.next()) 
            {
            stmt.executeUpdate("insert into Email(id) values('"+e+"');");
            }
            else
            {
             String Email =  "create table Email(id VARCHAR(30) PRIMARY KEY);";
             stmt.executeUpdate(Email);
             stmt.executeUpdate("insert into Email(id) values('"+e+"');");
            }
         }
         
         else if (request.getParameter("deleteEmail") != null)
         {      
                      
              stmt.executeUpdate("delete from Email where id ='"+e+"'");
         }
         
         else if (request.getParameter("addtime") != null)
         {
             if (rt.next()) 
            {
            stmt.executeUpdate("insert into stime(tid) values('"+t+"');");
            }
            else
            {               
             String time =  "create table stime(" + "tid VARCHAR(10) PRIMARY KEY);";
             stmt.executeUpdate("create table stime(" + "tid VARCHAR(10) PRIMARY KEY);");
           
             stmt.executeUpdate("insert into stime(tid) values('"+t+"');");
            }
              backup.RunBackUp();
         }
         else if (request.getParameter("deletetime") != null)
         {
              stmt.executeUpdate("delete from stime where tid ='"+t+"'");
               backup.RunBackUp();
         }     
         else if (request.getParameter("viewemail") != null)
         {
             int i=0;
             while(rs.next())
             {
                 list[i]=rs.getString(i+1);
             } 
         }
         else if (request.getParameter("viewschedule") != null)
         {
             
         }
            con.close();
         }
         
         catch(Exception E) {
                 System.out.println(E.getMessage());  
            }
                response.sendRedirect("main.jsp");
    %>
</body>
</html>
