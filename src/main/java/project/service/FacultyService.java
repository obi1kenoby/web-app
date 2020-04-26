package project.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import project.model.Faculty;
import project.model.Model;
import project.model.Subject;
import project.repository.ModelRepository;

import java.util.*;


/**
 * Implementation of {@link Service} interface for departments.
 *
 * @author Alexander Naumov.
 */
@Slf4j
public class FacultyService implements Service {

    @Autowired
    private ModelRepository repository;

    /**
     * Implementation of {@link Service#getAll()}.
     *
     * @return list of departments {@link List< Faculty >}.
     */
    @Override
    public List<Model> getAll() {
        List<Model> models = null;
        try {
            Optional<List<Model>> o = repository.getList(Faculty.class);
            if (o.isPresent()) {
                models = o.get();
                models.forEach(m -> log.info("IN getAll, department: {} successfully loaded", m));
            }
        } catch (Exception e) {
            log.error("IN getAll, error while loading.");
        }
        return models;
    }

    /**
     * Implementation of {@link Service#getListById(Long[])}.
     *
     * @param ids {@link Long[]} of departments {@link Faculty}.
     * @return list of departments {@link List< Faculty >}.
     */
    @Override
    public List<Model> getListById(Long[] ids) {
        List<Model> models = null;
        try {
            Optional<List<Model>> o = repository.getListById(Faculty.class, ids);
            if (o.isPresent()) {
                models = o.get();
                models.forEach(m -> log.info("IN getListById, departments with ids: {} successfully loaded", Arrays.toString(ids)));
            }
        } catch (Exception e) {
            log.error("IN getListById, departments with ids: {} error while loading.", Arrays.toString(ids));
        }
        return models;
    }

    /**
     * Implementation of {@link Service#getById(Long)}.
     *
     * @param id of {@link Faculty}.
     * @return instance of {@link Faculty}.
     */
    @Override
    public Model getById(Long id) {
        Model model = null;
        try {
            Optional<Model> o = repository.getById(Faculty.class, id);
            if (o.isPresent()) {
                model = o.get();
                log.info("IN getById, department with id: {} successfully loaded.", id);
            }
        } catch (Exception e) {
            log.error("IN getById, department with id: {} error while loading.", id);
        }
        return model;
    }

    /**
     * Save department {@link Faculty} with subjects {@link Subject}.
     *
     * @param faculty for saving.
     * @param subjects of department.
     * @return boolean result.
     */
    public boolean saveWithSubject(Faculty faculty, List<Model> subjects) {
        try {
            subjects.forEach(m -> {
                ((Subject) m).getFaculties().add(faculty);
                repository.saveOrUpdate(m);
                log.info("IN saveWithSubject, department: {} successfully saved.", m);
            });
            return true;
        } catch (Exception e) {
            log.error("IN saveWithSubject, department: {} error while saving.", faculty);
        }
        return false;
    }

    /**
     * Implementation of {@link Service#save(Model)}.
     *
     * @param model {@link Faculty}.
     * @return boolean result.
     */
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

    /**
     * Implementation of {@link Service#deleteById(Long)}.
     *
     * @param id of {@link Faculty}.
     * @return int result.
     */
    @Override
    public int deleteById(Long id) {
        int res = 0;
        try{
            res = repository.deleteById(Faculty.class, id);
            log.info("IN deleteById, department with id: {} successfully removed.", id);
        } catch (Exception e) {
            log.error("IN deleteById, department with id: {} error while removing.", id);
        }
        return res;
    }

    /**
     * Get instance of {@link Faculty} by their name.
     *
     * @param name of department.
     * @return instance of {@link Faculty}.
     */
    public Model getByName(String name) {
        Model model = null;
        try {
            Optional<Model> o = repository.getFacByName(name);
            if (o.isPresent()) {
                model = o.get();
                log.info("IN getByName, department with name: {} successfully loaded.", name);
            }
        } catch (Exception e) {
            log.error("IN getByName, department with name: {} error while loading.", name);
        }
        return model;
    }
}