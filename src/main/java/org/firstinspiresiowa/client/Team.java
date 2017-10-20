/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.firstinspiresiowa.client;

import org.json.simple.JSONObject;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author vens
 */
public class Team implements Jsonable{
    private final int number;
    private final String name;
    private final String school;
    private final String city;
    private final String state;
    private final String country;
    private final int hash;
    
    public Team(Element row){
        Elements cols = row.getElementsByTag("td");
        number = Integer.parseInt(cols.get(0).text());
        name = cols.get(1).text();
        school = cols.get(2).text();
        city = cols.get(3).text();
        state = cols.get(4).text();
        country = cols.get(5).text();
        String tmp = name + school + city + state + country + number;
        hash = tmp.hashCode();
    }
    
    @Override
    public String toString() {
        return "" + number + " (" + name + ")";
    }
    
    @Override
    public int hashCode(){
        return hash;
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
        final Team other = (Team) obj;
        return this.hash == other.hash;
    }
    
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("number", number);
        json.put("teamname", name);
        json.put("school", school);
        json.put("city", city);
        json.put("state", state);
        json.put("country", country);
        return json;
    }
}
