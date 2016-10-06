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

package dashboard.controller;

import dashboard.domain.GerritChange;
import dashboard.repository.GerritChangeRepository;
import dashboard.utils.ChartData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
public class ChartController {

    @Autowired
    private GerritChangeRepository gerritChangeRepository;

    @GetMapping("chart/1")
    public String chart1() {
        ChartData data = new ChartData();
        data.addColumnHeading("Member Name", "string");
        data.addColumnHeading("Code Size", "number");

        List<GerritChange> changes = gerritChangeRepository.findByStatus("Merged");
        Map<String, Integer> map = new TreeMap<>();
        for (GerritChange change: changes) {
            String key = change.getOwner();
            Integer value = change.getActualSize();
            if(map.containsKey(key)) {
                map.put(key, map.get(key) + value);
            } else {
                map.put(key, value);
            }
        }
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            data.addRow(entry.getKey(), entry.getValue()/1000);
        }

        return data.createJson();
    }

    @GetMapping("chart/2")
    public String chart2() {
        ChartData data = new ChartData();
        data.addColumnHeading("Status", "string");
        data.addColumnHeading("Code Size", "number");

        List<GerritChange> changes = gerritChangeRepository.findAll();
        Map<String, Integer> map = new TreeMap<>();
        for (GerritChange change: changes) {
            String key = change.getStatus();
            Integer value = change.getActualSize();
            if(map.containsKey(key)) {
                map.put(key, map.get(key) + value);
            } else {
                map.put(key, value);
            }
        }
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            data.addRow(entry.getKey(), entry.getValue()/1000);
        }

        return data.createJson();
    }

    @GetMapping("chart/3")
    public String chart3() {
        ChartData data = new ChartData();
        data.addColumnHeading("Date", "date");
        data.addColumnHeading("Code Size", "number");

        List<GerritChange> changes = gerritChangeRepository.findAll();
        Map<String, Integer> map = new TreeMap<>();
        for (GerritChange change: changes) {
            data.addRow(data.stringToGoogleDate(change.getUpdatedOn()),  change.getActualSize()/1000);
        }
        return data.createJson();
    }
}
