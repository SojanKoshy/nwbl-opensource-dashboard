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
import com.huawei.nwbl.opensource.dashboard.domain.CompanyEmailDomain;
import com.huawei.nwbl.opensource.dashboard.domain.CompanyRepository;
import com.huawei.nwbl.opensource.dashboard.domain.Folder;
import com.huawei.nwbl.opensource.dashboard.domain.FolderRepository;
import com.huawei.nwbl.opensource.dashboard.domain.GerritAccount;
import com.huawei.nwbl.opensource.dashboard.domain.GerritAccountRepository;
import com.huawei.nwbl.opensource.dashboard.domain.GerritChange;
import com.huawei.nwbl.opensource.dashboard.domain.GerritChangeRepository;
import com.huawei.nwbl.opensource.dashboard.domain.Member;
import com.huawei.nwbl.opensource.dashboard.domain.MemberRepository;
import com.huawei.nwbl.opensource.dashboard.domain.Project;
import com.huawei.nwbl.opensource.dashboard.domain.ProjectRepository;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 */
@Controller
@RequestMapping("companies")
public class CompanyController {

    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private FolderRepository folderRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private GerritChangeRepository gerritChangeRepository;

    @Autowired
    private GerritAccountRepository gerritAccountRepository;

    @GetMapping
    public ModelAndView list() {
        List<Company> companies = companyRepository.findByOrderByName();
        return new ModelAndView("companies/list", "companies", companies);
    }

    @GetMapping(params = "form")
    public String createForm(@ModelAttribute Company company) {
        return "companies/form";
    }

    @PostMapping
    public ModelAndView create(@Valid Company company, BindingResult result,
                               RedirectAttributes redirect) {
        if (result.hasErrors()) {
            return new ModelAndView("companies/form", "formErrors", result.getAllErrors());
        }

        if (company.getId() != null) {
            for (CompanyEmailDomain emailDomain : companyRepository.findOne(company.getId()).getEmailDomains()) {
                emailDomain.setCompany(null);
            }
        }
        if (company.getEmailDomains() != null) {
            for (CompanyEmailDomain emailDomain : company.getEmailDomains()) {
                emailDomain.setCompany(company);
            }
        }
        try {
            company = companyRepository.save(company);
        } catch (DataIntegrityViolationException e) {
            result.addError(new ObjectError("globalProject", "Company name already exists"));
            return new ModelAndView("companies/form", "formErrors", result.getAllErrors());
        }
        redirect.addFlashAttribute("globalCompany", "Successfully created a new company");
        return new ModelAndView("redirect:/companies/{company.id}", "company.id", company.getId());
    }

    @GetMapping("{id}")
    public ModelAndView view(@PathVariable("id") Company company) {
        ModelAndView modelAndView = new ModelAndView("companies/view");
        modelAndView.addObject("company", company);
        return modelAndView;
    }

    @GetMapping(value = "delete/{id}")
    public ModelAndView delete(@PathVariable("id") Long id) {
        companyRepository.delete(id);
        return new ModelAndView("redirect:/companies");
    }

    @GetMapping(value = "modify/{id}")
    public ModelAndView modifyForm(@PathVariable("id") Company company) {
        return new ModelAndView("companies/form", "company", company);
    }

}
