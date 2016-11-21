package com.huawei.nwbl.opensource.dashboard.service;

import com.huawei.nwbl.opensource.dashboard.domain.*;
import com.huawei.nwbl.opensource.dashboard.domain.GerritAccount;
import com.huawei.nwbl.opensource.dashboard.domain.GerritAccountRepository;
import com.huawei.nwbl.opensource.dashboard.domain.GerritDumpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.apache.el.util.MessageFactory.get;

/**
 * Created by saravana on 21/11/16.
 */
@Service
public class GerritExtractService {

    @Autowired
    private GerritDumpRepository gerritDumpRepository;

    @Autowired
    private GerritAccountRepository gerritAccountRepository;

    public String getAllData() {

        for (Long i = 10000L; i < 10005L; i++) {

            System.out.println("https://gerrit.onosproject.org/changes/" + i + "/detail");

            String dataDetails = getJsonData("https://gerrit.onosproject.org/changes/" + i + "/detail");

            //String dataFiles = getJsonData("https://gerrit.onosproject.org/changes/" + i);

            if (dataDetails != null) {

                String dataFiles = getJsonData("https://gerrit.onosproject.org/changes/?q=/" + i + "/&o=CURRENT_REVISION&o=CURRENT_COMMIT&o=CURRENT_FILES&o=DOWNLOAD_COMMANDS");

                GerritDump gerritDump = gerritDumpRepository.findOne(i);
                if (gerritDump == null) {
                    gerritDump = new GerritDump();
                }
                gerritDump.setId(i);
                gerritDump.setJsonDetails(dataDetails);
                gerritDump.setJsonFiles(dataFiles);

                gerritDumpRepository.save(gerritDump);

            }
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
