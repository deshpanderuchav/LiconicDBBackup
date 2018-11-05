/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.liconic.backup;


import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;
/**
 *
 * @author Rucha Deshpande
 */
class DeleteBackup extends TimerTask {
      private Backup backup;

    public DeleteBackup(Backup backup) {
        this.backup = backup;
    }
    
      @Override
    public void run() {
        
        List<String> remotePath = backup.getBackupPaths();
        for(int j=0;j<remotePath.size();j++)
        {
           File folder = new File(remotePath.get(j));
           File[] listOfFiles = folder.listFiles();
           
           if(listOfFiles ==null)
           {
               System.out.println("Folder "+ remotePath.get(j)+" does not exists.");
           }

           else{

           if(listOfFiles.length == 0)
           {
               System.out.println("No files in backup folder: "+ remotePath.get(j));
           }

           else{
           for (int i = 0; i < listOfFiles.length; i++) {
           if (listOfFiles[i].isFile()) {
              Date fileDate = new Date(listOfFiles[i].lastModified());
Calendar cal = Calendar.getInstance();
              cal.add(Calendar.MONTH,-3);
//               System.out.println(cal.getTime() + " is after "+ fileDate);
               // System.out.println(fileDate);
              if(cal.getTime().after(fileDate))
              {
                 if(listOfFiles[i].delete()) 
                     System.out.println("DB backup file "+ listOfFiles[i].getAbsolutePath() + "is deleted." );
                 else
                     System.out.println("Error deleting DB backup file "+ listOfFiles[i].getAbsolutePath());
              }
           }
           }
           }
           }     


            }

       File folder = new File(backup.getDBBackupPath());
       File[] listOfFiles = folder.listFiles();
       
       if(listOfFiles ==null)
       {
           System.out.println("Folder does not exists.");
       }
       
       else{
           
       if(listOfFiles.length == 0)
       {
           System.out.println("No files in backup folder");
       }
       
       else{
       for (int i = 0; i < listOfFiles.length; i++) {
       if (listOfFiles[i].isFile()) {
          Date fileDate = new Date(listOfFiles[i].lastModified());
          Calendar cal = Calendar.getInstance();
          cal.add(Calendar.MONTH,-3);
         //  System.out.println(cal.getTime() + " is after "+ fileDate);
           // System.out.println(fileDate);
          if(cal.getTime().after(fileDate))
          {
             if(listOfFiles[i].delete()) 
                 System.out.println("DB baclup file "+ listOfFiles[i].getName() + "is deleted." );
             else
                 System.out.println("Error deleting DB backup file "+ listOfFiles[i].getName());
          }
       }
       }
       }
       }
    }
}
