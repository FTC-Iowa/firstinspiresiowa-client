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
public class Match {
    private class Aliance {
        public int team1;
        public int team2;
        public int team3 = 0;
        public int auto;
        public int auto_b;
        public int end_game;
        public int penalty;
        public int tele_op;
        public int total;
        
        public String BuildJson() {
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
        System.out.println(cols.size());
        number = _number;
        name = cols.get(0).text();
        event_ref = _event_ref;
        System.out.println(cols.get(5).text());
        red.auto = Integer.parseInt(cols.get(5).text());
        red.auto_b = Integer.parseInt(cols.get(6).text());
        red.end_game = Integer.parseInt(cols.get(8).text());
        red.penalty = Integer.parseInt(cols.get(9).text());
        red.team1 = 1111;
        red.team2 = 2222;
        red.tele_op = Integer.parseInt(cols.get(7).text());
        red.total = Integer.parseInt(cols.get(4).text());
        blue.auto = Integer.parseInt(cols.get(11).text());
        blue.auto_b = Integer.parseInt(cols.get(12).text());
        blue.end_game = Integer.parseInt(cols.get(14).text());
        blue.penalty = Integer.parseInt(cols.get(15).text());
        blue.team1 = 3333;
        blue.team2 = 4444;
        blue.tele_op = Integer.parseInt(cols.get(13).text());
        blue.total = Integer.parseInt(cols.get(10).text());
    }
    
    public String BuildJson() {
        String j = "{";
        j += "\"number\":\"" + number + "\",";
        j += "\"name\":\""+ name +"\",";
        j += "\"event\":\""+ event_ref +"\",";
        j += "\"red\":" + red.BuildJson() + ",";
        j += "\"blue\":" + blue.BuildJson();
        j += "}";
        return j;
    }
    
    
}
