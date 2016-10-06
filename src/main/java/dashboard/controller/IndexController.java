package dashboard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 */
@Controller
public class IndexController {
    @GetMapping("/")
    @Transactional(readOnly = true)
    public ModelAndView index() {
        return new ModelAndView("index", "", null);
    }

}
