package project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import project.model.Faculty;
import project.model.Model;
import project.model.Subject;
import project.service.FacultyService;
import project.service.Service;
import project.service.SubjectService;

import java.util.List;

/**
 * REST Controller class, that handles requests
 * for {@link Subject} entities.
 *
 * @author Alexander Naumov.
 */
@Controller
@RequestMapping("/api/subject")
public class SubjectController {

    @Autowired
    @Qualifier("subjectService")
    private Service subjectService;

    @Autowired
    @Qualifier("facultyService")
    private Service facultyService;

    /**
     * Returns list of {@link Subject} as JSON format.
     *
     * @return set of {@link ResponseEntity}.
     */
    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Model>> list() {
        List<Model> models = this.subjectService.getAll();
        if (models == null || models.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(models, HttpStatus.OK);
    }

    /**
     * Returns special {@link Subject} instance by ID.
     *
     * @param id department ID (primary key).
     * @return {@link ResponseEntity}.
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Model> getByid(@PathVariable("id")Long id) {
        Model model = this.subjectService.getById(id);
        if (id == null || id == 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (model != null) {
            return new ResponseEntity<>(model, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * Returns HTTP status of removing process of {@link Subject} by their ID.
     *
     * @param id array of subjects ID's.
     * @return {@link HttpStatus}.
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Model> delete(@PathVariable("id")Long id) {
        if (id == 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (this.subjectService.deleteById(id) > 0) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    /**
     * Creates new {@link Subject} by special name. Also links this
     * new subject with special departments.
     *
     * @param ids array of IDs of {@link Faculty}.
     * @param name subject name.
     * @return just created {@link Subject} instance.
     */
    @RequestMapping(value = "/{name}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Model> save(@PathVariable("name")String name, @RequestParam("ids")Long[] ids) {
        if (name == null || name.isEmpty() || ids.length < 1) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Subject subject = new Subject(name);
        List<Model> departments = ((FacultyService)this.facultyService).getListById(ids);
        if (departments.size() != ids.length) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        departments.forEach(d -> subject.getFaculties().add((Faculty)d));
        this.subjectService.save(subject);
        Model model = ((SubjectService)this.subjectService).getByName(name);
        if (model != null) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}