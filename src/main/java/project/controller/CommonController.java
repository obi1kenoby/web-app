package project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import project.model.Student;

/**
 * The controller class that hadles loading hmtl pages.
 */
@Controller
public class CommonController {

//    @Autowired
//    private DateService service;
//
//    // login
//
//    @RequestMapping(value = "/login", method = RequestMethod.GET)
//    public String login(Model model, @RequestParam(value = "error", required = false) String error, @RequestParam(value = "logout", required = false) String logout) {
//
//        if (error != null) {
//            model.addAttribute("error", "Invalid username and password!");
//        }
//
//        if (logout != null) {
//            model.addAttribute("msg", "You've been logged out successfully.");
//        }
//        return "header";
//
//    }
//
//    // get header html page.
//
//    @RequestMapping(value = "/header", method = RequestMethod.GET)
//    public String getHeader(Model model){
//        return "header";
//    }
//
//    // get footer html page.
//
//    @RequestMapping(value = "/footer", method = RequestMethod.GET)
//    public String getFooter(){
//        return "footer";
//    }
//
//    // get admin jsp page.
//
    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String admin(Model model){
        model.addAttribute("student", new Student());
        return "admin";
    }
//
//    // gets all days of month of that date excludes weekends.
//
//    @RequestMapping(value = "/dates/{date}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    public @ResponseBody List<LocalDate> getDates(@PathVariable("date")String date){
//        String[] strings = date.split("-");
//        List<LocalDate> dates = service.month(LocalDate.of(Integer.parseInt(strings[0]), Integer.parseInt(strings[1]), 1));
//        return dates;
//    }
}
