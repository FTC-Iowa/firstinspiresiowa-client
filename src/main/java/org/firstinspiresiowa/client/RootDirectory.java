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
                App.app.fileWatcher.registerDirectory(reportsDirectory);
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
                App.app.fileWatcher.registerDirectory(reportsDirectory);
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
