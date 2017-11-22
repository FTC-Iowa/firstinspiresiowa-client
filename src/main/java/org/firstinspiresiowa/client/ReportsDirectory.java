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
        ReportFile reportFile = null;
        // if this is the TeamInfo file
        if (name.startsWith("TeamInfo")) {
            TeamInfoFile file = new TeamInfoFile(path.toFile());
            reportFile = file;
        }
        // if this is the Rankings file
        else if (name.startsWith("Rankings")) {
            RankingsFile file = new RankingsFile(path.toFile());
            reportFile = file;
        }
        // if this is the Matches file
        else if (name.startsWith("Matches")) {
            MatchesFile file = new MatchesFile(path.toFile());
            reportFile = file;
        }
        // if this is the MatchResultsDetails file
        else if (name.startsWith("MatchResultsDetails")) {
            MatchResultsDetailsFile file = new MatchResultsDetailsFile(path.toFile());
            reportFile = file;
        }
        // if this is the ElimLadder file
    //    else if (name.startsWith("ElimLadder")) {
    //        ElimLadderFile file = new ElimLadderFile(path.toFile());
    //        reportFile = file;
    //    }
        
        
        
        // add the file to the hash map, and make sure the server has the most
        // up-to-date information.
        if (reportFile != null) {
            fileMap.put(path, reportFile);
            JSONObject json = reportFile.getServerData();
            if (json != null) {
                App.app.server.QueuePost(json);
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
            // get any changes to the file
            JSONObject changes = file.onFileChange();
            // send those changes to the server async
            App.app.server.QueuePost(changes);
        }
        System.out.println("report File " + path + " modified");
    }
    
}
