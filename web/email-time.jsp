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
         String e= request.getParameter("eMail");
         String t= request.getParameter("time");
                       
         if(request.getParameter("AddEmail") != null) 
         {
             //System.out.println("add"+e); 
             ResultSet rs = md.getTables(null, null, "notify", null);
            if (rs.next()) 
            {
            stmt.executeUpdate("insert into notify(id) values('"+e+"');");
            //  System.out.println(e+"added");  
            }
            else
            {
             String Email =  "create table notify(id VARCHAR(40) PRIMARY KEY);";
             stmt.executeUpdate(Email);
             stmt.executeUpdate("insert into notify(id) values('"+e+"');");
               //System.out.println(e+"added");  
            }
         }
         
         else if (request.getParameter("deleteEmail") != null)
         {      
                      
              stmt.executeUpdate("delete from notify where id ='"+e+"'");
               // System.out.println(e+"deleted");  
         }
         
         else if (request.getParameter("addtime") != null)
         {
              ResultSet rt = md.getTables(null, null, "stime", null);
         
             if (rt.next()) 
            {
            stmt.executeUpdate("insert into stime(tid) values('"+t+"');");
             // System.out.println(t+"added");  
            }
            else
            {               
             String time =  "create table stime(" + "tid VARCHAR(10) PRIMARY KEY);";
             stmt.executeUpdate("create table stime(" + "tid VARCHAR(10) PRIMARY KEY);");
           
             stmt.executeUpdate("insert into stime(tid) values('"+t+"');");
           System.out.println(t+"added");  
            }
              backup.RunBackUp();
         }
         else if (request.getParameter("deletetime") != null)
         {
              stmt.executeUpdate("delete from stime where tid ='"+t+"'");
            //  System.out.println(t+"deleted");  
               backup.RunBackUp();
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
