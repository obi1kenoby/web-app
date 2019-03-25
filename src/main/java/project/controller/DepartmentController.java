package project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.model.Model;
import project.model.Subject;
import project.model.Department;
import project.service.DepartmentService;
import project.service.Service;
import project.service.SubjectService;

import java.util.List;


/**
 * REST controller class, that handles requests
 * for {@link Department} entities.
 *
 * @author Alexander Naumov.
 */
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
    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<Model>> list() {
        List<Model> departments = this.departmentService.getAll();
        if (departments.isEmpty()) {
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
    @RequestMapping(value = "/{name}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Model> save(@RequestParam(value="array[]") Long[] ids, @PathVariable("name") String name){
        if (name == null || name.isEmpty() || ids.length < 1) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Department department = new Department(name);
        List<Model> subjects = ((SubjectService)this.subjectService).getListById(ids);
        if (subjects == null || subjects.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        ((DepartmentService)this.departmentService).saveWithSubject(department, subjects);
        Model result = ((DepartmentService)this.departmentService).getByName(name);
        if (result != null && result.getId() > 0) {
           return new ResponseEntity<>(department, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    /**
     * Returns special {@link Department} instance, by ID.
     *
     * @param ID department ID (primary key).
     * @return special {@link Department}.
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Model> getById(@PathVariable("id") Long ID) {
        if (ID == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Model department = this.departmentService.getById(ID);
        if (department != null) {
            return new ResponseEntity<>(department, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * Returns HTTP status of removing process of {@link Department} by their ID.
     *
     * @param id array of departments ID's.
     * @return {@link HttpStatus}.
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Model> delete(@PathVariable("id") Long id) {
        if (id == null || id == 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (this.departmentService.deleteById(id)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}