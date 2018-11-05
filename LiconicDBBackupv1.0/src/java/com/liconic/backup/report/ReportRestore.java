package com.liconic.backup.report;

public class ReportRestore {
    
    private String pathToBackup;
    private String pathToDatabase;
    private String error = "";

    public ReportRestore(String pathToBackup, String pathToDatabase) {
        this.pathToBackup = pathToBackup;
        this.pathToDatabase = pathToDatabase;
    }

    public String getPathToBackup() {
        return pathToBackup;
    }

    public void setPathToBackup(String pathToBackup) {
        this.pathToBackup = pathToBackup;
    }

    public String getPathToDatabase() {
        return pathToDatabase;
    }

    public void setPathToDatabase(String pathToDatabase) {
        this.pathToDatabase = pathToDatabase;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
    
    
    
    
    
}
