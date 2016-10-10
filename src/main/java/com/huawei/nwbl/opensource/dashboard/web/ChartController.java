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

import com.huawei.nwbl.opensource.dashboard.domain.GerritAccount;
import com.huawei.nwbl.opensource.dashboard.domain.GerritAccountRepository;
import com.huawei.nwbl.opensource.dashboard.domain.GerritChange;
import com.huawei.nwbl.opensource.dashboard.domain.GerritChangeRepository;
import com.huawei.nwbl.opensource.dashboard.domain.Member;
import com.huawei.nwbl.opensource.dashboard.domain.MemberRepository;
import com.huawei.nwbl.opensource.dashboard.utils.ChartUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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

    @GetMapping("1")
    public String plotCodeMergedByMembers() {
        ChartUtils data = new ChartUtils();
        data.addColumnHeading("Member Name", "string");
        data.addColumnHeading("Code Size", "number");

        // FIXME Use @Query for faster access
        for (Member member : memberRepository.findAll()) {
            Integer codeSize = 0;
            for (GerritAccount account : member.getAccounts()) {
                for (GerritChange gerritChange : account.getGerritChanges()) {
                    codeSize += gerritChange.getActualSize();
                }
            }
            if (codeSize / 1000.0 > 0) {
                data.addRow(member.getName(), codeSize / 1000.0);
            }
        }
        return data.createJson();
    }

    @GetMapping("2")
    public String plotCodeStatus() {
        ChartUtils data = new ChartUtils();
        data.addColumnHeading("Status", "string");
        data.addColumnHeading("Code Size", "number");

        // FIXME Use @Query for faster access
        List<GerritChange> changes = gerritChangeRepository.findAll();
        Map<String, Integer> map = new TreeMap<>();
        for (GerritChange change : changes) {
            String key = change.getStatus();
            Integer value = change.getActualSize();
            if (map.containsKey(key)) {
                map.put(key, map.get(key) + value);
            } else {
                map.put(key, value);
            }
        }
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            data.addRow(entry.getKey(), entry.getValue() / 1000.0);
        }

        return data.createJson();
    }

    @GetMapping("3")
    public String plotCodeMergeTimeline() {
        ChartUtils data = new ChartUtils();
        data.addColumnHeading("Date", "date");
        data.addColumnHeading("Code Size", "number");

        //TODO Add status selection in chart
        List<GerritChange> changes = gerritChangeRepository.findAll();
        for (GerritChange change : changes) {
            data.addRow(data.stringToGoogleDate(change.getUpdatedOn()), change.getActualSize() / 1000);
        }
        return data.createJson();
    }
}
