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

import com.huawei.nwbl.opensource.dashboard.domain.GerritAccountRepository;
import com.huawei.nwbl.opensource.dashboard.domain.GerritChangeRepository;
import com.huawei.nwbl.opensource.dashboard.domain.MemberRepository;
import com.huawei.nwbl.opensource.dashboard.domain.ProjectRepository;
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
    private GerritChangeRepository gerritChangeRepository;

    @Autowired
    private GerritAccountRepository gerritAccountRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @GetMapping
    public ModelAndView dashboard() {
        return new ModelAndView("dashboard", "projects", projectRepository.getAllByIsVisibleOrderByName());
    }

    @GetMapping("1/{startDate}/{endDate}/{projectsId}")
    public String getCodeContributionProjectwise(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate,
            @PathVariable ArrayList<Long> projectsId) {

        Map<String, Double[]> projects = new HashMap<>();

        List<Object[]> gerritChangesMerged = gerritChangeRepository.getSumActualSizeGroupByProjectAndMerged(startDate, endDate,
                projectsId);
        for (Object[] gerritChange : gerritChangesMerged) {
            String projectName = (String) gerritChange[0];
            Long codeSize = (Long) gerritChange[1];
            if (codeSize != null && codeSize / 1000.0 > 0) {
                Double[] status = {codeSize / 1000.0, 0.0};
                projects.put(projectName, status);
            }
        }

        List<Object[]> gerritChangesOpen = gerritChangeRepository.getSumActualSizeGroupByProjectAndOpen(startDate, endDate,
                projectsId);
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

    @GetMapping("2/{startDate}/{endDate}/{projectsId}")
    public String getCodeContributionMemberwise(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate,
            @PathVariable ArrayList<Long> projectsId) {
        Map<String, Double[]> members = new HashMap<>();

        List<Object[]> gerritChangesMerged = gerritChangeRepository.getSumActualSizeGroupByMemberAndMerged(startDate, endDate,
                projectsId);
        for (Object[] gerritChange : gerritChangesMerged) {
            String memberName = (String) gerritChange[0];
            Long codeSize = (Long) gerritChange[1];
            if (codeSize != null && codeSize / 1000.0 > 0) {
                Double[] status = {codeSize / 1000.0, 0.0};
                members.put(memberName, status);
            }
        }

        List<Object[]> gerritChangesOpen = gerritChangeRepository.getSumActualSizeGroupByMemberAndOpen(startDate, endDate,
                projectsId);
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

    @GetMapping("3/{startDate}/{endDate}/{projectsId}")
    public String getOverallCodeStatus(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate,
            @PathVariable ArrayList<Long> projectsId) {

        ChartUtils data = new ChartUtils();
        data.addColumnHeading("Status", "string");
        data.addColumnHeading("Code Size", "number");
        data.addColumnAnnotation();

        Integer mergedCodeSize = gerritChangeRepository.getSumActualSizeByStatusIsMerged(startDate, endDate,
                projectsId);
        if (mergedCodeSize != null && mergedCodeSize / 1000.0 > 0) {
            data.addRow("Merged", mergedCodeSize / 1000.0, String.valueOf(mergedCodeSize / 1000.0));
        }

        Integer openCodeSize = gerritChangeRepository.getSumActualSizeByStatusIsOpen(startDate, endDate,
                projectsId);
        if (openCodeSize != null && openCodeSize / 1000.0 > 0) {
            data.addRow("Open", openCodeSize / 1000.0, String.valueOf(openCodeSize / 1000.0));
        }
        return data.createJson();
    }

    @GetMapping("4/{startDate}/{endDate}/{projectsId}")
    public String getCodeCommittedTimeline(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate,
            @PathVariable ArrayList<Long> projectsId) {
        JSONArray data = new JSONArray();
        ChartUtils chartUtils = new ChartUtils();

        List<Object[]> gerritChanges = gerritChangeRepository.getSumActualSizeGroupByUpdatedOn(startDate, endDate,
                projectsId);
        for (Object[] gerritChange : gerritChanges) {
            java.sql.Date date = new java.sql.Date(((Date) gerritChange[0]).getTime());
            Long codeSize = (Long) gerritChange[1];
            JSONArray row = new JSONArray();
            row.add(chartUtils.stringToHighChartDate(date));
            row.add(codeSize / 1000.0);
            data.add(row);
        }
        return data.toJSONString();
    }
}
