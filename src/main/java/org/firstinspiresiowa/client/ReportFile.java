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

import java.io.File;
import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author vens
 */
public abstract class ReportFile {
    protected File file;
    
    public ReportFile(File _file){
        file = _file;
    }
    
    protected Element getHtmlTable() throws Exception{
        Document doc = Jsoup.parse(file, "UTF-8", "");
        Elements tables = doc.getElementsByTag("table");
        if (tables.isEmpty()) {
            System.err.println("Could not find table in HTML document");
            throw new Exception();
        }
        return tables.first();
    }
    
    public abstract JSONObject onFileChange();
    
    public abstract JSONObject getServerData();
    
    // report Files:
    //     MatchResultsDetails, Matches, Rankings, TeamInfo
}
