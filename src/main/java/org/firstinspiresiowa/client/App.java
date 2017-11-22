/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
