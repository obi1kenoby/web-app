package project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.model.Model;
import project.model.Subject;
import project.model.Department;
import project.service.DepartmentService;
import project.service.SubjectService;

import java.util.List;
import java.util.Optional;
import java.util.Set;


/**
 * REST Controller class, that handles requests
 * for {@link Department} entities.
 *
 * @author Alexander Naumov.
 */
@RestController
@RequestMapping("/api/department")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private SubjectService subjectService;

    /**
     * Returns all {@link Department} instances, sorted by name.
     *
     * @return set of departments.
     */
    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Set<Model>> list() {
        Set<Model> departments = departmentService.getAll();
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
        Optional<List<Model>> optional = this.subjectService.getListById(ids);
        if (!optional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<Model> subjects = optional.get();
        this.departmentService.save(department, subjects);
        Model result = departmentService.getByName(name);
        if (result != null) {
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
    @RequestMapping(value = "{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
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
}