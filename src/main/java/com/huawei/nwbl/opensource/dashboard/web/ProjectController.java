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

import com.huawei.nwbl.opensource.dashboard.domain.Project;
import com.huawei.nwbl.opensource.dashboard.domain.ProjectRepository;
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
@RequestMapping("projects")
public class ProjectController {

    @Autowired
    private ProjectRepository projectRepository;

    @GetMapping
    public ModelAndView list() {
        List<Project> projects = projectRepository.findAllByOrderByName();
        return new ModelAndView("projects/list", "projects", projects);
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
        try {
            project = projectRepository.save(project);
        } catch (DataIntegrityViolationException e) {
            result.addError(new ObjectError("globalProject", "Project name already exists"));
            return new ModelAndView("projects/form", "formErrors", result.getAllErrors());
        }
        redirect.addFlashAttribute("globalProject", "Successfully created a new project");
        return new ModelAndView("redirect:/projects/{project.id}", "project.id", project.getId());
    }

    @GetMapping(value = "delete/{id}")
    public ModelAndView delete(@PathVariable("id") Long id) {
        projectRepository.delete(id);
        List<Project> projects = projectRepository.findAllByOrderByName();
        return new ModelAndView("projects/list", "projects", projects);
    }

    @GetMapping(value = "modify/{id}")
    public ModelAndView modifyForm(@PathVariable("id") Project project) {
        return new ModelAndView("projects/form", "project", project);
    }

}
