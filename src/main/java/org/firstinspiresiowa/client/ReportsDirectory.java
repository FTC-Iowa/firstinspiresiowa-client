/*
 * Copyright 2017 Jeramie Vens.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
            if(changes != null)
                App.app.server.QueuePost(changes);
        }
        System.out.println("report File " + path + " modified");
    }
    
}
