/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.firstinspiresiowa.client;

import org.json.simple.JSONObject;

/**
 *
 * @author vens
 */
public interface Jsonable {
    public JSONObject toJson();

    //public abstract String buildJson();
    
    //public String buildJson(String name) {
    //    return "\"" + name + "\":" + buildJson();
    //}
    
//    public static String buildJsonArray(String name, ArrayList<? extends JsonAble> list){
//        String json = "\"" + name + "\": [";
//        boolean first = true;
//        for (JsonAble j: list) {
//            if(!first)
//                json += ",";
//            else
//                first = false;
//            json += j.buildJson();
//        }
//        
//        json += "]";
//        return json;
//    }
}