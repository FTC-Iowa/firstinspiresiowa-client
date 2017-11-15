/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.firstinspiresiowa.client;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author vens
 */
public final class Config {
    /// file name of the config file for this event
    public final String FILE_NAME = "firstinspiresiowa.json";
    /// the config file
    private File cfgFile;
    /// ref that all queries to the server contain to id to this event
    private String event_ref;
    /// url of the server we are connected to
    private String server;
    /// server passphrase for login credentials
    private String passphrase;
    /// the file suffix that gets attached by the score system to each html file
    private String file_sufix;
    /// the root directory of the score system.
    private File rootDir;
    
    public Config() throws FileNotFoundException{
        rootDir = new File(".");
        cfgFile = new File(rootDir, this.FILE_NAME);
        // see if it exists in the current working directory.  If it doesn't
        // get the correct working directory and switch to it.
        if(!cfgFile.exists()) {
            // the file didn't exist in the working directory, so let's see if 
            // we are in the correct directory
            rootDir = getScoreSystemDirectory();
            cfgFile = new File(rootDir, this.FILE_NAME);
        }
        
        // see if the file now exists.  If it does load it, but if not we need
        // to create a new config file.
        if(cfgFile.exists()) {
            try {
                readConfigFile(this.cfgFile);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            buildConfigInteractive();
            saveConfigFile(cfgFile);
        }
        System.out.println(event_ref);
        System.out.println(server);
        System.out.println(passphrase);
        System.out.println(file_sufix);
        //System.out.println(directory);
    }
    
    public void save() {
        saveConfigFile(cfgFile);
    }
    
    public String getEvent_ref() {
        return event_ref;
    }

    public String getServer() {
        return server;
    }

    public String getPassphrase() {
        return passphrase;
    }

    public File getRootDir() {
        return rootDir;
    }
    
    private void readConfigFile(File file) throws IOException {
        try {
            FileReader fileReader = new FileReader(file);
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(fileReader);
            this.event_ref = (String) json.get("event_ref");
            this.server = (String) json.get("server");
            this.passphrase = (String) json.get("passphrase");
            this.file_sufix = (String) json.get("file_sufix");
            //this.directory = FileSystems.getDefault().getPath((String) json.get("directory"));
        } catch (ParseException ex) {
            Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void saveConfigFile(File file) {
        JSONObject json = new JSONObject();
        json.put("event_ref", this.event_ref);
        json.put("server", this.server);
        json.put("passphrase", this.passphrase);
        json.put("file_sufix", this.file_sufix);
        //json.put("reports_dir", this.directory.toAbsolutePath().toString());
        
        try {
            FileWriter jsonFileWriter;
            jsonFileWriter = new FileWriter(file);
            jsonFileWriter.write(json.toJSONString());
            jsonFileWriter.flush();
            jsonFileWriter.close();
        } catch (IOException ex) {
            Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private File getScoreSystemDirectory() throws FileNotFoundException {
        System.out.println("Select the root directory of the Score System App...");
        System.out.println("\tthis is the folder that was created when you extracted the .zip file");
        System.out.print("Enter root directory... ");
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fc.setDialogTitle("Score System Directory");
        int rv = fc.showDialog(null, "Select");
        Path directory;
        if (rv == JFileChooser.APPROVE_OPTION) {
            directory = fc.getSelectedFile().toPath();
        } else {
            System.err.println("Failed to open score system directory");
            throw new FileNotFoundException();
        }
        System.out.println(directory.toString());
        
        return fc.getSelectedFile();
    }
    
    private void buildConfigInteractive() {        
        // Get the server
        System.out.print("Enter the address of the remote server... ");
        server = (String)JOptionPane.showInputDialog(
                "Enter the address of the remote server",
                Server.DEFAULT_SERVER);
        System.out.println(server);
        
        // Get passphrase
        System.out.print("Enter server passphase... ");
        passphrase = (String)JOptionPane.showInputDialog(
                "Enter server passphase");
        System.out.println(passphrase);
    }
}
