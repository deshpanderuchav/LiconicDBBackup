/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.liconic.backup.report;

import java.util.ArrayList;
import java.util.List;


public class ReportBackup {
    
    private String BackupDB;
    private ReportPath reportPath;
    private List<ReportPath> remoteBackup;

    public ReportBackup(String BackupDB) {
        this.BackupDB = BackupDB;
        reportPath = new ReportPath("", "");
        remoteBackup = new ArrayList<>();
    }

    public String getBackupDB() {
        return BackupDB;
    }
        
    public ReportPath getReportPath() {
        return reportPath;
    }

    public List<ReportPath> getRemoteBackup() {
        return remoteBackup;
    }
    
    
    
    
}
