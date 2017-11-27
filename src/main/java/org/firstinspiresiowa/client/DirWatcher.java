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
import static java.nio.file.StandardWatchEventKinds.*;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author vens
 */
public class DirWatcher extends Thread{
    private final WatchService watcher;
    private final Map<WatchKey,Path> keys;
    private final Map<WatchKey,DirectoryEvents> callbacks;
    
    static <T> WatchEvent<T> cast(WatchEvent<?> event) {
        return (WatchEvent<T>)event;
    }
    
    public void registerDirectory(DirectoryEvents directory) throws IOException {
        File dir = directory.getDirFile();
        WatchKey key = dir.toPath().register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
        keys.put(key, dir.toPath());
        callbacks.put(key, directory);
    }
    
    /**
     * Create a file watcher that will start watching the supplied directory.
     * @throws IOException 
     */
    public DirWatcher () throws IOException {
        this.watcher = FileSystems.getDefault().newWatchService();
        this.keys = new HashMap<>();
        this.callbacks = new HashMap<>();
    }
    
    @Override
    public void run() {
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
                //System.out.format("%s: %s\n", event.kind().name(), name.toAbsolutePath());
                
                DirectoryEvents d = callbacks.get(key);
                if (kind == ENTRY_CREATE) {
                    d.onFileCreate(child);
                } else if (kind == ENTRY_DELETE) {
                    d.onFileDelete(child);
                } else if (kind == ENTRY_MODIFY) {
                    d.onFileModify(child);
                } else {
                    System.err.println("Unknown event type: " + kind.name());
                }
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
