package com.huawei.nwbl.opensource.dashboard.service;

import com.huawei.nwbl.opensource.dashboard.domain.JiraAccount;
import com.huawei.nwbl.opensource.dashboard.domain.JiraAccountRepository;
import com.huawei.nwbl.opensource.dashboard.domain.JiraDump;
import com.huawei.nwbl.opensource.dashboard.domain.JiraDumpRepository;
import com.huawei.nwbl.opensource.dashboard.domain.JiraTicket;
import com.huawei.nwbl.opensource.dashboard.domain.JiraTicketRepository;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by saravana on 18/11/16.
 */
@Service
public class JiraExtractService {
    @Autowired
    private JiraDumpRepository jiraDumpRepository;

    @Autowired
    private JiraTicketRepository jiraTicketRepository;

    @Autowired
    private JiraAccountRepository jiraAccountRepository;

    public String getAllData() {
        for (Long i = 10000L; i < 18000L; i++) {

            System.out.println("https://jira.onosproject.org/rest/api/2/issue/" + i);

            String data = getJsonData("https://jira.onosproject.org/rest/api/2/issue/" + i);
            if (data != null) {

                JiraDump jiraDump = jiraDumpRepository.findOne(i);
                if (jiraDump == null) {
                    jiraDump = new JiraDump();
                }
                jiraDump.setId(i);
                jiraDump.setJson(data);

                jiraDumpRepository.save(jiraDump);

            }
        }

        return parseJson();
    }

    public String parseJson() {

        List<JiraDump> jiraDumps = jiraDumpRepository.findAll();
        JSONParser parser = new JSONParser();

        try {
            for (JiraDump jiraDump : jiraDumps) {
                JSONArray jsonArray = (JSONArray) parser.parse(jiraDump.getJson());
                JSONObject jObject = (JSONObject) jsonArray.get(0);
                Long id = Long.valueOf((String) jObject.get("id"));

                JiraTicket jiraTicket = jiraTicketRepository.findOne(id);
                if (jiraTicket == null) {
                    jiraTicket = new JiraTicket();
                }
                jiraTicket.setId(id);
                jiraTicket.setName((String) jObject.get("key"));
                jObject = (JSONObject) jObject.get("fields");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); //"2016-11-10T11:20:33.794-0800"
                jiraTicket.setCreatedOn(sdf.parse(((String) jObject.get("created")).split("T")[0]));
                jiraTicket.setUpdatedOn(sdf.parse(((String) jObject.get("updated")).split("T")[0]));

                JiraAccount account = jiraAccountRepository.findByEmail(
                        (String) ((JSONObject) jObject.get("creator")).get("emailAddress"));
                if (account == null) {
                    account = new JiraAccount();
                    account.setEmail((String) ((JSONObject) jObject.get("creator")).get("emailAddress"));
                    account.setName((String) ((JSONObject) jObject.get("creator")).get("name"));
                    jiraAccountRepository.save(account);
                }
                jiraTicket.setCreator(account);

                jiraTicket.setAssignee(null);

                if (jObject.get("assignee") != null) {

                    account = jiraAccountRepository.findByEmail(
                            (String) ((JSONObject) jObject.get("assignee")).get("emailAddress"));
                    if (account == null) {
                        account = new JiraAccount();
                        account.setEmail((String) ((JSONObject) jObject.get("assignee")).get("emailAddress"));
                        account.setName((String) ((JSONObject) jObject.get("assignee")).get("name"));
                        jiraAccountRepository.save(account);
                    }

                    jiraTicket.setAssignee(account);
                }

                jiraTicket.setReporter(null);

                if (jObject.get("reporter") != null) {

                    account = jiraAccountRepository.findByEmail(
                            (String) ((JSONObject) jObject.get("reporter")).get("emailAddress"));
                    if (account == null) {
                        account = new JiraAccount();
                        account.setEmail((String) ((JSONObject) jObject.get("reporter")).get("emailAddress"));
                        account.setName((String) ((JSONObject) jObject.get("reporter")).get("name"));
                        jiraAccountRepository.save(account);
                    }
                    jiraTicket.setReporter(account);
                }

                jiraTicket.setSummary((String) jObject.get("summary"));
                jiraTicket.setSeverity((String) ((JSONObject) jObject.get("priority")).get("name"));
                jiraTicket.setStatus((String) ((JSONObject) jObject.get("status")).get("name"));
                jiraTicket.setProject((String) ((JSONObject) jObject.get("project")).get("name"));
                jiraTicket.setType((String) ((JSONObject) jObject.get("issuetype")).get("name"));
                jiraTicket.setResolution(null);
                if (jObject.get("resolution") != null) {
                    jiraTicket.setResolution((String) ((JSONObject) jObject.get("resolution")).get("name"));
                }
                jiraTicketRepository.save(jiraTicket);
            }

        } catch (ParseException | java.text.ParseException e) {
            e.printStackTrace();
        }

        return "Done";
    }

    public String getJsonData(String urlString) {

        try {

            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                return null;
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String output;
            List<String> allOutput = new ArrayList<>();
            while ((output = br.readLine()) != null) {
                allOutput.add(output);
            }

            conn.disconnect();
            return allOutput.toString();

        } catch (IOException e) {

            e.printStackTrace();

        }

        return null;
    }

}
