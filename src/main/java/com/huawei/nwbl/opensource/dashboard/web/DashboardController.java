/*
 * Copyright 2012-2016 the original author or authors.
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

package com.huawei.nwbl.opensource.dashboard.web;

import com.huawei.nwbl.opensource.dashboard.domain.*;
import com.huawei.nwbl.opensource.dashboard.service.JiraExtractService;
import com.huawei.nwbl.opensource.dashboard.utils.ChartUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 */
@RestController
@RequestMapping("dashboard")
public class DashboardController {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private GerritChangeRepository gerritChangeRepository;

    @Autowired
    private GerritAccountRepository gerritAccountRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private JiraExtractService jiraExtractService;

    @GetMapping("jira_json")
    public String getJiraData() {
        return jiraExtractService.jiraExtract();
    }

    @GetMapping("company_json")
    public String getMembers() {

        List<Company> companies = companyRepository.getDistinctHasAccountsOrderByName();


        JSONObject companiesJson = new JSONObject();

        for (Company company : companies) {
            JSONArray members = new JSONArray();
            for (GerritAccount account : company.getGerritAccounts()) {
                JSONArray member = new JSONArray();
                member.add(account.getId());
                member.add(account.getName());
                members.add(member);
            }
            companiesJson.put(company.getId(), members);
        }

        return companiesJson.toJSONString();
    }
    @GetMapping("1/{startDate}/{endDate}/{projectsId}/{accountsId}")
    public String getCodeContributionProjectwise(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate,
            @PathVariable ArrayList<Long> projectsId,
            @PathVariable ArrayList<Long> accountsId) {

        Map<String, Double[]> projects = new HashMap<>();

        List<Object[]> gerritChangesMerged = gerritChangeRepository.getSumActualSizeGroupByProjectAndMerged(startDate, endDate,
                projectsId, accountsId);
        for (Object[] gerritChange : gerritChangesMerged) {
            String projectName = (String) gerritChange[0];
            Long codeSize = (Long) gerritChange[1];
            if (codeSize != null && codeSize / 1000.0 > 0) {
                Double[] status = {codeSize / 1000.0, 0.0};
                projects.put(projectName, status);
            }
        }

        List<Object[]> gerritChangesOpen = gerritChangeRepository.getSumActualSizeGroupByProjectAndOpen(startDate, endDate,
                projectsId, accountsId);
        for (Object[] gerritChange : gerritChangesOpen) {
            String projectName = (String) gerritChange[0];
            Long codeSize = (Long) gerritChange[1];
            if (codeSize != null && codeSize / 1000.0 > 0) {
                if (projects.containsKey(projectName)) {
                    Double[] status = projects.get(projectName);
                    status[1] = codeSize / 1000.0;
                } else {
                    Double[] status = {0.0, codeSize / 1000.0};
                    projects.put(projectName, status);
                }
            }
        }

        JSONObject data = new JSONObject();
        JSONArray categories = new JSONArray();
        JSONArray series = new JSONArray();
        JSONObject series1 = new JSONObject();
        JSONArray series1data = new JSONArray();
        JSONObject series2 = new JSONObject();
        JSONArray series2data = new JSONArray();

        Map<Double, String> projectsSorted = new TreeMap<>(Collections.reverseOrder());
        projects.forEach((k, v) -> projectsSorted.put(v[0] + v[1], k));
        projectsSorted.values().forEach((k) -> {
            categories.add(k);
            Double[] v = projects.get(k);
            series1data.add(v[0]);
            series2data.add(v[1]);
        });

        series2.put("name", "Open");
        series2.put("data", series2data);
        series.add(series2);
        series1.put("name", "Merged");
        series1.put("data", series1data);
        series.add(series1);
        data.put("series", series);
        data.put("categories", categories);
        return data.toJSONString();
    }

    @GetMapping("2/{startDate}/{endDate}/{projectsId}/{accountsId}")
    public String getCodeContributionMemberwise(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate,
            @PathVariable ArrayList<Long> projectsId,
            @PathVariable ArrayList<Long> accountsId) {
        Map<String, Double[]> members = new HashMap<>();

        List<Object[]> gerritChangesMerged = gerritChangeRepository.getSumActualSizeGroupByMemberAndMerged(startDate, endDate,
                projectsId, accountsId);
        for (Object[] gerritChange : gerritChangesMerged) {
            String memberName = (String) gerritChange[0];
            Long codeSize = (Long) gerritChange[1];
            if (codeSize != null && codeSize / 1000.0 > 0) {
                Double[] status = {codeSize / 1000.0, 0.0};
                members.put(memberName, status);
            }
        }

        List<Object[]> gerritChangesOpen = gerritChangeRepository.getSumActualSizeGroupByMemberAndOpen(startDate, endDate,
                projectsId, accountsId);
        for (Object[] gerritChange : gerritChangesOpen) {
            String memberName = (String) gerritChange[0];
            Long codeSize = (Long) gerritChange[1];
            if (codeSize != null && codeSize / 1000.0 > 0) {
                if (members.containsKey(memberName)) {
                    Double[] status = members.get(memberName);
                    status[1] = codeSize / 1000.0;
                } else {
                    Double[] status = {0.0, codeSize / 1000.0};
                    members.put(memberName, status);
                }
            }
        }

        JSONObject data = new JSONObject();
        JSONArray categories = new JSONArray();
        JSONArray series = new JSONArray();
        JSONObject series1 = new JSONObject();
        JSONArray series1data = new JSONArray();
        JSONObject series2 = new JSONObject();
        JSONArray series2data = new JSONArray();

        Map<Double, String> membersSorted = new TreeMap<>(Collections.reverseOrder());
        members.forEach((k, v) -> membersSorted.put(v[0] + v[1], k));
        membersSorted.values().forEach((k) -> {
            categories.add(k);
            Double[] v = members.get(k);
            series1data.add(v[0]);
            series2data.add(v[1]);
        });

        series2.put("name", "Open");
        series2.put("data", series2data);
        series.add(series2);
        series1.put("name", "Merged");
        series1.put("data", series1data);
        series.add(series1);
        data.put("series", series);
        data.put("categories", categories);
        return data.toJSONString();
    }

    @GetMapping("3/{startDate}/{endDate}/{projectsId}/{accountsId}")
    public String getOverallCodeStatus(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate,
            @PathVariable ArrayList<Long> projectsId,
            @PathVariable ArrayList<Long> accountsId) {


        JSONObject data = new JSONObject();
        JSONArray seriesdata = new JSONArray();
        List<Object[]> objectsList = gerritChangeRepository.getSumActualSizeGroupByCompany(startDate, endDate,
                projectsId, accountsId);
        for (Object[] objects : objectsList) {
            JSONObject seriesdata1 = new JSONObject();
            String companyName = (String) objects[0];
            Long codeSize = (Long) objects[1];
            seriesdata1.put("name", companyName);
            seriesdata1.put("y", codeSize / 1000.0);
            seriesdata.add(seriesdata1);
        }
        data.put("data", seriesdata);
        return data.toJSONString();
    }

    @GetMapping("4/{startDate}/{endDate}/{projectsId}/{accountsId}")
    public String getCodeCommittedTimeline(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate,
            @PathVariable ArrayList<Long> projectsId,
            @PathVariable ArrayList<Long> accountsId) {
        JSONArray data = new JSONArray();
        ChartUtils chartUtils = new ChartUtils();

        List<Object[]> gerritChanges = gerritChangeRepository.getSumActualSizeGroupByUpdatedOn(startDate, endDate,
                projectsId, accountsId);
        for (Object[] gerritChange : gerritChanges) {
            java.sql.Date date = new java.sql.Date(((Date) gerritChange[0]).getTime());
            Long codeSize = (Long) gerritChange[1];
            JSONArray row = new JSONArray();
            row.add(date.getTime());
            row.add(codeSize / 1000.0);
            data.add(row);
        }
        return data.toJSONString();
    }
}
