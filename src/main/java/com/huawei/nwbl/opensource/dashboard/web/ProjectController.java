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
import com.huawei.nwbl.opensource.dashboard.service.GerritChangeListScraperService;
import com.huawei.nwbl.opensource.dashboard.service.GerritChangeScraperService;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 */
@Controller
@RequestMapping("projects")
public class ProjectController {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private FolderRepository folderRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private GerritChangeRepository gerritChangeRepository;

    @GetMapping
    public ModelAndView list() {
        List<Project> projects = projectRepository.findAllByOrderByName();
        List<GerritChange> changes = gerritChangeRepository.findByFolderIsNull();
        ModelAndView modelAndView = new ModelAndView("projects/list");
        modelAndView.addObject("projects", projects);
        modelAndView.addObject("changes", changes);
        return modelAndView;
    }

    @GetMapping("{id}")
    public ModelAndView view(@PathVariable("id") Project project) {
        return new ModelAndView("projects/view", "project", project);
    }

    @GetMapping(params = "form")
    public String createForm(@ModelAttribute Project project) {
        return "projects/form";
    }

    @PostMapping
    public ModelAndView create(@Valid Project project, BindingResult result,
                               RedirectAttributes redirect) {
        if (result.hasErrors()) {
            return new ModelAndView("projects/form", "formErrors", result.getAllErrors());
        }
        for (Folder folder : project.getFolders()) {
            folder.setProject(project);
        }
        try {
            project = projectRepository.save(project);
        } catch (Exception e) {
            e.printStackTrace();
            result.addError(new ObjectError("globalProject", "Cannot Save"));
            return new ModelAndView("projects/form", "formErrors", result.getAllErrors());
        }
        redirect.addFlashAttribute("globalProject", "Successfully created a new project");
        return new ModelAndView("redirect:/projects/{project.id}", "project.id", project.getId());
    }

    @GetMapping(value = "delete/{id}")
    public ModelAndView delete(@PathVariable("id") Long id) {
        projectRepository.delete(id);
        return new ModelAndView("redirect:/projects");
    }

    @GetMapping(value = "modify/{id}")
    public ModelAndView modifyForm(@PathVariable("id") Project project) {
        return new ModelAndView("projects/form", "project", project);
    }

    @GetMapping("update")
    public ModelAndView update() {
        List<Folder> folders = folderRepository.findAll();
        for (Folder folder : folders) {
            List<GerritChange> gerritChanges = gerritChangeRepository.findByFirstFilePathContaining(folder.getName());
            if (gerritChanges != null) {
                for (GerritChange gerritChange : gerritChanges) {
                    gerritChange.setFolder(folder);
                }
                folder.setGerritChanges(gerritChanges);
            }
        }
        folderRepository.save(folders);

        List<Project> projectList = projectRepository.findAll();
        for (Project project : projectList) {
            Set<Member> members = new HashSet<>();
            for (Folder folder : project.getFolders()) {
                Set<GerritAccount> gerritAccounts = new HashSet<>();
                for (GerritChange gerritChange : folder.getGerritChanges()) {
                    gerritAccounts.add(gerritChange.getAccount());
                }
                for (GerritAccount gerritAccount : gerritAccounts) {
                    members.add(gerritAccount.getMember());
                }
            }
            project.setMembers(members);
        }
        for (Member member : memberRepository.findAll()) {
            Set<Project> projectSet = new HashSet<>();
            for (Project project : projectList) {
                if (project.getMembers().contains(member))
                    projectSet.add(project);
            }
            member.setProjects(projectSet);
        }
        projectRepository.save(projectList);

        return new ModelAndView("redirect:/projects");
    }
}
