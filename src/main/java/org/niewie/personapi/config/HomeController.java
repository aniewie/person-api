package org.niewie.personapi.config;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author aniewielska
 * @since 19/07/2018
 * <p>
 *
 * Home redirection to swagger api documentation
 */
@Controller
public class HomeController {

    @RequestMapping(value = "/")
    public String index() {
        return "redirect:swagger-ui.html";
    }
}

