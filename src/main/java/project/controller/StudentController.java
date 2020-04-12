package project.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.model.Department;
import project.model.Model;
import project.model.Role;
import project.model.Student;
import project.service.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

/**
 * REST controller that handles requests
 * for {@link Student} entities.
 *
 * @author Alexander Naumov.
 */
@Slf4j
@RestController
@RequestMapping("/api/student")
public class StudentController {

    @Autowired
    @Qualifier("studentService")
    private Service studentService;

    @Autowired
    @Qualifier("departmentService")
    private Service departmentService;

    @Autowired
    private BCryptPasswordEncoder encoder;

    /**
     * Returns all {@link Student} from database.
     *
     * @return set of students.
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<Model>> list() {
        List<Model> students = this.studentService.getAll();
        if (students.isEmpty()) {
            log.info("IN list: students not found.");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

    /**
     * Returns special {@link Student} instance, by ID.
     *
     * @param id department ID (primary key).
     * @return special {@link Student}.
     */
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Model> getById(@PathVariable("id") Long id){
        if (id == null || id <= 0) {
            log.info("IN getById: ID is NULL or less then 1.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Model model = this.studentService.getById(id);
        if (model != null) {
            return new ResponseEntity<>(model, HttpStatus.OK);
        }
        log.info("IN getById: student with id: {}, not found.", id);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * Save new instance of {@link Student} to DB.
     *
     * @param student {@link Student} instance for saving.
     * @param photo {@link MultipartFile} photo.
     * @param depId ID of {@link Department}.
     * @throws IOException if photo can't produce bytes.
     */
    @PostMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Student> save(@ModelAttribute("student") Student student, @RequestParam(value = "file", required = false) MultipartFile photo,
                                        @RequestParam("day")String day, @RequestParam("month")String month, @RequestParam("year")String year,
                                        @RequestParam("depId")String depId) throws IOException {
        if (student == null) {
            log.info("IN save: student can't be NULL.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Department department = (Department)this.departmentService.getById(Long.parseLong(depId));
        if (photo != null) {
            student.setPhoto(Base64.getEncoder().encodeToString(photo.getBytes()));
        }
        try {
            student.setPassword(encoder.encode(student.getPassword()));
            student.setDepartment(department);
            student.setBirthday(LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day)));
            student.setRole(Role.USER);
            this.studentService.save(student);
        } catch (Exception e) {
            log.info("IN save: saving user was failed.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Returns HTTP status of removing process of {@link Student} by their ID.
     *
     * @param id array of Students ID
     * @return {@link HttpStatus}.
     */
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Student> deleteById(@PathVariable("id") Long id) {
        if (id == null || id < 1) {
            log.info("IN deleteById: student id is NULL or less then 1.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            if (this.studentService.deleteById(id) > 0) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                log.info("IN deleteById: deleting student with id: {} is impossible.", id);
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
    }

    /**
     * Returns list of {@link Student} by its {@link Department} ID.
     *
     * @param depId of {@link Department}.
     * @return {@link List<Student>}.
     */
    @RequestMapping(value = "/department/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<Student>> getStudentsByDep(@PathVariable("id") String depId) {
        if (depId == null || Long.parseLong(depId) < 1L) {
            log.info("IN getStudentByDep: department id is NULL or less then 1.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        List<Student> students = new ArrayList<>(((Department) departmentService.getById(Long.parseLong(depId))).getStudents());
        Collections.sort(students);
        return new ResponseEntity<>(students, HttpStatus.OK);
    }
}