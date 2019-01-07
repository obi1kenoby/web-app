package project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import project.model.Model;
import project.model.Subject;

import java.util.List;
import java.util.Optional;

/**
 * REST Controller class, that handles requests
 * for {@link Subject} entities.
 *
 * @author Alexander Naumov.
 */
@Controller
@RequestMapping("/api/subject")
public class SubjectController {



//    /**
//     * Returns list of {@link Subject} as JSON format.
//     *
//     * @return list of {@link ResponseEntity}.
//     */
//    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    public ResponseEntity<List<Model>> list() {
//        Optional<List<Model>> optional = service.getAll();
//        return optional.map(model -> new ResponseEntity<>(model, HttpStatus.OK))
//                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
//    }
//
//    @Autowired
//    private DepartmentDao departmentDao;
//
//    // get subject information
//
//    @RequestMapping(value = "/subject/{id}", method = RequestMethod.GET)
//    public String subjectInfo(@PathVariable("id") String id, Model model){
//        Subject subject = subjectDao.findOne(Long.parseLong(id));
//        model.addAttribute("subject", subject);
//        return "subject";
//    }
//
//    // get all subjects of current department as response body (json object).
//
//    @RequestMapping(value = "/subjects/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    public @ResponseBody Set<Subject> getSubjects(@PathVariable("id") String id){
//        return null;
//    }
//
//    // if department which ID is "id" contains subject with name "subject".
//
//    @RequestMapping(value = "/{subject}/{department}/subject", method = RequestMethod.GET)
//    public @ResponseBody boolean isSubjectInDepartment(@PathVariable("subject") String subj, @PathVariable("department") long id){
//         return true;
//    }
//
//    // return all subjects from DB.
//
//    @RequestMapping(value = "/subjects", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    public @ResponseBody List<Subject> allSubject(){
//        return subjectDao.findAll();
//    }
}