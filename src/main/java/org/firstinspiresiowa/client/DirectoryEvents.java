/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.firstinspiresiowa.client;

import java.io.File;
import java.nio.file.Path;

/**
 *
 * @author vens
 */
interface DirectoryEvents {
    public File getDirFile();
    public void onFileCreate(Path path);
    public void onFileDelete(Path path);
    public void onFileModify(Path path);
    
}
