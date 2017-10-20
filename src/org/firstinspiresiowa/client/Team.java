/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.firstinspiresiowa.client;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author vens
 */
public class Team implements JsonAble{
    private int number;
    private String name;
    private String school;
    private String city;
    private String state;
    private String country;
    private int hash;
    
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
    
    public String toString() {
        return "" + number + " (" + name + ")";
    }
    
    public int hashCode(){
        return hash;
    }
    
    public boolean equals(Team a) {
            if (hashCode() == a.hashCode()) {
                return true;      
            } else {
                return false;
            }
        }
    
    @Override
    public String BuildJson() 
    {
        String j = "{";
        j += "\"number\":\"" + number + "\",";
        j += "\"teamname\":\""+ name +"\",";
        j += "\"school\":\""+ school +"\",";
        j += "\"city\":\"" + city + "\",";
        j += "\"state\":\"" + state + "\",";
        j += "\"country\":\"" + country + "\"";
        j += "}";
        return j;
    }
}
