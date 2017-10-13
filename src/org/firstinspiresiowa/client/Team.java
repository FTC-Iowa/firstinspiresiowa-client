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
public class Team {
    int number;
    String name;
    String school;
    String city;
    String state;
    String country;
    
    public Team(Element row){
        Elements cols = row.getElementsByTag("td");
        number = Integer.parseInt(cols.get(0).text());
        name = cols.get(1).text();
        school = cols.get(2).text();
        city = cols.get(3).text();
        state = cols.get(4).text();
        country = cols.get(5).text();
        
    }
    
    
    public String BuildJson() {
        String j = "{";
        j += "\"number\":\"" + number + "\",";
        j += "\"name\":\""+ name +"\",";
        j += "\"school\":\""+ school +"\",";
        j += "\"city\":" + city + ",";
        j += "\"state\":" + state + ",";
        j += "\"country\":" + country;
        j += "}";
        return j;
    }
}
