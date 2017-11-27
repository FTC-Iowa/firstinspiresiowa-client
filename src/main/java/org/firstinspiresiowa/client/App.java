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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author vens
 */
public class App {
    public static App app;
    public final Config config;
    public final Server server;
    public final DirWatcher fileWatcher;
    public final RootDirectory rootDirectory;
    
    private App() throws FileNotFoundException, IOException {
        app = this;
        try {
            config = new Config(); // get or create the config file
        } catch (FileNotFoundException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
        
        // create the server
        server = new Server(config.getServer());
        
        // open the reports directory to watch for changes
        //fileWatcher = new FileWatcher(config.getRootDir());
        fileWatcher = new DirWatcher();
        
        // create root directory object
        rootDirectory = new RootDirectory(config.getRootDir());
        
        fileWatcher.registerDirectory(rootDirectory);
    }

    private void run() {
        fileWatcher.start(); // start the file watcher thread
        server.startThread(1000); // start the server thread with an interval of 1000 (max of 1 server post per second)
        System.out.println("Hello");
        while (true) {
            
        }
    }
    
    
    
    
    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {
        App a = new App();
        a.run();
    }
}
