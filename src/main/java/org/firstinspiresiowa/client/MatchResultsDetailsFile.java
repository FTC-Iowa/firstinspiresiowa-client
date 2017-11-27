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
    private JSONArray matches;
    
    public MatchResultsDetailsFile(File _file) {
        super(_file);
        matches = parseHtmlFile();
    }
    
    private static String trimString(String str) {
        return str.substring(0, str.length() - 1);
    }
    
    private JSONObject buildAlliance(String teamstring) {
        JSONObject json = new JSONObject();
        JSONArray teams = new JSONArray();
        JSONArray surrogates = new JSONArray();
        
        String[] teamArray = teamstring.split("\\s");
        
        if(teamArray[0].endsWith("*")) {
            teamArray[0] = trimString(teamArray[0]);
            surrogates.add(Integer.parseInt(teamArray[0]));
        }
        teams.add(Integer.parseInt(teamArray[0]));
        
        if(teamArray[1].endsWith("*")) {
            teamArray[1] = trimString(teamArray[1]);
            surrogates.add(Integer.parseInt(teamArray[1]));
        }
        teams.add(Integer.parseInt(teamArray[1]));
        
        if(teamArray.length > 2) {
            if(teamArray[2].endsWith("*")) {
                teamArray[2] = trimString(teamArray[2]);
                surrogates.add(Integer.parseInt(teamArray[2]));
            }
            teams.add(Integer.parseInt(teamArray[2]));
        }
        
        json.put("teams", teams);
        if(!surrogates.isEmpty()) {
            json.put("surrogates", surrogates);
        }
        
        return json;
    }
    
    private JSONObject buildAlliance(String teamstring, int tot, int auto, int autob, int tele, int endg, int pen) {
        JSONObject json = buildAlliance(teamstring);
        
        json.put("auto", auto);
        json.put("auto_b", autob);
        json.put("end_game", endg);
        json.put("penalty", pen);
        json.put("tele_op", tele);
        json.put("total", tot);
        
        return json;
    }
    
    private JSONObject parseRow(Element row, int number) {
    JSONObject json = new JSONObject();
        Elements cols = row.getElementsByTag("td");
        
        
        json.put("number", number);
        json.put("name", cols.get(0).text());
        
        JSONObject red, blue;
        
        // if this is an empty match
        if (!cols.get(1).text().contains("-")) {
            red = buildAlliance (cols.get(2).text());
            blue = buildAlliance (cols.get(3).text());
        } else {
            red = buildAlliance (
                    cols.get(2).text(),
                    Integer.parseInt(cols.get(4).text()),
                    Integer.parseInt(cols.get(5).text()),
                    Integer.parseInt(cols.get(6).text()),
                    Integer.parseInt(cols.get(7).text()),
                    Integer.parseInt(cols.get(8).text()),
                    Integer.parseInt(cols.get(9).text())                
                );
            blue = buildAlliance (
                    cols.get(3).text(),
                    Integer.parseInt(cols.get(10).text()),
                    Integer.parseInt(cols.get(11).text()),
                    Integer.parseInt(cols.get(12).text()),
                    Integer.parseInt(cols.get(13).text()),
                    Integer.parseInt(cols.get(14).text()),
                    Integer.parseInt(cols.get(15).text())                
                );
        }
        json.put("red", red);
        json.put("blue", blue);
        
        return json;
    }
    
    private JSONArray parseHtmlFile() {
        JSONArray array = new JSONArray();
        Element table;
        try {
            table = this.getHtmlTable();
        } catch (Exception ex) {
            Logger.getLogger(TeamInfoFile.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        Elements rows = table.getElementsByTag("tr");
        
        if ( rows.size() <= 2 )
            return null;
        
        for(int i = 2; i< rows.size(); i++) {
            Element row = rows.get(i);
            try {
                JSONObject obj = parseRow(row, i-2);
                array.add(obj);
            } catch (Exception e) {
                Logger.getLogger(TeamInfoFile.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        
        return array;
    }

    @Override
    public JSONObject onFileChange() {
        JSONArray newMatches = parseHtmlFile();
        if(newMatches != null && !matches.equals(newMatches)) {
            matches = newMatches;
            return getServerData();
        } else {
            return null;
        }
    }

    @Override
    public JSONObject getServerData() {
        JSONObject json = new JSONObject();
        json.put("matches", matches);
        return json;
    }

    
}
