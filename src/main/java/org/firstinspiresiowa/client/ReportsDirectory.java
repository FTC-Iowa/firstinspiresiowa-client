/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.firstinspiresiowa.client;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;

/**
 *
 * @author vens
 */
public class ReportsDirectory implements DirectoryEvents {
    private final File dirFile;
    
    private final Map<Path,ReportFile> fileMap;
    
    
    public ReportsDirectory(File rootDir) {
        dirFile = new File(rootDir, "reports");
        fileMap = new HashMap<>();
    }
    
    public boolean exists() {
        return dirFile.exists();
    }

    @Override
    public File getDirFile() {
        return this.dirFile;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onFileCreate(Path path) {
        String name = path.getFileName().toString();
        System.out.println("report File " + path.toAbsolutePath().toString() + " created");
        if (name.startsWith("TeamInfo")) {
            try {
                TeamInfoFile file = new TeamInfoFile(path.toFile());
                fileMap.put(path, file);
                JSONObject json = file.getServerData();
                if (json != null) {
                    App.app.server.Post(json, "/teams");
                }
            } catch (Exception ex) {
                Logger.getLogger(ReportsDirectory.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onFileDelete(Path path) {
        System.out.println("report File " + path + " deleted");
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onFileModify(Path path) {
        ReportFile file = fileMap.get(path);
        if(file != null) {
            JSONObject changes = file.onFileChange();
            if (changes != null) {
                try {
                    App.app.server.Post(changes, "/teams");
                } catch (Exception ex) {
                    Logger.getLogger(ReportsDirectory.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        System.out.println("report File " + path + " modified");
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
