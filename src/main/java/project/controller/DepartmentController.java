package project.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project.model.Model;
import project.model.Subject;
import project.model.Department;
import project.service.DepartmentService;
import project.service.Service;

import java.util.Arrays;
import java.util.List;


/**
 * REST controller class, that handles requests
 * for {@link Department} entities.
 *
 * @author Alexander Naumov.
 */
@Slf4j
@RestController
@RequestMapping("/api/department")
public class DepartmentController {

    @Autowired
    @Qualifier("departmentService")
    private Service departmentService;

    @Autowired
    @Qualifier("subjectService")
    private Service subjectService;

    /**
     * Returns all {@link Department} instances, sorted by name.
     *
     * @return set of departments.
     */
    @PreAuthorize("hasAuthority('departments:read')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Model>> list() {
        List<Model> departments = this.departmentService.getAll();
        if (departments.isEmpty()) {
            log.info("IN list, departments not found.");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(departments, HttpStatus.OK);
    }

    /**
     * Creates new {@link Department} by special name. Also links this
     * new department with special subjects.
     *
     * @param ids array of IDs of {@link Subject}.
     * @param name department name.
     * @return just created {@link Department} instance.
     */
    @PreAuthorize("hasAuthority('departmetns:write')")
    @PostMapping(value = "/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Model> save(@RequestParam(value="array[]") Long[] ids, @PathVariable("name") String name) {
        if (name == null || name.isEmpty() || ids.length < 1) {
            log.info("IN save: the name argument is empty or array of id's is empty.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Department department = new Department(name);
        List<Model> subjects = this.subjectService.getListById(ids);
        if (subjects == null || subjects.isEmpty()) {
            log.info("IN save: subjects with id's: {} is not exist.", Arrays.toString(ids));
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        ((DepartmentService)this.departmentService).saveWithSubject(department, subjects);
        Model result = ((DepartmentService)this.departmentService).getByName(name);
        if (result != null && result.getId() > 0) {
           return new ResponseEntity<>(department, HttpStatus.OK);
        }
        log.info("IN save: bad request.");
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles the case if you try save department {@link Department}
     * with empty name or name that equals null.
     *
     * @return BAD REQUEST.
     */
    @PreAuthorize("hasAuthority('departmetns:write')")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Model> saveEmptyName() {
        log.info("IN saveEmptyName: the department name can not be empty or equals null!");
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    /**
     * Returns special {@link Department} instance, by ID.
     *
     * @param ID department ID (primary key).
     * @return special {@link Department}.
     */
    @PreAuthorize("hasAuthority('departments:read')")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Model> getById(@PathVariable("id") Long ID) {
        if (ID == null|| ID < 1L) {
            log.info("IN getById: ID is NULL or less then 1.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Model department = this.departmentService.getById(ID);
        if (department != null) {
            return new ResponseEntity<>(department, HttpStatus.OK);
        }
        log.info("IN getById: department with id: {} not found.", ID);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * Returns HTTP status of removing process of {@link Department} by their ID.
     *
     * @param id array of departments ID's.
     * @return {@link HttpStatus}.
     */
    @PreAuthorize("hasAuthority('departmetns:write')")
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Model> deleteById(@PathVariable("id") Long id) {
        if (id == null || id < 1) {
            log.info("IN deleteById: ID is NULL or less then 1.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (this.departmentService.deleteById(id) > 0) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            log.info("IN deleteById: deleting department with Id: {} is impossible.", id);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}