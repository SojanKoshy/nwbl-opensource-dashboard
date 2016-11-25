package com.huawei.nwbl.opensource.dashboard.service;

import com.huawei.nwbl.opensource.dashboard.domain.GerritAccount;
import com.huawei.nwbl.opensource.dashboard.domain.GerritAccountRepository;
import com.huawei.nwbl.opensource.dashboard.domain.GerritChange;
import com.huawei.nwbl.opensource.dashboard.domain.GerritChangeRepository;
import com.huawei.nwbl.opensource.dashboard.domain.GerritDump;
import com.huawei.nwbl.opensource.dashboard.domain.GerritDumpRepository;
import com.huawei.nwbl.opensource.dashboard.domain.GerritReview;
import com.huawei.nwbl.opensource.dashboard.domain.GerritReviewRepository;
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
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    @Autowired
    private GerritReviewRepository gerritReviewRepository;


    public String getAllAccounts() {

        for (Long i = 1000001L; i < 1000520L; i++) {
            System.out.println("https://gerrit.onosproject.org/accounts/" + i);
            String account = getJsonData("https://gerrit.onosproject.org/accounts/" + i);
            if (account == null) {
                continue;
            }

            account = account.substring(6);
            account = account.substring(0, account.length() - 1);
            JSONParser parser = new JSONParser();
            try {
                JSONObject jObject = (JSONObject) parser.parse(account);
                Long id = (Long) jObject.get("_account_id");
                String name = (String) jObject.get("name");
                String email = (String) jObject.get("email");
                String username = (String) jObject.get("username");
                GerritAccount gerritAccount = gerritAccountRepository.findOne(id);
                if (gerritAccount == null) {
                    gerritAccount = new GerritAccount();
                }
                gerritAccount.setId(id);
                gerritAccount.setName(name);
                gerritAccount.setEmail(email);
                gerritAccount.setUsername(username);
                gerritAccountRepository.save(gerritAccount);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return "done";
    }
    public String getAllData() {

        for (Long i = 11000L; i < 12000L; i++) {

            System.out.println("https://gerrit.onosproject.org/changes/" + i + "/detail");

            String dataDetails = getJsonData("https://gerrit.onosproject.org/changes/" + i + "/detail");

            if (dataDetails != null) {

                String dataFiles = getJsonData("https://gerrit.onosproject.org/changes/?q=" + i +
                        "&o=CURRENT_REVISION&o=CURRENT_COMMIT&o=CURRENT_FILES&o=DOWNLOAD_COMMANDS");

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

                //FIXME: If account not found add new account
                gerritChange.setAccount(gerritAccountRepository.findOne(gerritID));

                gerritChange.setId(id);
                gerritChange.setAddedSize(addedSize.intValue());
                gerritChange.setDeletedSize(deletedSize.intValue());
                gerritChange.setActualSize(actualSize.intValue());
                gerritChange.setBranch(branch);
                gerritChange.setStatus(status);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); //"2016-11-10T11:20:33.794-0800"

                try {
                    gerritChange.setUpdatedOn(new java.sql.Date(sdf.parse((updatedOn.split(" ")[0])).getTime()));
                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }
                gerritChange.setProject(project);
                gerritChange.setSubject(subject);
                gerritChange.setLink("https://gerrit.onosproject.org/" + id);
                String firstFile;
                JSONObject commitedfiles;

                JSONObject revisions = (JSONObject) jsonFileObject.get("revisions");
                if (revisions.isEmpty()) {
                    firstFile = "";
                    gerritChange.setFirstFilePath(firstFile);

                } else {
                    String currentRevisionId = (String) revisions.keySet().iterator().next();
                    commitedfiles = (JSONObject) ((JSONObject) revisions.get(currentRevisionId)).get("files");
                    if (commitedfiles.isEmpty()) {
                        firstFile = "";
                        gerritChange.setFirstFilePath(firstFile);

                    } else firstFile = (String) commitedfiles.keySet().iterator().next();

                    gerritChange.setFirstFilePath(firstFile);

                    for (Object file : commitedfiles.keySet()) {
                        if (((String) file).endsWith(".java")) {
                            gerritChange.setFirstFilePath(firstFile);
                            break;
                        }
                    }
                }
                gerritChangeRepository.save(gerritChange);

            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "Done";
    }


    public String parseReviewComments() {

        List<GerritDump> gerritDumps = gerritDumpRepository.findAll();
        JSONParser parser = new JSONParser();

        for (GerritDump gerritDump : gerritDumps) {
            JSONObject jObject = null;
            try {
                jObject = (JSONObject) parser.parse(gerritDump.getJsonDetails());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Long changeId = (Long) jObject.get("_number");
            JSONArray messages = (JSONArray) jObject.get("messages");
            Iterator itr = messages.iterator();
            while (itr.hasNext()) {
                JSONObject message = (JSONObject) itr.next();

                String commentCountMessage = (String) message.get("message");
                Pattern pattern = Pattern.compile("\\((\\d+) comments*\\)");
                Matcher matcher = pattern.matcher(commentCountMessage);
                if (matcher.find()) {
                    Integer count = Integer.valueOf(matcher.group(1));
                    String reviewId = (String) message.get("id");
                    Long accountId = (Long) ((JSONObject) message.get("author")).get("_account_id");
                    GerritAccount account = gerritAccountRepository.findOne(accountId);
                    if (!account.isInUse()) {
                        account.setInUse(true);
                        gerritAccountRepository.save(account);
                    }
                    GerritReview gerritReview = gerritReviewRepository.findByReviewId(reviewId);
                    GerritChange gerritChange = gerritChangeRepository.findOne(changeId);
                    if (gerritReview == null) {
                        gerritReview = new GerritReview();
                    }
                    gerritReview.setCommentCount(count);
                    gerritReview.setGerritAccount(account);
                    gerritReview.setGerritChange(gerritChange);
                    gerritReview.setReviewId(reviewId);
                    gerritReviewRepository.save(gerritReview);
                }

            }
        }
        return "Ok";
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
