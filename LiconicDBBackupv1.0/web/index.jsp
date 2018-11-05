<%-- 
    Document   : home
    Created on : Nov 10, 2015, 10:28:09 AM
    Author     : Rucha Deshpande
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import ="java.sql.*" %>
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
        
         <form method="post" action="login.jsp">
            <center>
            <table>
                <thead>
                    <tr>
                        <th colspan="2">Login</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>User Name</td>
                        <td><input type="text" name="uname" value="" /></td>
                    </tr>
                    <tr>
                        <td>Password</td>
                        <td><input type="password" name="pass" value="" /></td>
                    </tr>
                    <tr>
                        <td><input type="submit" value="Login" /></td>
                        <td><input type="reset" value="Reset" /></td>
                    </tr>
                    
                </tbody>
            </table>
            </center>
        </form>
        
        
    </body>
</html>
