package project.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import project.model.Model;
import project.model.Student;
import project.repository.ModelRepository;


import java.util.*;

/**
 * Implementation of {@link Service} interface.
 *
 * @author Alexander Naumov.
 */
@Slf4j
public class StudentService implements Service {

    @Autowired
    private ModelRepository repository;


    @Override
    public List<Model> getAll() {
        List<Model> models = null;
        try {
            Optional o = repository.getList(Student.class);
            models = (List<Model>) o.get();
            log.info("IN getAll, all " + models.size() + " students were successfully loaded.");
        } catch (Exception e) {
            log.error("IN getAll, error while loading.");
        }
        return models;
    }

    @Override
    public Model getById(Long id) {
        Model model = null;
        try {
            Optional o = repository.getById(Student.class, id);
            model = (Model) o.get();
            log.info("IN getById, student with id: {} was successfully loaded.", id);
        } catch (Exception e) {
            log.error("IN getById, student with id: {} was error while loaded.", id);
        }
        return model;
    }

    @Override
    public boolean save(Model model) {
        try {
            repository.saveOrUpdate(model);
            log.info("IN save, subject: {} was successfully saved.", model);
            return true;
        } catch (Exception e) {
            log.error("IN save, subject: {} error while saving.", model);
        }
        return false;
    }

    @Override
    public int deleteById(Long id) {
        int res = 0;
        try {
            res = repository.deleteById(Student.class, id);
            log.info("IN deleteById, student with id: {} was successfully removed.", id);
        } catch (Exception e) {
            log.error("IN deleteById, student with id: {} error while removing.", id);
        }
        return res;
    }
}