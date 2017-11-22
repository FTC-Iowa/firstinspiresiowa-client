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
public class TeamInfoFile extends ReportFile {
    private final ArrayList<Team> teams;
    //public CollisionManager(ArrayList<? extends JsonAble>) teams;
    
    public TeamInfoFile(File _file){
        super(_file);
        this.teams = new ArrayList<>();
        firstParseHtmlFile();
    }
    
    private void firstParseHtmlFile(){
        Element teamInfoTable;
        try {
            teamInfoTable = this.getHtmlTable();
        } catch (Exception ex) {
            Logger.getLogger(TeamInfoFile.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        Elements teamInfoRows = teamInfoTable.getElementsByTag("tr");
        for(int i = 1; i< teamInfoRows.size(); i++) {
            Element row = teamInfoRows.get(i);
            Team t = new Team(row);
            teams.add(i - 1, t);
            System.out.println("Found team: " + t);
        }
    }
    
    public ArrayList<Team> getTeamList() {
        return teams;
    }
    
    @Override
    public JSONObject getServerData() {
        if(teams.size()>0){
            JSONArray teamArray = new JSONArray();
            teams.forEach((t) -> {
                teamArray.add(t.toJson());
            });
            JSONObject json = new JSONObject();
            json.put("teams", teamArray);
            return json;
        } else {
            return null;
        }
    }
    
    /**
     * Called whenever the file changes on the disk
     * @return A list of teams that have changed between the disk and ram versions of the file.
     * @throws Exception 
     */
    @Override
    public JSONObject onFileChange(){
        ArrayList<Jsonable> array = new ArrayList<>();
        Element teamInfoTable;
        try {
            teamInfoTable = this.getHtmlTable();
        } catch (Exception ex) {
            return null;
        }
        Elements teamInfoRows = teamInfoTable.getElementsByTag("tr");
        for(int i = 1; i< teamInfoRows.size(); i++) {
            Element row = teamInfoRows.get(i);
            Team t = new Team(row);
            if(!t.equals(teams.get(i-1))) {
                System.out.println("Team " + t.toString() + " changed.");
                teams.set(i-1, t);
                array.add(t);
            }
        }
        
        if(array.size()>0){
            JSONArray teamArray = new JSONArray();
            array.forEach((t) -> {
                teamArray.add(t.toJson());
            });
            JSONObject json = new JSONObject();
            json.put("teams", teamArray);
            return json;
        } else {
            return null;
        }
    }
}
