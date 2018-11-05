package com.liconic.backup;

import com.liconic.backup.report.ReportBackup;
import com.liconic.backup.report.ReportPath;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimerTask;

public class BackupTimer extends TimerTask{

    private Backup backup;

    public BackupTimer(Backup backup) {
        this.backup = backup;
    }
    
    
    @Override
    public void run() {
           
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
        
        
    }

    private void CopyFile(String srcFile, String toFile) throws Exception {

        try {

            // Copy
            System.out.println("BACKUP Copy file : " +srcFile + " to " + toFile);
            Files.copy(new java.io.File(srcFile).toPath(), new java.io.File(toFile).toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);


        } catch (IOException E) {
            System.out.println("BACKUP ERROR Copy file : " +srcFile + " to " + toFile);
            throw E;
        }

    }
    
    
    
    
}
