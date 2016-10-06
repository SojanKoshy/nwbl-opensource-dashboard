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

import dashboard.domain.Member;
import dashboard.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
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

    @GetMapping
    public ModelAndView list() {
        List<Member> members = memberRepository.findAllByOrderByName();
        return new ModelAndView("members/list", "members", members);
    }

    @GetMapping("{id}")
    public ModelAndView view(@PathVariable("id") Member member) {
        return new ModelAndView("members/view", "member", member);
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
        member = memberRepository.save(member);
        redirect.addFlashAttribute("globalMember", "Successfully created a new member");
        return new ModelAndView("redirect:/members/{member.id}", "member.id", member.getId());
    }

    @RequestMapping("foo")
    public String foo() {
        throw new RuntimeException("Expected exception in controller");
    }

    @GetMapping(value = "delete/{id}")
    public ModelAndView delete(@PathVariable("id") Long id) {
        //TODO memberRepository.deleteMember(id);
        Iterable<Member> members = memberRepository.findAllByOrderByName();
        return new ModelAndView("members/list", "members", members);
    }

    @GetMapping(value = "modify/{id}")
    public ModelAndView modifyForm(@PathVariable("id") Member member) {
        return new ModelAndView("members/form", "member", member);
    }

}
