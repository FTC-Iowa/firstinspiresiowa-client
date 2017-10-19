/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.firstinspiresiowa.client;

import java.io.File;
import static java.lang.Thread.sleep;
import java.nio.file.Path;
import java.util.ArrayList;
import javax.swing.JFileChooser;

/**
 *
 * @author vens
 */
public class Main {
    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {
        JsonReader reader = Json.createReader();
        
        
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fc.setDialogTitle("Score System Directory");
        int rv = fc.showDialog(null, "Select");
        
        Path scoreSystemPath;
        if (rv == JFileChooser.APPROVE_OPTION) {
            scoreSystemPath = fc.getSelectedFile().toPath();
        } else {
            System.err.println("Failed to open score system directory");
            return;
        }
        
        ReportsDirectory reportsDir = new ReportsDirectory(scoreSystemPath);
        //reportsDir.proccessEvents();
        
        
        File teamInfo = new File("/home/vens/Projects/firstinspiresiowa-client/TeamInfo_North_Super_Regional_Kindig.html");
        TeamInfoFile tif = new TeamInfoFile(teamInfo);
        
        String json = "{" + tif.buildJson() + "}";
        
        
        
        
        
        
        //Server server = new Server("https://firstinspiresiowa.firebaseapp.com");
        Server server = new Server("http://localhost:5000");
        server.Post(json, "/teams");
        
        sleep(10000);
        System.out.println("continue");
        ArrayList<JsonAble> teams = tif.onFileChange();
        json = "{" + JsonAble.buildJsonArray("teams", teams) + "}";
        
        server.Post(json, "/teams");
        
        if(true)return;
        /*File matchDetailsFile = new File("MatchResultsDetails_North_Super_Regional_Kindig.html");
        File teamInfoFile = new File("TeamInfo_North_Super_Regional_Kindig.html");
        File rankingsFile = new File("Rankings_North_Super_Regional_Kindig.html");
        
       // Element matchDetailsTable = http.getTable(matchDetailsFile);
        Element teamInfoTable = http.getTable(teamInfoFile);
        Element rankingsTable = http.getTable(rankingsFile);
        
        Elements matchDetailsRows = matchDetailsTable.getElementsByTag("tr");
        ArrayList<Match> matches = new ArrayList<Match>();
        for(int i = 2; i < matchDetailsRows.size(); i++) { // start at row 2 to get past the two headers
            Element row = matchDetailsRows.get(i);
            matches.add(new Match(row, i - 1, "kindig"));
        }
        
        
        
        
        String matchlist = "{\"matches\":[";
        for (Match m: matches) {
            matchlist += m.BuildJson() + ",";
        }
        matchlist = matchlist.substring(0,matchlist.length()-1);
        matchlist += "]}";
        server.Post(matchlist, "/matches");
        
        
        /*String teamlist = "{\"teams\":[";
        for (Team t: teams) {
            teamlist += t.BuildJson() + ",";
        }
        teamlist = teamlist.substring(0,teamlist.length()-1);
        teamlist += "]}";
        //System.out.println(teamlist);
        server.Post(teamlist, "/teams");*/
    
    }
    
    
    // HTTP GET request
	/*private void sendGet() throws Exception {

		String url = "https://firstinspiresiowa.firebaseapp.com/event/davenport";
                //String url = "http://localhost:5000/event/davenport";
		URL obj = new URL(url);
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
		//HttpURLConnection con = (HttpURLConnection) obj.openConnection();

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

	}*/

	// HTTP POST request
	/*private void sendPost(String data) throws Exception {

		String url = "https://firstinspiresiowa.firebaseapp.com/event/davenport";
                //String url = "http://localhost:5000/event/davenport";
		URL obj = new URL(url);
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
		//HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                
		//add reuqest header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
                con.setRequestProperty("Content-Type", "application/json");

                //String data = m.BuildJson();
                
		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(data);
		wr.flush();
		wr.close();

		int responseCode = con.getResponseCode();
		//System.out.println("\nSending 'POST' request to URL : " + url);
		//System.out.println("Post parameters : " + data);
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

	}*/
    
}
