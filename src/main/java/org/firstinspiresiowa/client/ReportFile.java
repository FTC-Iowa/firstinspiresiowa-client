/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.firstinspiresiowa.client;

import java.io.File;
import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author vens
 */
public abstract class ReportFile {
    protected File file;
    
    public ReportFile(File _file){
        file = _file;
    }
    
    protected Element getHtmlTable() throws Exception{
        Document doc = Jsoup.parse(file, "UTF-8", "");
        Elements tables = doc.getElementsByTag("table");
        if (tables.isEmpty()) {
            System.err.println("Could not find table in HTML document");
            throw new Exception();
        }
        return tables.first();
    }
    
    public abstract JSONObject onFileChange();
    
    public abstract JSONObject getServerData();
    
    // report Files:
    //     MatchResultsDetails, Matches, Rankings, TeamInfo
}
