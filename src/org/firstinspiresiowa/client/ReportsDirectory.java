/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.firstinspiresiowa.client;

import java.io.IOException;
import static java.nio.file.StandardWatchEventKinds.*;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author vens
 */
public class ReportsDirectory {
    private final WatchService watcher;
    private final Map<WatchKey,Path> keys;
    
    static <T> WatchEvent<T> cast(WatchEvent<?> event) {
        return (WatchEvent<T>)event;
    }
    
    private void register(Path dir) throws IOException {
        WatchKey key = dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
        keys.put(key, dir);
    }
    
    public ReportsDirectory (Path dir) throws IOException {
        this.watcher = FileSystems.getDefault().newWatchService();
        this.keys = new HashMap<WatchKey,Path>();
        
        register(dir);
    }
    
    public void proccessEvents() {
        for (;;) {
            WatchKey key;
            try {
                key = watcher.take();
            } catch (InterruptedException x) {
                return;
            }
            
            Path dir = keys.get(key);
            if (dir == null) {
                System.err.println("WatchKey not recognized!!");
                continue;
            }
            
            for (WatchEvent<?> event : key.pollEvents()) {
                WatchEvent.Kind kind = event.kind();
                
                if (kind == OVERFLOW) {
                    continue;
                }
                
                // Context for directory entry event is the file name of entry
                WatchEvent<Path> ev = cast(event);
                Path name = ev.context();
                Path child = dir.resolve(name);
                
                // print out event
                System.out.format("%s: %s\n", event.kind().name(), child);
            }
            
            boolean valid = key.reset();
            if (!valid) {
                keys.remove(key);
                if(keys.isEmpty()) {
                    break;
                }
            }
        }
    }
    
}
