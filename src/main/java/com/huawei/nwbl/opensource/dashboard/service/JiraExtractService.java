package com.huawei.nwbl.opensource.dashboard.service;

import com.huawei.nwbl.opensource.dashboard.domain.JiraDump;
import com.huawei.nwbl.opensource.dashboard.domain.JiraDumpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by saravana on 18/11/16.
 */
@Service
public class JiraExtractService {
    @Autowired
    private JiraDumpRepository jiraDumpRepository;

    public String jiraExtract() {

        for (int i = 10000; i < 10005; i++) {

            System.out.println("https://jira.onosproject.org/rest/api/2/issue/"+i);

            String data = get("https://jira.onosproject.org/rest/api/2/issue/"+i);
            JiraDump jiraDump = new JiraDump();
            jiraDump.setId(i);
            jiraDump.setJson(data);

            jiraDumpRepository.save(jiraDump);
        }

        return "Done";
    }

    public String get(String urlString) {

        try {

            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String output;
            List<String> allOutput = new ArrayList<>();
           // System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                //System.out.println(output);
                allOutput.add(output);
            }

            conn.disconnect();
            return allOutput.toString();

        } catch (IOException e) {

            e.printStackTrace();

        }

        return "";
    }

}
