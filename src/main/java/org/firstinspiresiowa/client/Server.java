/*
 * Copyright 2017 Jeramie Vens.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.firstinspiresiowa.client;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author vens
 */
public class Server {
    private class ServerWorker extends Thread {
        Server server;
        JSONArray fullData;    
        long interval;
        boolean paused;
        
        public ServerWorker(Server _server) {
            server = _server;
            fullData = new JSONArray();
            interval = 1000;
            paused = false;
        }
        
        public void AddJob(JSONObject data) {
            synchronized (this) {
                fullData.add(data);
            }
        }
        
        public void setPaused(boolean _paused) {
            synchronized (this) {
                paused = _paused;
                if(!paused) {
                    this.notify(); // notify that we are no longer paused 
                }
            }
        }
        
        @Override
        public void run() {
            try {
                while(true) {
                    Thread.sleep(interval);
                    synchronized (this) {
                        while (paused)
                            wait();
                    }
                    
                    JSONObject obj = new JSONObject();
                    
                    synchronized (this) {
                        if (fullData.size() > 0) {
                            obj.put("updates", fullData);
                            fullData = new JSONArray();
                        } else {
                            continue;
                        }
                    }
                    
                    obj.put("eventId", App.app.config.getEventId());
                    
                    try {
                        server.Post(obj, "/update");
                    } catch (IOException ex) {
                        Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            } catch (InterruptedException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        private void setInterval(long _interval) {
            interval = _interval;
        }
    }
    
    public static final String DEFAULT_SERVER = "http://localhost:5000";
    //public static final String DEFAULT_SERVER = "https://firstinspiresiowa.firebaseapp.com";
    private final String USER_AGENT = "Vens/5.0";

    private final String url;
    //private boolean use_ssl;
    
    ServerWorker workerThread;

    
    
    
    public Server(String _url){
        url = _url;
        workerThread = new ServerWorker(this);
    }
    
    public void startThread(long interval) {
        workerThread.setInterval(interval);
        workerThread.start();
    }
    
    public void pauseThread() {
        workerThread.setPaused(true);
    }
    
    public void resumeThread() {
        workerThread.setPaused(false);
    }
    
    private void Post(JSONObject data, String endpoint) throws IOException{
        System.out.println(data.toJSONString());
        
        URL obj = new URL(url + endpoint);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        
        //add reuqest header
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        con.setRequestProperty("Content-Type", "application/json");

        // Send post request
        con.setDoOutput(true);
        try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
            wr.writeBytes(data.toJSONString());
            wr.flush();
        }

        int responseCode = con.getResponseCode();
        //System.out.println("\nSending 'POST' request to URL : " + url);
        //System.out.println("Post parameters : " + data);
        System.out.println("Response Code : " + responseCode);

        StringBuilder response;
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()))) {
            String inputLine;
            response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
        }

        //print result
        System.out.println(response.toString());
    }
    
    public void QueuePost(JSONObject data) {
        workerThread.AddJob(data);
    }
}
