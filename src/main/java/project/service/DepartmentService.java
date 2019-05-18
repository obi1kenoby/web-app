package project.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import project.model.Department;
import project.model.Model;
import project.model.Subject;
import project.repository.ModelRepository;


import java.util.*;

/**
 * Implementation of {@link Service} interface.
 *
 * @author Alexander Naumov.
 */
@Slf4j
public class DepartmentService implements Service {

    @Autowired
    private ModelRepository repository;

    @Override
    public List<Model> getAll() {
        List<Model> models = null;
        try {
            Optional o = repository.getList(Department.class);
            models = (List<Model>) o.get();
            models.forEach(m -> log.info("IN getAll, department: {} successfully loaded", m));
        } catch (Exception e) {
            log.error("IN getAll, error while loading.");
        }
        return models;
    }

    public List<Model> getListById(Long[] ids) {
        List<Model> models = null;
        try {
            Optional o = repository.getListById(Department.class, ids);
            models = (List<Model>) o.get();
            models.forEach(m -> log.info("IN getListById, departments with ids: {} successfully loaded", ids));
        } catch (Exception e) {
            log.error("IN getListById, departments with ids: {} error while loading.", ids);
        }
        return models;
    }

    @Override
    public Model getById(Long id) {
        Model model = null;
        Optional o;
        try {
            o = repository.getById(Department.class, id);
            model = (Model) o.get();
            log.info("IN getById, department with id: {} successfully loaded.", id);
        } catch (Exception e) {
            log.error("IN getById, department with id: {} error while loading.", id);
        }
        return model;
    }

    public boolean saveWithSubject(Department department, List<Model> subjects) {
        try {
            subjects.forEach(m -> {
                ((Subject) m).getDepartments().add(department);
                repository.saveOrUpdate(m);
                log.info("IN saveWithSubject, department: {} successfully saved.", m);
            });
            return true;
        } catch (Exception e) {
            log.error("IN saveWithSubject, department: {} error while saving.", department);
        }
        return false;
    }

    @Override
    public boolean save(Model model) {
        try {
            repository.saveOrUpdate(model);
            log.info("IN save, department: {} successfully saved.", model);
            return true;
        } catch (Exception e) {
            log.error("IN save, department: {} error while saving.", model);
        }
        return false;
    }

    @Override
    public int deleteById(Long id) {
        int res = 0;
        try{
            res = repository.deleteById(Department.class, id);
            log.info("IN deleteById, department with id: {} successfully removed.", id);
        } catch (Exception e) {
            log.error("IN deleteById, department with id: {} error while removing.", id);
        }
        return res;
    }

    public Model getByName(String name) {
        Model model = null;
        try {
            Optional o = repository.getDepByName(name);
            model = (Model) o.get();
            log.info("IN getByName, department with name: {} successfully loaded.", name);
        } catch (Exception e) {
            log.error("IN getByName, department with name: {} error while loading.", name);
        }
        return model;
    }
}