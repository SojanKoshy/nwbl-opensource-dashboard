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
import com.huawei.nwbl.opensource.dashboard.domain.GerritAccountRepository;
import com.huawei.nwbl.opensource.dashboard.service.GerritAccountScraperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 *
 */
@Controller
@RequestMapping("accounts")
public class GerritAccountController {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private GerritAccountRepository gerritAccountRepository;

    @Autowired
    private GerritAccountScraperService gerritAccountScraperService;

    @GetMapping
    public ModelAndView list() {
        List<GerritAccount> accounts = gerritAccountRepository.getAllOrderByMemberName();
        accounts.addAll(gerritAccountRepository.findAllByMemberIsNullOrderByName());
        return new ModelAndView("accounts/list", "accounts", accounts);
    }

    @GetMapping("update")
    public ModelAndView update() {
        gerritAccountScraperService.scrape();
        return new ModelAndView("redirect:/accounts");
    }


    @GetMapping("update_company")
    public ModelAndView updateCompany() {

        List<GerritAccount> huaweiAccounts = gerritAccountRepository.getAllOrderByMemberName();
        Company huawei = companyRepository.getByEmailDomain("huawei.com");
        for (GerritAccount account : huaweiAccounts) {
            account.setCompany(huawei);
        }

        List<GerritAccount> accounts = gerritAccountRepository.findAllByMemberIsNullOrderByName();
        for (GerritAccount account : accounts) {
            String email = account.getEmail();
            if (email != null && email.contains("@")) {
                String emailDomain = email.split("@")[1];
                account.setCompany(companyRepository.getByEmailDomain(emailDomain));
            }
        }
        accounts.addAll(huaweiAccounts);
        gerritAccountRepository.save(accounts);

        return new ModelAndView("redirect:/accounts");
    }
}
