/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.firstinspiresiowa.client;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author vens
 */
public class App {
    public static App app;
    public final Config config;
    public final Server server;
    public final DirWatcher fileWatcher;
    public final RootDirectory rootDirectory;
    
    private App() throws FileNotFoundException, IOException {
        app = this;
        try {
            config = new Config(); // get or create the config file
        } catch (FileNotFoundException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
        
        // create the server
        server = new Server(config.getServer());
        
        // open the reports directory to watch for changes
        //fileWatcher = new FileWatcher(config.getRootDir());
        fileWatcher = new DirWatcher();
        
        // create root directory object
        rootDirectory = new RootDirectory(config.getRootDir());
        
        fileWatcher.register(rootDirectory);
    }

    private void run() {
        fileWatcher.proccessEvents();
        
        while (true) {
            
        }
    }
    
    
    
    
    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {
        App a = new App();
        app.run();
        
        if(true)return;
        
    //    ReportsDirectory reportsDir = new ReportsDirectory(scoreSystemPath);
        //reportsDir.proccessEvents();
        
        
        File teamInfo = new File("/home/vens/Projects/firstinspiresiowa-client/TeamInfo_North_Super_Regional_Kindig.html");
        TeamInfoFile tif = new TeamInfoFile(teamInfo);
        
        ArrayList<Team> teamList = tif.getTeamList();
        
        JSONArray teamArray = new JSONArray();
        teamList.forEach((t) -> {
            teamArray.add(t.toJson());
        });
        
        JSONObject json = new JSONObject();
        json.put("teams", teamArray);
        
        
        
        
        
        
        //Server server = new Server("https://firstinspiresiowa.firebaseapp.com");
        Server server = new Server("http://localhost:5000");
        server.Post(json, "/teams");
        
        sleep(10000);
        System.out.println("continue");
    //    ArrayList<Jsonable> teams = tif.onFileChange();
    //    JSONArray changedTeams = new JSONArray();
    //    teams.forEach((t) -> {
    //        changedTeams.add(t.toJson());
    //    });
        
     //   JSONObject json2 = new JSONObject();
    //    json2.put("teams", changedTeams);
        
    //    server.Post(json2, "/teams");
        
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
}
