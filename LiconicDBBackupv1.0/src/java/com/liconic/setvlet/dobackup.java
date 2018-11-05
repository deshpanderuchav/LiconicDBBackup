/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.liconic.setvlet;

import com.liconic.backup.Backup;
import com.liconic.backup.report.ReportBackup;
import com.liconic.backup.report.ReportPath;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
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


/**
 *
 * @author GKo
 */
@WebServlet(name = "dobackup", urlPatterns = {"/dobackup"})
public class dobackup extends HttpServlet {

    Backup backup = Backup.getInstance();
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        
        
        
        String fileName = new SimpleDateFormat("YYYY_MM_DD_HH_mm_ss_SSS").format(new Date())+".fbk";
        String PathToTheFile = backup.getDBBackupPath()+File.separator+fileName;
        
        ReportBackup reportBackup = new ReportBackup(backup.getDBPath());
        
        try{
            
            reportBackup.getReportPath().setPath(PathToTheFile);
            backup.MakeBackup(PathToTheFile);
                            
            for (String path : backup.getBackupPaths()) {
            
                ReportPath reportPath = new ReportPath(path+File.separator+fileName, "");
                
                try {
                    CopyFile(PathToTheFile, path + File.separator + fileName);
                } catch (Exception E) {
                    reportPath.setError(E.getMessage());
                }
                
                reportBackup.getRemoteBackup().add(reportPath);
                
            }
                        
        }catch(Exception E) {
            reportBackup.getReportPath().setError(E.getMessage());
        }
       
        HttpSession session = request.getSession(true);
        
        session.setAttribute("BackupReport", reportBackup);
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("/main.jsp");                    
        dispatcher.forward(request, response); 
        
    }

    private void CopyFile(String srcFile, String toFile) throws Exception {

        try {

            // Copy
            System.out.println("BACKUP Copy file : " +srcFile + " to " + toFile);
            Files.copy(new java.io.File(srcFile).toPath(), new java.io.File(toFile).toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        }
        catch (IOException E) 
        {
            System.out.println("BACKUP ERROR Copy file : " +srcFile + " to " + toFile);
              try
              {
                  Class.forName("org.firebirdsql.jdbc.FBDriver");
              Connection con = DriverManager.getConnection("jdbc:firebirdsql:localhost/3050:C:/Liconic/DB/BCKUP.GDB", "SYSDBA", "masterkey");
          Statement stmt = con.createStatement();
           ResultSet rs = stmt.executeQuery("Select id from EMail");
          
             while (rs.next()) {
                 
           backup.SendEmail(rs.getString(1), "Error of copy database backup", " Error of copy backup database file: "+srcFile+"\r to: "+toFile+"\r Error: "+E.getMessage());
            }
             con.close();
              } 
            catch(Exception e) {
             System.out.println(e.getMessage());
            }
              throw E;
        }}
    
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
