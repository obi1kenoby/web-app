package project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import project.model.Student;

/**
 * The controller class that redirects on hmtl pages according their URLs.
 *
 * @author Alexander Naumov.
 */
@Controller
public class RedirectController {

    @GetMapping("/header")
    public String getHeader(){
        return "header";
    }

    @GetMapping("/admin")
    public String admin(Model model){
        model.addAttribute("student", new Student());
        return "admin";
    }

    @GetMapping("/contacts")
    public String contacts() {
        return "contacts";
    }

    @GetMapping("/table")
    public String table() {
        return "table";
    }
}
