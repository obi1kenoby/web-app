package project.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.model.Faculty;
import project.model.Model;
import project.model.Subject;
import project.service.FacultytService;
import project.service.Service;

import java.util.Arrays;
import java.util.List;


/**
 * REST controller class, that handles requests
 * for {@link Faculty} entities.
 *
 * @author Alexander Naumov.
 */
@Slf4j
@RestController
@RequestMapping("/api/faculty")
public class FacultyController {

    @Autowired
    @Qualifier("facultyService")
    private Service facultyService;

    @Autowired
    @Qualifier("subjectService")
    private Service subjectService;

    /**
     * Returns all {@link Faculty} instances, sorted by name.
     *
     * @return set of faculties.
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Model>> list() {
        List<Model> facultys = this.facultyService.getAll();
        if (facultys.isEmpty()) {
            log.info("IN list, facultys not found.");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(facultys, HttpStatus.OK);
    }

    /**
     * Creates new {@link Faculty} by special name. Also links this
     * new faculty with special subjects.
     *
     * @param ids array of IDs of {@link Subject}.
     * @param name faculty name.
     * @return just created {@link Faculty} instance.
     */
    @PostMapping(value = "/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Model> save(@RequestParam(value="array[]") Long[] ids, @PathVariable("name") String name) {
        if (name == null || name.isEmpty() || ids.length < 1) {
            log.info("IN save: the name argument is empty or array of id's is empty.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Faculty faculty = new Faculty(name);
        List<Model> subjects = this.subjectService.getListById(ids);
        if (subjects == null || subjects.isEmpty()) {
            log.info("IN save: subjects with id's: {} is not exist.", Arrays.toString(ids));
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        ((FacultytService)this.facultyService).saveWithSubject(faculty, subjects);
        Model result = ((FacultytService)this.facultyService).getByName(name);
        if (result != null && result.getId() > 0) {
           return new ResponseEntity<>(faculty, HttpStatus.OK);
        }
        log.info("IN save: bad request.");
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles the case if you try save faculty {@link Faculty}
     * with empty name or name that equals null.
     *
     * @return BAD REQUEST.
     */
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Model> saveEmptyName() {
        log.info("IN saveEmptyName: the faculty name can not be empty or equals null!");
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    /**
     * Returns special {@link Faculty} instance, by ID.
     *
     * @param ID faculty ID (primary key).
     * @return special {@link Faculty}.
     */
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Model> getById(@PathVariable("id") Long ID) {
        if (ID == null|| ID < 1L) {
            log.info("IN getById: ID is NULL or less then 1.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Model faculty = this.facultyService.getById(ID);
        if (faculty != null) {
            return new ResponseEntity<>(faculty, HttpStatus.OK);
        }
        log.info("IN getById: faculty with id: {} not found.", ID);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * Returns HTTP status of removing process of {@link Faculty} by their ID.
     *
     * @param id array of faculties ID's.
     * @return {@link HttpStatus}.
     */
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Model> deleteById(@PathVariable("id") Long id) {
        if (id == null || id < 1) {
            log.info("IN deleteById: ID is NULL or less then 1.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (this.facultyService.deleteById(id) > 0) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            log.info("IN deleteById: deleting faculty with Id: {} is impossible.", id);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}