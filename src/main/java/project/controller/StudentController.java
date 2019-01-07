package project.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.model.Student;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

import static java.util.stream.Collectors.toList;


@Controller
public class StudentController {
//
//    @Autowired
//    private DepartmentDao departmentDao;
//    @Autowired
//    private StudentDao studentDao;
//
//    @Autowired
//    private BCryptPasswordEncoder encoder;
//    private static Logger logger = Logger.getLogger(MarkController.class);
//
//
//    @InitBinder
//    public void initBinder(WebDataBinder binder) {
//        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//        sdf.setLenient(true);
//        binder.registerCustomEditor(LocalDate.class, new CustomDateEditor(sdf, true));
//    }
//
//    // gets table jsp page that represent of ratings.
//
//    @RequestMapping(value = "{department}/{subject}/marks", method = RequestMethod.GET)
//    public String ratingBoard(@PathVariable("department") String department, @PathVariable("subject") String subject) {
//        logger.info("loading ratings on " + subject + " for department ID:"  + department + "...");
//        return "table";
//    }
//
//    // returns student page and put into page this student and his average rating.
//
//    @RequestMapping(value = "/students/{id}", method = RequestMethod.GET)
//    public String studentInfoPage(@PathVariable("id")long id, Model model){
//        Student student = studentDao.findOne(id);
//        String avg = studentDao.averageValue(student.getLast_name(), student.getFirst_name());
//        model.addAttribute("student", student);
//        model.addAttribute("avgValue", avg);
//        return "student";
//    }
//
//    // returns student object by its id in json format.
//
//    @RequestMapping(value = "/student/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    public @ResponseBody Student getStudent(@PathVariable("id") long id){
//        return studentDao.findOne(id);
//    }
//
//    // save student with photo to DB and bounds him with appropriate department.
//
//    @RequestMapping(value = "student/save", method = RequestMethod.POST)
//    public String addStudent(Model model, @ModelAttribute("student")Student student,
//        @RequestParam("file")MultipartFile file, @RequestParam("dep")String department) throws IOException {
//        String base64 = Base64.getEncoder().encodeToString(file.getBytes());
//        student.setPhoto(base64);
//        student.setDepartment(departmentDao.getByName(department));
//        student.setPassword(encoder.encode(student.getPassword()));
//        studentDao.save(student);
//        logger.info("student " + student + " was successfully saved.");
//        model.addAttribute("student", new Student());
//        return "admin";
//    }
//
//    // remove student by its ID.
//
//    @RequestMapping(value = "/student/{id}/delete", method = RequestMethod.GET)
//    public @ResponseBody int deleteStudent(@PathVariable("id") long id){
//        int result = studentDao.deleteById(id);
//        if (result > 0){
//            logger.info("student with ID is " + id + " successfully removed.");
//        } else {
//            logger.info("student with ID is" + id + " not exist or already removed earlier.");
//        }
//        return result;
//    }
//
//    @RequestMapping(value = "/student/{first_name}/{last_name}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    public @ResponseBody Student getStudentByName(@PathVariable("first_name")String firstName, @PathVariable("last_name")String lastName) {
//        return studentDao.getByNameAndLastName(firstName, lastName);
//    }
//
//    // return all students of department from DB.
//
//    @RequestMapping(value = "/{department}/students", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    public @ResponseBody List<Student> studentsOfDepartment(@PathVariable("department") long id){
//        return studentDao.getByDepartment(id).stream().sorted().collect(toList());
//    }
//
//    // removes student group from DB and return array of their IDs back as response.
//
//    @RequestMapping(value = "/delete-group", method = RequestMethod.POST)
//    public @ResponseBody long[] deleteByGroup(@RequestParam(value="array[]") long[] id){
//        if (id.length > 0){
//            studentDao.deleteByGroup(id);
//        }
//        return id;
//    }
//
//    // return all students of DB.
//
//    @RequestMapping(value = "/students", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    public @ResponseBody List<Student> allStudents(){
//        return studentDao.findAll().stream().sorted().collect(toList());
//    }
}