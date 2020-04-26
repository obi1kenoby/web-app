package project.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.model.*;
import project.service.GradeService;
import project.service.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * REST controller, that handles requests
 * for {@link Grade} entities.
 *
 * @author Alexander Naumov.
 */
@Slf4j
@RestController
@RequestMapping("api/grade")
public class GradeController {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

    @Autowired
    @Qualifier("gradeService")
    private Service gradeService;

    @Autowired
    @Qualifier("studentService")
    private Service studentService;

    @Autowired
    @Qualifier("subjectService")
    private Service subjectService;

    /**
     * Returns special {@link Grade} instance, by ID.
     *
     * @param id faculty ID (primary key).
     * @return special {@link Grade}.
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Model> getById(@PathVariable("id") Long id) {
        if (id == null || id < 1) {
            log.info("IN getById: ID is NULL or less then 1.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Model mark = this.gradeService.getById(id);
        if (mark == null) {
            log.info("IN getById: mark with id: {}, not found.", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(mark, HttpStatus.OK);
    }

    /**
     * Returns HTTP status of removing process of {@link Grade} by their ID.
     *
     * @param id array of marks ID's.
     * @return {@link Grade}.
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Model> deleteById(@PathVariable("id") Long id) {
        if (id == null || id < 1) {
            log.info("IN deleteById: mark id is NULL or less then 1.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (this.gradeService.deleteById(id) == 0) {
            log.info("IN deleteById: deleting mark with id: {} is impossible.", id);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    /**
     * Returns all {@link Grade} instances, for special month.
     *
     * @return list of {@link Grade}.
     */
    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Model>> getMarksByDateRange(@RequestParam("date") String string) {
        LocalDate date;
        try {
            date = LocalDate.parse(string, formatter);
        } catch (DateTimeParseException e) {
            log.error("IN getMarksByDateRange, argument has incorrect format: {}.", string);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        List<Model> marks = ((GradeService) this.gradeService).getGradesByDateRange(date);
        if (marks == null || marks.isEmpty()) {
            LocalDate start = date.withDayOfMonth(1);
            LocalDate end = date.withDayOfMonth(date.lengthOfMonth());
            log.info("IN getMarksByDateRange: in range ({} / {}) marks not found.", start, end);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(marks, HttpStatus.OK);
        }
    }

    /**
     * Returns {@link HttpStatus} of saving new or already existed {@link Grade}.
     *
     * @param id of existed {@link Grade}.
     * @param percent of existed or new {@link Grade}.
     * @param date of existed {@link Grade}.
     * @param studId ID of {@link Student} that corresponds this {@link Grade}.
     * @param subId ID of {@link Subject} that corresponds this {@link Grade}.
     * @return {@link HttpStatus}.
     */
    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Model> save(@RequestParam("id") String id, @RequestParam("percent") String percent,
                                      @RequestParam("date") String date, @RequestParam("stud_id") String studId,
                                      @RequestParam("sub_id") String subId) {
        if (percent.isEmpty() || percent == null || studId == null ||
                studId.isEmpty() || subId == null || subId.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Subject subject = (Subject) this.subjectService.getById(Long.parseLong(subId));
        Student student = (Student) this.studentService.getById(Long.parseLong(studId));
        if (student == null || subject == null) {
            log.info("IN save: ");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Grade grade = Grade.builder().student(student).
                subject(subject)
                .value(Double.parseDouble(percent))
                .grade(GradeValue.getValueFromPercent(Double.parseDouble(percent)))
                .date(LocalDate.parse(date, formatter)).build();
        if (id != null && !id.isEmpty()) {
            grade.setId(Integer.parseInt(id));
        }
        if (this.gradeService.save(grade)) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            log.info("IN save: failed to save grade = {}", grade);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}