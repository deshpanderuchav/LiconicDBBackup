<%-- 
    Document   : login
    Created on : Nov 10, 2015, 9:30:53 AM
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
        
<%
    String userid = request.getParameter("uname");    
    String pwd = request.getParameter("pass");
 if((userid.equals("ADMIN")) && (pwd.equals("backupkey")))
                {
                     response.sendRedirect("main.jsp");
                }
  
%>             