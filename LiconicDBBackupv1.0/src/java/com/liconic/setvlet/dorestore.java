/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.liconic.setvlet;

import com.liconic.backup.Backup;
import com.liconic.backup.report.ReportBackup;
import com.liconic.backup.report.ReportPath;
import com.liconic.backup.report.ReportRestore;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@WebServlet(name = "dorestore", urlPatterns = {"/dorestore"})
public class dorestore extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        Backup backup = Backup.getInstance();
       
        String fileBackup = "";
        String fileDatabase = "";
        
        try{
            fileBackup = request.getParameter("backupFile");
        }catch(Exception E){
            System.out.println("\nBACKUP ERROR: restore, get backupFileName: " + E.getMessage());
        }
        
        try{
            fileDatabase = request.getParameter("backupDatabase");
        }catch(Exception E){
            System.out.println("\nBACKUP ERROR: restore, get Database: " + E.getMessage());
        }
        
        ReportRestore reportRestore = new ReportRestore(fileBackup, fileDatabase);
                
        try{           
            backup.MakeRestore(fileBackup, fileDatabase);
            Class.forName("org.firebirdsql.jdbc.FBDriver");
           Connection con = DriverManager.getConnection("jdbc:firebirdsql:localhost/3050:C:/Liconic/DB/BCKUP.GDB", "SYSDBA", "masterkey");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("Select id from EMail");
            while (rs.next()) {
                 
            backup.SendEmail(rs.getString(1), "Database restored", " Backup database file: "+fileBackup+"\r restored to: "+fileDatabase);
            }
            con.close();
            }
        catch(Exception E) {
            reportRestore.setError(E.getMessage());
        }
       
        HttpSession session = request.getSession(true);
        session.setAttribute("RestoreReport", reportRestore);       
        RequestDispatcher dispatcher = request.getRequestDispatcher("/main.jsp");                    
        dispatcher.forward(request, response); 
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}


