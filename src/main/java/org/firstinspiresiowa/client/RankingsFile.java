/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.firstinspiresiowa.client;

import java.io.File;
import org.json.simple.JSONObject;

/**
 *
 * @author vens
 */
class RankingsFile extends ReportFile {

    public RankingsFile(File _file) {
        super(_file);
    }

   

    @Override
    public JSONObject getServerData() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return null;
    }

    @Override
    public JSONObject onFileChange() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return null;
    }
    
}
