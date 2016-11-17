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

import com.huawei.nwbl.opensource.dashboard.domain.Company;
import com.huawei.nwbl.opensource.dashboard.domain.CompanyRepository;
import com.huawei.nwbl.opensource.dashboard.domain.GerritAccount;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 *
 */
@RestController
@RequestMapping("company")
public class CompanyController {

    @Autowired
    private CompanyRepository companyRepository;


    @GetMapping
    public ModelAndView list() {
        List<Company> companies = companyRepository.getDistinctHasAccountsOrderByName();
        return new ModelAndView("company/list", "companies", companies);
    }


    @GetMapping("json")
    public String getMembers() {

        List<Company> companies = companyRepository.getDistinctHasAccountsOrderByName();


        JSONObject companiesJson = new JSONObject();

        for (Company company : companies) {
            JSONArray members = new JSONArray();
            for (GerritAccount account : company.getGerritAccounts()) {
                members.add(account.getName());
            }

            companiesJson.put(company.getId(), members);
        }

        return companiesJson.toJSONString();
    }
}
