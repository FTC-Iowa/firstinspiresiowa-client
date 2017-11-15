/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.firstinspiresiowa.client;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author vens
 */
public class RootDirectory implements DirectoryEvents{
    private final File directory;
    private final ReportsDirectory reportsDirectory;
    
    
    public RootDirectory(File rootDir) {
        directory = rootDir;
        reportsDirectory = new ReportsDirectory(rootDir);
        if (reportsDirectory.exists()) {
            try {
                App.app.fileWatcher.register(reportsDirectory);
            } catch (IOException ex) {
                Logger.getLogger(RootDirectory.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void onFileCreate(Path path) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        System.out.println("File " + path + " created");
        if(path.endsWith("reports")){
            try {
                App.app.fileWatcher.register(reportsDirectory);
            } catch (IOException ex) {
                Logger.getLogger(RootDirectory.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void onFileDelete(Path path) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        System.out.println("File " + path + " deleted");
    }

    @Override
    public void onFileModify(Path path) {
       //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        System.out.println("File " + path + " modified");
    }

    @Override
    public File getDirFile() {
        return directory;
    }
    
}
