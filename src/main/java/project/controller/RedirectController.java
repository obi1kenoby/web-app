package project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import project.model.Student;

/**
 * The controller class that redirects on HTML pages according their URLs.
 *
 * @author Alexander Naumov.
 */
@Controller
public class RedirectController {

    /**
     * Redirect to home URL.
     *
     * @return homepage name.
     */
    @GetMapping("/")
    public String homePage() {
        return "home";
    }

    /**
     * Redirect to admin URL.
     *
     * @return admin page.
     */
    @GetMapping("/admin")
    public String admin(Model model){
        model.addAttribute("student", new Student());
        return "admin";
    }

    /**
     * Redirect to contacts information URL.
     *
     * @return contacts page.
     */
    @GetMapping("/contacts")
    public String contacts() {
        return "contacts";
    }

    /**
     * Redirect to main URL that represents academic
     * performance board.
     *
     * @return academic performance manage board.
     */
    @GetMapping("/table")
    public String table() {
        return "table";
    }

    /**
     * Redirect to login URL.
     *
     * @return login page.
     */
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
}
