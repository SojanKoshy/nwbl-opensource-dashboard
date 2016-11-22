package com.huawei.nwbl.opensource.dashboard.service;

import com.huawei.nwbl.opensource.dashboard.domain.GerritAccountRepository;
import com.huawei.nwbl.opensource.dashboard.domain.GerritChange;
import com.huawei.nwbl.opensource.dashboard.domain.GerritChangeRepository;
import com.huawei.nwbl.opensource.dashboard.domain.GerritDump;
import com.huawei.nwbl.opensource.dashboard.domain.GerritDumpRepository;
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
import java.util.Iterator;
import java.util.List;

/**
 * Created by saravana on 21/11/16.
 */
@Service
public class GerritExtractService {

    @Autowired
    private GerritDumpRepository gerritDumpRepository;

    @Autowired
    private GerritAccountRepository gerritAccountRepository;

    @Autowired
    private GerritChangeRepository gerritChangeRepository;

    public String getAllData() {

        for (Long i = 10000L; i < 10005L; i++) {

            System.out.println("https://gerrit.onosproject.org/changes/" + i + "/detail");

            String dataDetails = getJsonData("https://gerrit.onosproject.org/changes/" + i + "/detail");

            //String dataFiles = getJsonData("https://gerrit.onosproject.org/changes/" + i);

            if (dataDetails != null) {

                String dataFiles = getJsonData("https://gerrit.onosproject.org/changes/?q=" + i + "&o=CURRENT_REVISION&o=CURRENT_COMMIT&o=CURRENT_FILES&o=DOWNLOAD_COMMANDS");

                GerritDump gerritDump = gerritDumpRepository.findOne(i);
                if (gerritDump == null) {
                    gerritDump = new GerritDump();
                }

                dataDetails = dataDetails.substring(7);
                dataDetails = dataDetails.substring(0, dataDetails.length() - 1);
                dataFiles = dataFiles.substring(8);
                dataFiles = dataFiles.substring(0, dataFiles.length() - 2);

                gerritDump.setId(i);
                gerritDump.setJsonDetails(dataDetails);
                gerritDump.setJsonFiles(dataFiles);

                gerritDumpRepository.save(gerritDump);

            }
        }

        return parseJson();
    }

    public String parseJson() {

        List<GerritDump> gerritDumps = gerritDumpRepository.findAll();
        JSONParser parser = new JSONParser();


        try {
            for (GerritDump gerritDump : gerritDumps) {
                JSONObject jObject = (JSONObject) parser.parse(gerritDump.getJsonDetails());
                JSONObject jsonFileObject = (JSONObject) parser.parse(gerritDump.getJsonFiles());

                Long id = (Long) jObject.get("_number");

                GerritChange gerritChange = gerritChangeRepository.findOne(id);
                if (gerritChange == null) {
                    gerritChange = new GerritChange();
                }


                Long addedSize = (Long) jObject.get("insertions");
                Long deletedSize = (Long) jObject.get("deletions");
                Long actualSize = addedSize - deletedSize;
                String branch = (String) jObject.get("branch");
                String status = (String) jObject.get("status");
                String project = (String) jObject.get("project");
                String subject = (String) jObject.get("subject");
                String updatedOn = (String) jObject.get("updated");
                Long gerritID = (Long) ((JSONObject) jObject.get("owner")).get("_account_id");

                gerritChange.setOwner((String) ((JSONObject) jObject.get("owner")).get("name"));

                gerritChange.setAccount(gerritAccountRepository.findOne(gerritID));


                gerritChange.setId(id);
                gerritChange.setAddedSize(addedSize.intValue());
                gerritChange.setDeletedSize(deletedSize.intValue());
                gerritChange.setActualSize(actualSize.intValue());
                gerritChange.setBranch(branch);
                gerritChange.setStatus(status);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd"); //"2016-11-10T11:20:33.794-0800"

                try {
                    gerritChange.setUpdatedOn(sdf.parse((updatedOn.split(" ")[0])));
                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }
                gerritChange.setProject(project);
                gerritChange.setSubject(subject);
                gerritChange.setLink("https://gerrit.onosproject.org/" + id);

                JSONObject revisions = (JSONObject) jsonFileObject.get("revisions");
                String currentRevisionId = (String) revisions.keySet().iterator().next();
                //JSONObject revisions = (JSONObject) jsonFileObject.keySet()
                System.out.println("commitedFilesPath >>>>>>" + currentRevisionId);
                JSONObject commitedfiles = (JSONObject) ((JSONObject)  ((JSONObject) jsonFileObject.get("revisions")).get(currentRevisionId)).get("files");
                String firstFile = (String) commitedfiles.keySet().iterator().next();

                gerritChange.setFirstFilePath(firstFile);

                Iterator<String> iter = commitedfiles.keySet().iterator();

                while (iter.hasNext()) {
                    if(iter.next().endsWith(".java")) {
                        gerritChange.setFirstFilePath(firstFile);
                        break;
                    }
                }

                gerritChangeRepository.save(gerritChange);

            }

        } catch (ParseException e) {
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
