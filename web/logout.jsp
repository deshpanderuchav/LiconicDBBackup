<%-- 
    Document   : logout.jsp
    Created on : Nov 23, 2015, 9:23:52 AM
    Author     : Rucha Deshpande
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Liconic Backup Service -Logout</title>
        <link rel="stylesheet" type="text/css" href="CSS/style.css">
      
    </head>
    <body>
        <div class="header">
        <img src="images/Logo.png" alt="Liconic" />
        <h1>Liconic - Backup Database</h1><br>
        <br><br><br><br><br><br><br><hr>
</div>
        <%

        session.removeAttribute("username");
        session.removeAttribute("password");
        session.invalidate();
        %>
        <h1 align ="center">Successfully logged out.</h1>
        
    </body>
</html>



