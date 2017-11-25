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
class RankingsFile extends ReportFile {

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
    
    private ArrayList<Ranking> rankings;
    private final boolean isLeague;
    
    public RankingsFile(File _file) {
        super(_file);
        isLeague = App.app.config.getEventType() == EventType.LeagueTournament;
        rankings = parseHtmlFile();
    }

   
    private ArrayList<Ranking> parseHtmlFile() {
        ArrayList<Ranking> fileRankings = new ArrayList<>();
        Element table;
        try {
            table = this.getHtmlTable();
        } catch (Exception ex) {
            Logger.getLogger(TeamInfoFile.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        Elements rows = table.getElementsByTag("tr");
        
        int i = isLeague ? 2 : 1;
        
        for(; i< rows.size(); i++) {
            Element row = rows.get(i);
            try {
                Ranking m = new Ranking(row, isLeague);
                fileRankings.add(m);
            } catch (Exception e) {
                
            }
            //System.out.println("Found match: " + m);
        }
        
        return fileRankings;
    }

    @Override
    public JSONObject getServerData() {
        if(rankings.size()>0){
            JSONArray array = new JSONArray();
            rankings.forEach((i) -> {
                array.add(i.toJson());
            });
            JSONObject json = new JSONObject();
            json.put("rankings", array);
            return json;
        } else {
            return null;
        }
    }

    @Override
    public JSONObject onFileChange() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return null;
    }
    
}
