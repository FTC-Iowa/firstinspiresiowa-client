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
class MatchResultsDetailsFile extends ReportFile {

    public MatchResultsDetailsFile(File _file) {
        super(_file);
    }

    @Override
    public JSONObject onFileChange() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public JSONObject getServerData() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
}