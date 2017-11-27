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
class RankingsFile extends ReportFile {

    private JSONObject parseRow(Element row) {
        JSONObject json = new JSONObject();
        Elements cols = row.getElementsByTag("td");
        
        json.put("rank", Integer.parseInt(cols.get(0).text()));
        json.put("team", Integer.parseInt(cols.get(1).text()));
        json.put("qp", Integer.parseInt(cols.get(3).text()));
        json.put("rp", Integer.parseInt(cols.get(4).text()));
        json.put("highest", Integer.parseInt(cols.get(5).text()));
        json.put("matches", Integer.parseInt(cols.get(6).text()));
        if (cols.size() > 7) {
            JSONObject league = new JSONObject();
            league.put("qp", Integer.parseInt(cols.get(7).text()));
            league.put("rp", Integer.parseInt(cols.get(8).text()));
            league.put("matches", Integer.parseInt(cols.get(9).text()));
            json.put("league", league);
        }
        
        return json;
    }

    @Deprecated
    private class Ranking implements Jsonable{
        private final int rank;
        private final int teamNumber;
        private final int qp;
        private final int rp;
        private final int highest;
        private final int matches;
        private final boolean isLeague;
        private final int leagueQp;
        private final int leagueRp;
        private final int leagueMatches;

        public Ranking(Element row, boolean _isLeague) {
            isLeague = _isLeague;
            Elements cols = row.getElementsByTag("td");
            rank = Integer.parseInt(cols.get(0).text());
            teamNumber = Integer.parseInt(cols.get(1).text());
            qp = Integer.parseInt(cols.get(3).text());
            rp = Integer.parseInt(cols.get(4).text());
            highest = Integer.parseInt(cols.get(5).text());
            matches = Integer.parseInt(cols.get(6).text());
            if(isLeague) {
                leagueQp = Integer.parseInt(cols.get(7).text());
                leagueRp = Integer.parseInt(cols.get(8).text());
                leagueMatches = Integer.parseInt(cols.get(9).text());
            } else {
                leagueQp = 0;
                leagueRp = 0;
                leagueMatches = 0;
            }
        }
        
        @Override
        public JSONObject toJson() {
            JSONObject json = new JSONObject();
            json.put("rank", rank);
            json.put("team", teamNumber);
            json.put("qp", qp);
            json.put("rp", rp);
            json.put("highest", highest);
            json.put("matches", matches);
            if (isLeague) {
                JSONObject league = new JSONObject();
                league.put("qp", leagueQp);
                league.put("rp", leagueRp);
                league.put("matches", leagueMatches);
                json.put("league", league);
            }
            
            return json;
        }
        
        @Override
        public String toString() {
            return "" + rank + " (" + teamNumber + ")";
        }

        @Override
        public int hashCode(){
            return toJson().hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Ranking other = (Ranking) obj;
            return this.hashCode() == other.hashCode();
        }
    }
    
    //private ArrayList<Ranking> rankings;
    private final boolean isLeague;
    private JSONArray rankings;
    
    public RankingsFile(File _file) {
        super(_file);
        isLeague = App.app.config.getEventType() == EventType.LeagueTournament;
        rankings = parseHtmlFile();
    }

   
    private JSONArray parseHtmlFile() {
        JSONArray json = new JSONArray();
        Element table;
        try {
            table = this.getHtmlTable();
        } catch (Exception ex) {
            Logger.getLogger(TeamInfoFile.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        Elements rows = table.getElementsByTag("tr");
        
        int i = isLeague ? 2 : 1;
        
        if(rows.size() <= i)
            return null;
        
        for(; i< rows.size(); i++) {
            Element row = rows.get(i);
            try {
                JSONObject rank = parseRow(row);
                json.add(rank);
            } catch (Exception e) {
                
            }
        }
        
        return json;
    }

    @Override
    public JSONObject getServerData() {
        JSONObject json = new JSONObject();
        json.put("rankings", rankings);
        return json;
    }

    @Override
    public JSONObject onFileChange() {
        JSONArray array = parseHtmlFile();
        if ( array != null && !array.equals(rankings)) {
            rankings = array;
            return getServerData();
        } else {
            return null;
        }
    }
    
}
