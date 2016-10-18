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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 */
@RestController
@RequestMapping("chart")
public class ChartController {

    @Autowired
    private GerritChangeRepository gerritChangeRepository;

    @Autowired
    private GerritAccountRepository gerritAccountRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProjectRepository projectRepository;


    @GetMapping("1/{startDate}/{endDate}/{projectsId}")
    public String getCodeContributionProjectwise(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate,
            @PathVariable ArrayList<Long> projectsId) {
        ChartUtils data = new ChartUtils();
        data.addColumnHeading("Project Name", "string");
        data.addColumnHeading("Code Size", "number");
        data.addColumnAnnotation();

        List<Object[]> gerritChanges = gerritChangeRepository.getSumActualSizeGroupByProject(startDate, endDate,
                projectsId);
        for (Object[] gerritChange : gerritChanges) {
            String projectName = (String) gerritChange[0];
            Long codeSize = (Long) gerritChange[1];
            if (codeSize != null && codeSize / 1000.0 > 0) {
                data.addRow(projectName, codeSize / 1000.0, String.valueOf(codeSize / 1000.0));
            }
        }
        return data.createJson();
    }

    @GetMapping("2/{startDate}/{endDate}/{projectsId}")
    public String getCodeContributionMemberwise(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate,
            @PathVariable ArrayList<Long> projectsId) {
        ChartUtils data = new ChartUtils();
        data.addColumnHeading("Member Name", "string");
        data.addColumnHeading("Code Size", "number");
        data.addColumnAnnotation();

        List<Object[]> gerritChanges = gerritChangeRepository.getSumActualSizeGroupByMember(startDate, endDate,
                projectsId);
        for (Object[] gerritChange : gerritChanges) {
            String memberName = (String) gerritChange[0];
            Long codeSize = (Long) gerritChange[1];
            if (codeSize != null && codeSize / 1000.0 > 0) {
                data.addRow(memberName, codeSize / 1000.0, String.valueOf(codeSize / 1000.0));
            }
        }
        return data.createJson();
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

        ChartUtils data = new ChartUtils();
        data.addColumnHeading("Date", "date");
        data.addColumnHeading("Code Size", "number");

        List<Object[]> gerritChanges = gerritChangeRepository.getSumActualSizeGroupByUpdatedOn(startDate, endDate,
                projectsId);
        for (Object[] gerritChange : gerritChanges) {
            java.sql.Date date = new java.sql.Date(((Date) gerritChange[0]).getTime());
            Long codeSize = (Long) gerritChange[1];
            data.addRow(data.stringToGoogleDate(date), codeSize / 1000.0);
        }
        return data.createJson();
    }

}
