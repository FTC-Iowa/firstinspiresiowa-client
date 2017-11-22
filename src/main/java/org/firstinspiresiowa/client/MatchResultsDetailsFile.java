/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.firstinspiresiowa.client;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author vens
 */
class MatchResultsDetailsFile extends ReportFile {
    private final ArrayList<Match> matches;
    
    public MatchResultsDetailsFile(File _file) {
        super(_file);
        matches = parseHtmlFile();
    }
    
    private ArrayList<Match> parseHtmlFile() {
        ArrayList<Match> fileMatches = new ArrayList<>();
        Element table;
        try {
            table = this.getHtmlTable();
        } catch (Exception ex) {
            Logger.getLogger(TeamInfoFile.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        Elements rows = table.getElementsByTag("tr");
        for(int i = 2; i< rows.size(); i++) {
            Element row = rows.get(i);
            Match m = new Match(row, i-2);
            fileMatches.add(m);
            //System.out.println("Found match: " + m);
        }
        
        return fileMatches;
    }

    @Override
    public JSONObject onFileChange() {
        ArrayList<Match> newMatches = parseHtmlFile();
        ArrayList<Jsonable> changed = new ArrayList<>();
        
        int i = 0;
        if (matches.size() <= newMatches.size()) {
            for(i=0;i<matches.size();i++) {
                if (!matches.get(i).equals(newMatches.get(i))) {
                    System.out.println("Match " + matches.get(i) + " changed");
                    matches.set(i, newMatches.get(i));
                    changed.add(newMatches.get(i));
                }
            }
        }
        if (newMatches.size() > matches.size()) {
            for(;i<newMatches.size(); i++) {
                    System.out.println("Match " + matches.get(i) + " added");
                matches.add(newMatches.get(i));
                changed.add(newMatches.get(i));
            }
        }
        
        if(changed.size()>0){
            JSONArray matchArray = new JSONArray();
            changed.forEach((m) -> {
                matchArray.add(m.toJson());
            });
            JSONObject json = new JSONObject();
            json.put("matches", matchArray);
            return json;
        } else {
            return null;
        }
        
        
        //ArrayList<Match> changed = matche
        
        
        
//        
//        ArrayList<Jsonable> array = new ArrayList<>();
//        Element table;
//        try {
//            table = this.getHtmlTable();
//        } catch (Exception ex) {
//            return null;
//        }
//        Elements rows = table.getElementsByTag("tr");
//        for(int i = 2; i< rows.size(); i++) {
//            Element row = rows.get(i);
//            Match m = new Match(row, i-2);
//            if(!m.equals(matches.get(i-2))) {
//                System.out.println("Match " + m.toString() + " changed.");
//                matches.set(i-2, m);
//                array.add(m);
//            }
//        }
//        
//        if(array.size()>0){
//            JSONArray matchArray = new JSONArray();
//            array.forEach((m) -> {
//                matchArray.add(m.toJson());
//            });
//            JSONObject json = new JSONObject();
//            json.put("matches", matchArray);
//            return json;
//        } else {
//            return null;
//        }
    }

    @Override
    public JSONObject getServerData() {
        if(matches.size()>0){
            JSONArray array = new JSONArray();
            matches.forEach((i) -> {
                array.add(i.toJson());
            });
            JSONObject json = new JSONObject();
            json.put("matches", array);
            return json;
        } else {
            return null;
        }
    }

    
}
