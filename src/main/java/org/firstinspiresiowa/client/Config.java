/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.firstinspiresiowa.client;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
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
    public final String file_name;
    public String event_ref;
    public String server;
    public String passphrase;
    public String file_sufix;
    public Path directory;
    
    public Config(String _file_name){
        file_name = _file_name;
        JSONParser parser = new JSONParser();
        try {
            FileReader fileReader = new FileReader(file_name);
            JSONObject json = (JSONObject) parser.parse(fileReader);
            event_ref = (String) json.get("event_ref");
            server = (String) json.get("server");
            passphrase = (String) json.get("passphrase");
            file_sufix = (String) json.get("file_sufix");
            directory = FileSystems.getDefault().getPath((String) json.get("directory"));
            
            /// @TODO confirm if this is the correct event.
        } catch (FileNotFoundException ex) {
            buildConfigInteractive();
           //save();
        } catch (IOException | ParseException ex) {
            ex.printStackTrace();
        }
        System.out.println(event_ref);
        System.out.println(server);
        System.out.println(passphrase);
        System.out.println(file_sufix);
        System.out.println(directory);
    }
    
    public void buildConfigInteractive() {
        // Get Score System Directory
        System.out.println("Select the root directory of the Score System App...");
        System.out.println("\tthis is the folder that was created when you extracted the .zip file");
        System.out.print("Enter root directory... ");
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fc.setDialogTitle("Score System Directory");
        int rv = fc.showDialog(null, "Select");
        
        if (rv == JFileChooser.APPROVE_OPTION) {
            directory = fc.getSelectedFile().toPath();
        } else {
            System.err.println("Failed to open score system directory");
            return;
        }
        System.out.println(directory);
        
        // Get the server
        System.out.print("Enter the address of the remote server... ");
        server = (String)JOptionPane.showInputDialog(
                "Enter the address of the remote server",
                "http://localhost:5000");
        System.out.println(server);
        
        // Get passphrase
        System.out.print("Enter server passphase... ");
        passphrase = (String)JOptionPane.showInputDialog(
                "Enter server passphase");
        System.out.println(passphrase);
    }
    
    public void save() {
        JSONObject json = new JSONObject();
        json.put("event_ref", event_ref);
        json.put("server", server);
        json.put("passphrase", passphrase);
        json.put("file_sufix", file_sufix);
        json.put("reports_dir", directory.toAbsolutePath());
        
        try {
            FileWriter jsonFileWriter;
            jsonFileWriter = new FileWriter(file_name);
            jsonFileWriter.write(json.toJSONString());
            jsonFileWriter.flush();
            jsonFileWriter.close();
        } catch (IOException ex) {
            Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
