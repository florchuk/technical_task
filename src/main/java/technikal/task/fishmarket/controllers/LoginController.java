package technikal.task.fishmarket.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/login")
public class LoginController {
    @GetMapping(path = {"", "/"})
    public String login() {
        return "login";
    }
}