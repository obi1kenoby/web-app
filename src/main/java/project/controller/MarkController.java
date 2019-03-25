package project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.model.Mark;
import project.model.Model;
import project.model.Student;
import project.model.Subject;
import project.service.MarkService;
import project.service.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * REST controller, that handles requests
 * for {@link Mark} entities.
 *
 * @author Alexander Naumov.
 */
@RestController
@RequestMapping("api/mark")
public class MarkController {

    private static DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

    @Autowired
    @Qualifier("markService")
    private Service markService;

    @Autowired
    @Qualifier("studentService")
    private Service studentService;

    @Autowired
    @Qualifier("subjectService")
    private Service subjectService;

    /**
     * Returns special {@link Mark} instance, by ID.
     *
     * @param id department ID (primary key).
     * @return special {@link Mark}.
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Model> getById(@PathVariable("id") Long id) {
        Model mark = this.markService.getById(id);
        if (mark == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(mark, HttpStatus.OK);
    }

    /**
     * Returns HTTP status of removing process of {@link Mark} by their ID.
     *
     * @param id array of marks ID's.
     * @return {@link HttpStatus}.
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Model> delete(@PathVariable("id") Long id) {
        if (id == null || id == 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (!this.markService.deleteById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    /**
     * Returns all {@link Mark} instances, for special month.
     *
     * @return list of marks.
     */
    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<Model>> getMarksByDateRange(@RequestParam("date") String string) {
        if (string == null || string.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        LocalDate date = LocalDate.parse(string, formatter);
        List<Model> marks = ((MarkService) this.markService).getMarksByDateRange(date);
        if (marks == null || marks.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(marks, HttpStatus.OK);
        }
    }

    /**
     * Returns {@link HttpStatus} of saving new or already existed {@link Mark}.
     *
     * @param id     of existed {@link Mark}.
     * @param value  of existed or new {@link Mark}.
     * @param date   of existed {@link Mark}.
     * @param studId ID of {@link Student} that coresponds this {@link Mark}.
     * @param subId  ID of {@link Subject} that coresponds this {@link Mark}.
     * @return {@link HttpStatus}.
     */
    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Model> save(@RequestParam("id") String id, @RequestParam("value") String value,
                                      @RequestParam("date") String date, @RequestParam("stud_id") String studId,
                                      @RequestParam("sub_id") String subId) {
        if (value.isEmpty() || value.equals("0") || studId == null ||
                studId.isEmpty() || subId == null || subId.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Subject subject = (Subject) this.subjectService.getById(Long.parseLong(subId));
        Student student = (Student) this.studentService.getById(Long.parseLong(studId));
        if (student == null || subject == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Mark mark = Mark.builder().student(student).subject(subject).value(Integer.parseInt(value)).date(LocalDate.parse(date, formatter)).build();
        if (id != null || id.isEmpty()) {
            mark.setId(Integer.parseInt(id));
        }
        if (this.markService.save(mark)) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
