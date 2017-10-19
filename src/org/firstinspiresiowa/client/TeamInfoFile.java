/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.firstinspiresiowa.client;

import java.io.File;
import java.util.ArrayList;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author vens
 */
public class TeamInfoFile extends ReportFile implements JsonAble {
    private final ArrayList<Team> teams;
    //public CollisionManager(ArrayList<? extends JsonAble>) teams;
    
    public TeamInfoFile(File _file) throws Exception{
        super(_file);
        this.teams = new ArrayList<>();
        firstParseHtmlFile();
    }
    
    private void firstParseHtmlFile() throws Exception{
        Element teamInfoTable = this.getHtmlTable();
        Elements teamInfoRows = teamInfoTable.getElementsByTag("tr");
        for(int i = 1; i< teamInfoRows.size(); i++) {
            Element row = teamInfoRows.get(i);
            Team t = new Team(row);
            teams.add(i - 1, t);
            System.out.println("Found team: " + t);
        }
    }
    
    
    
    /**
     * Called whenever the file changes on the disk
     * @return A list of teams that have changed between the disk and ram versions of the file.
     * @throws Exception 
     */
    @Override
    public ArrayList<JsonAble> onFileChange()  throws Exception{
        Element teamInfoTable = this.getHtmlTable();
        Elements teamInfoRows = teamInfoTable.getElementsByTag("tr");
        ArrayList<JsonAble> ret = new ArrayList<>();
        for(int i = 1; i< teamInfoRows.size(); i++) {
            Element row = teamInfoRows.get(i);
            Team t = new Team(row);
            if(!t.equals(teams.get(i-1))) {
                System.out.println("Team " + t.toString() + " changed.");
                teams.set(i-1, t);
                ret.add(t);
            }
        }
        return ret;
    }

    @Override
    public String buildJson() {
        return JsonAble.buildJsonArray("teams", teams);
    }    
}
