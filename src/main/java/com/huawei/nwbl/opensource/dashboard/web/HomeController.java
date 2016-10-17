package com.huawei.nwbl.opensource.dashboard.web;

import com.huawei.nwbl.opensource.dashboard.domain.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 */
@Controller
@RequestMapping("/")
public class HomeController {

    @Autowired
    ProjectRepository projectRepository;

    @GetMapping
    public ModelAndView index() {
        return new ModelAndView("index", "projects", projectRepository.getAllByIsVisibleOrderByName());
    }

}
