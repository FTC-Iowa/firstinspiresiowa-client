/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.firstinspiresiowa.client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.URL;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

/**
 *
 * @author vens
 */
public class Main {
    private final String USER_AGENT = "Mozilla/5.0";


    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        Main http = new Main();
        
        File input = new File("/home/vens/Projects/ftc-scoring-1-0/reports/MatchResultsDetails_meet_name.html");
        Document doc = Jsoup.parse(input, "UTF-8", "");
        Elements tables = doc.getElementsByTag("table");
        
        if(tables.isEmpty()) {
            System.err.println("Could not find table in HTML document");
            return;
        }
        
        Element table = tables.first();
        Elements rows = table.getElementsByTag("tr");
        System.out.println(rows.size());
        Element row = rows.get(2);
        Match m = new Match(row, 1, "davenport");
        
        
        http.sendGet();
        
        http.sendPost(m);
    }
    
    
    // HTTP GET request
	private void sendGet() throws Exception {

		//String url = "https://firstinspiresiowa.firebaseapp.com/event/davenport";
                String url = "http://localhost:5000/event/davenport";
		URL obj = new URL(url);
		//HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		//add request header
		con.setRequestProperty("User-Agent", USER_AGENT);

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		//print result
		System.out.println(response.toString());

	}

	// HTTP POST request
	private void sendPost(Match m) throws Exception {

		//String url = "https://firstinspiresiowa.firebaseapp.com/event/davenport";
                String url = "http://localhost:5000/event/davenport";
		URL obj = new URL(url);
		//HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                
		//add reuqest header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
                con.setRequestProperty("Content-Type", "application/json");

                //Match m = new Match("");
                
		//String urlParameters = "sn=C02G8416DRJM&cn=&locale=&caller=&num=12345";

                //String data = "{\"name\":\"jeramie\",\"num\":\"2\"}";
                String data = m.BuildJson();
                
		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(data);
		wr.flush();
		wr.close();

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'POST' request to URL : " + url);
		System.out.println("Post parameters : " + data);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		//print result
		System.out.println(response.toString());

	}
    
}
