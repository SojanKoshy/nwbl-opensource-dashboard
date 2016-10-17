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
import com.huawei.nwbl.opensource.dashboard.domain.ProjectRepository;
import com.huawei.nwbl.opensource.dashboard.service.GerritChangeListScraperService;
import com.huawei.nwbl.opensource.dashboard.service.GerritChangeScraperService;
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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

/**
 *
 */
@Controller
@RequestMapping("members")
public class MemberController {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private GerritChangeRepository gerritChangeRepository;

    @Autowired
    private GerritAccountRepository gerritAccountRepository;

    @Autowired
    private GerritChangeListScraperService gerritChangeListScraperService;

    @Autowired
    private GerritChangeScraperService gerritChangeScraperService;

    @Autowired
    private ProjectController projectController;

    @GetMapping
    public ModelAndView list() {

        List<Member> members = memberRepository.findAllByOrderByName();
        List<GerritChange> changes = gerritChangeRepository.findAllByAccountIsNullOrderByIdDesc();
        ModelAndView modelAndView = new ModelAndView("members/list");
        modelAndView.addObject("members", members);
        modelAndView.addObject("changes", changes);
        return modelAndView;
    }

    @GetMapping("{id}")
    public ModelAndView view(@PathVariable("id") Member member) {
        List<GerritChange> changes = gerritChangeRepository.getAllByMember(member.getId());
        ModelAndView modelAndView = new ModelAndView("members/view");
        modelAndView.addObject("member", member);
        modelAndView.addObject("changes", changes);
        return modelAndView;
    }

    @GetMapping(params = "form")
    public String createForm(@ModelAttribute Member member) {
        return "members/form";
    }

    @PostMapping
    public ModelAndView create(@Valid Member member, BindingResult result,
                               RedirectAttributes redirect) {
        if (result.hasErrors()) {
            return new ModelAndView("members/form", "formErrors", result.getAllErrors());
        }
        if (member.getId() != null) {
            for (GerritAccount account : memberRepository.findOne(member.getId()).getAccounts()) {
                account.setMember(null);
            }
        }
        if (member.getAccounts() != null) {
            for (GerritAccount account : member.getAccounts()) {
                account.setMember(member);
            }
        }
        try {
            member = memberRepository.save(member);
        } catch (DataIntegrityViolationException e) {
            result.addError(new ObjectError("globalProject", "Member name already exists"));
            return new ModelAndView("members/form", "formErrors", result.getAllErrors());
        }

        redirect.addFlashAttribute("globalMember", "Successfully created a new member");
        return new ModelAndView("redirect:/members/{member.id}", "member.id", member.getId());
    }

    @GetMapping(value = "delete/{id}")
    public ModelAndView delete(@PathVariable("id") Long id) {
        memberRepository.delete(id);
        return new ModelAndView("redirect:/members");
    }

    @GetMapping(value = "modify/{id}")
    public ModelAndView modifyForm(@PathVariable("id") Member member) {
        return new ModelAndView("members/form", "member", member);
    }

    @GetMapping("update")
    public ModelAndView update() {
//
//        Member member = memberRepository.findByName("CTS");
//        for (GerritAccount account : member.getAccounts()) {
//            String searchTerm = String.format("owner:\"%s <%s>\"", account.getName(), account.getEmail());
//            gerritChangeListScraperService.scrape(searchTerm);
//        }
//
//        for (Member member : memberRepository.findAllByOrderByName()) {
//            for (GerritAccount account : member.getAccounts()) {
//                String searchTerm = String.format("owner:\"%s <%s>\"", account.getName(), account.getEmail());
//                gerritChangeListScraperService.scrape(searchTerm);
//            }
//        }
//
//        gerritChangeScraperService.scrapeAndGetFirstFilePath("9816");
//
//
//        List<GerritChange> gerritChanges = gerritChangeRepository.findAllByFolderIsNullOrderByIdDesc();
//        for (GerritChange gerritChange : gerritChanges) {
//            String branchId = gerritChange.getLink().split("/")[3];
//            gerritChange.setFirstFilePath(gerritChangeScraperService.scrapeAndGetFirstFilePath(branchId));
//        }
//        gerritChangeRepository.save(gerritChanges);
//
//        List<GerritChange> gerritChanges = gerritChangeRepository.findAllByFirstFilePathIsNotContaining(".java");
//        for (GerritChange gerritChange : gerritChanges) {
//            String branchId = gerritChange.getLink().split("/")[3];
//            gerritChange.setFirstFilePath(gerritChangeScraperService.scrapeAndGetFirstFilePath(branchId));
//        }
//        gerritChangeRepository.save(gerritChanges);
//
//        for (Member member : memberRepository.getDistinctByProject(5L)) {
        for (Member member : memberRepository.findAllByOrderByName()) {
            for (GerritAccount account : member.getAccounts()) {
                String searchTerm = String.format("owner:\"%s <%s>\"", account.getName(), account.getEmail());
                gerritChangeListScraperService.scrape(searchTerm);
            }
        }
        projectController.update();

        return new ModelAndView("redirect:/members");
    }
}
