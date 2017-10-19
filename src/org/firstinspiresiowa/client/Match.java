/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.firstinspiresiowa.client;

import org.jsoup.nodes.*;
import org.jsoup.select.*;

/**
 *
 * @author vens
 */
public class Match extends JsonAble {
    private class Aliance extends JsonAble{
        public int team1;
        public int team2;
        public int team3 = 0;
        public int auto;
        public int auto_b;
        public int end_game;
        public int penalty;
        public int tele_op;
        public int total;
        
        public boolean equals(Aliance a) {
            if ( team1 == a.team1 &&
                    team2 == a.team2 &&
                    team3 == a.team3 &&
                    auto == a.auto &&
                    auto_b == a.auto_b &&
                    end_game == a.end_game &&
                    penalty == a.penalty &&
                    tele_op == a.tele_op &&
                    total == a.total) {
                return true;      
            }
            return false;
        }
        
        @Override
        public String buildJson() {
            String j = "{";
            j += "\"teams\": [\"" + team1 + "\",\"" + team2 + (team3>0 ? "\",\"" + team3 : "") +"\"],";
            j += "\"auto\":\"" + auto + "\",";
            j += "\"auto_b\":\"" + auto_b + "\",";
            j += "\"end_game\":\"" + end_game + "\",";
            j += "\"penalty\":\"" + penalty + "\",";
            j += "\"tele_op\":\"" + tele_op + "\",";
            j += "\"total\":\"" + total + "\"";
            j += "}";
            return j;
        }
    }
    private int number;
    private String name;
    private String event_ref;
    private Aliance red = new Aliance();
    private Aliance blue = new Aliance();
    
    //<TR ALIGN=CENTER><TD BGCOLOR="#FFFFFF">Q-1</TD><TD BGCOLOR="#FF4444">140-0 R</TD><TD BGCOLOR="#FFFFFF"></TD><TD BGCOLOR="#FFFFFF"></TD><TD BGCOLOR="#FFFFFF">140</TD><TD BGCOLOR="#FFFFFF">90</TD><TD BGCOLOR="#FFFFFF">0</TD><TD BGCOLOR="#FFFFFF">30</TD><TD BGCOLOR="#FFFFFF">20</TD><TD BGCOLOR="#FFFFFF">0</TD><TD BGCOLOR="#FFFFFF">0</TD><TD BGCOLOR="#FFFFFF">0</TD><TD BGCOLOR="#FFFFFF">0</TD><TD BGCOLOR="#FFFFFF">0</TD><TD BGCOLOR="#FFFFFF">0</TD><TD BGCOLOR="#FFFFFF">0</TD></TR>
    
    public Match(Element row, int _number, String _event_ref){
        
        Elements cols = row.getElementsByTag("td");
        //System.out.println(cols.size());
        number = _number;
        
        String[] red_teams = cols.get(2).text().split("\\s");
        String[] blue_teams = cols.get(3).text().split("\\s");
        red.team1 = Integer.parseInt(red_teams[0]);
        red.team2 = Integer.parseInt(red_teams[1]);
        if (red_teams.length == 3)
            red.team3 = Integer.parseInt(red_teams[2]);
        blue.team1 = Integer.parseInt(blue_teams[0]);
        blue.team2 = Integer.parseInt(blue_teams[1]);
        if (blue_teams.length == 3)
            red.team3 = Integer.parseInt(blue_teams[2]);
        
        name = cols.get(0).text();
        event_ref = _event_ref;
        System.out.println(cols.get(5).text());
        red.auto = Integer.parseInt(cols.get(5).text());
        red.auto_b = Integer.parseInt(cols.get(6).text());
        red.end_game = Integer.parseInt(cols.get(8).text());
        red.penalty = Integer.parseInt(cols.get(9).text());
        red.tele_op = Integer.parseInt(cols.get(7).text());
        red.total = Integer.parseInt(cols.get(4).text());
        blue.auto = Integer.parseInt(cols.get(11).text());
        blue.auto_b = Integer.parseInt(cols.get(12).text());
        blue.end_game = Integer.parseInt(cols.get(14).text());
        blue.penalty = Integer.parseInt(cols.get(15).text());
        blue.tele_op = Integer.parseInt(cols.get(13).text());
        blue.total = Integer.parseInt(cols.get(10).text());
    }
    
    public boolean equals(Match m) {
        if (number == m.number &&
                name.compareTo(m.name) == 0 &&
                event_ref.compareTo(m.event_ref) == 0 &&
                red.equals(m.red) &&
                blue.equals(m.blue)) {
            return true;
        }
        return false;
    }
    
    @Override
    public String buildJson() {
        String j = "{";
        j += "\"number\":\"" + number + "\",";
        j += "\"name\":\""+ name +"\",";
        j += "\"event\":\""+ event_ref +"\",";
        j += red.buildJson("red") + ",";
        j += blue.buildJson("blue");
        j += "}";
        return j;
    }
    
    
}
