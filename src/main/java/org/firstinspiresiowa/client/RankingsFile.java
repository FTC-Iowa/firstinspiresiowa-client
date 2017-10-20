/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.firstinspiresiowa.client;

import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author vens
 */
class RankingsFile extends ReportFile {

    public RankingsFile(File _file) {
        super(_file);
    }

    @Override
    public ArrayList<Jsonable> onFileChange() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
