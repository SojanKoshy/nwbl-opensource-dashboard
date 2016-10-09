package com.huawei.nwbl.opensource.dashboard.service;


import com.huawei.nwbl.opensource.dashboard.domain.GerritAccount;
import com.huawei.nwbl.opensource.dashboard.domain.GerritAccountRepository;
import org.apache.commons.io.FileUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;

/**
 * Queries Gerrit accounts and stores in local db
 */
@Service
public class GerritAccountScraperService extends GerritScraperService {

    private static final String ACCOUNT_QUERY_PATH = "/accounts/?q=";

    private static final Integer MAX_QUERY_SIZE = 100;
    private static final String DOWNLOAD_FILE = "/tmp/download.txt";
    private static final String JSON_FILE = "/tmp/download.json";

    @Autowired
    private GerritAccountRepository gerritAccountRepository;

    public void scrape() {
        for (char alphabet = 'a'; alphabet <= 'z'; alphabet++) {
            url = BASE_URL + ACCOUNT_QUERY_PATH + alphabet + "&n=" + MAX_QUERY_SIZE.toString();
            download();
            scrapeAccountDetails();
        }
    }

    private void download() {
        URL link;

        log.info("Downloading account info from link {}", url);
        try {
            link = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return;
        }
        try {
            FileUtils.copyURLToFile(link, new File(DOWNLOAD_FILE));

            BufferedReader reader = new BufferedReader(new FileReader(DOWNLOAD_FILE));
            BufferedWriter writer = new BufferedWriter(new FileWriter(JSON_FILE));

            String lineToRemove = ")]}'";
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                // trim newline when comparing with lineToRemove
                String trimmedLine = currentLine.trim();
                if (trimmedLine.equals(lineToRemove)) continue;
                writer.write(currentLine + System.getProperty("line.separator"));
            }
            writer.close();
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void scrapeAccountDetails() {
        JSONParser parser = new JSONParser();
        JSONArray jsonArray;
        try {
            jsonArray = (JSONArray) parser.parse(new FileReader(JSON_FILE));
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            return;
        }

        if (jsonArray.size() == MAX_QUERY_SIZE) {
            log.warn("Possible loss of account info as max size {} is reached ", MAX_QUERY_SIZE);
        } else {
            log.info("Number of accounts found are {}", jsonArray.size());
        }
        for (Object obj : jsonArray) {
            JSONObject jsonObject = (JSONObject) obj;

            Long id = (Long) jsonObject.get("_account_id");
            String name = (String) jsonObject.get("name");
            String email = (String) jsonObject.get("email");
            String username = (String) jsonObject.get("username");

            log.debug("_account_id : {}", id);
            log.debug("name : {}", name);
            log.debug("email : {}", email);
            log.debug("username : {}", username);

            GerritAccount gerritAccount = gerritAccountRepository.findOne(id);
            if (gerritAccount == null) {
                gerritAccount = new GerritAccount();
            }
            gerritAccount.setId(id);
            gerritAccount.setName(name);
            gerritAccount.setEmail(email);
            gerritAccount.setUsername(username);
            gerritAccount.setLastUpdatedOn(Calendar.getInstance());
            gerritAccountRepository.save(gerritAccount);
        }
    }

}

