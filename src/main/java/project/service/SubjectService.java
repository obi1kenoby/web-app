package project.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import project.model.Model;
import project.model.Subject;
import project.repository.ModelRepository;

import java.util.*;


/**
 * Implementation of {@link Service} interface for subject {@link Subject}.
 *
 * @author Alexander Naumov.
 */
@Slf4j
public class SubjectService implements Service {

    @Autowired
    private ModelRepository repository;

    /**
     * Implementation of {@link Service#getAll}
     *
     * @return list of subjects {@link List<Subject>}.
     */
    @Override
    public List<Model> getAll() {
        List<Model> models = null;
        try {
            Optional<List<Model>> o = repository.getList(Subject.class);
            if (o.isPresent()) {
                models = o.get();
                log.info("IN getAll, all subjects were successfully loaded.");
            }
        } catch (Exception e) {
            log.error("IN getAll, error while loading.");
        }
        return models;
    }

    /**
     * Implementation of  {@link Service#getListById(Long[])}.
     *
     * @param ids of subjects {@link Subject}.
     * @return list of subjects {@link List<Subject>}.
     */
    public List<Model> getListById(Long[] ids) {
        List<Model> models = null;
        try {
            Optional<List<Model>> o = repository.getListById(Subject.class, ids);
            if (o.isPresent()) {
                models = o.get();
                log.info("IN getListById, subjects with ids:{} successfully loaded.", Arrays.toString(ids));
            }
        } catch (Exception e) {
            log.error("IN getListById, subjects with ids:{} error while loading.", Arrays.toString(ids));
        }
        return models;
    }

    /**
     * Implementation of {@link Service#getById(Long)}.
     *
     * @param id of subject {@link Subject}.
     * @return unique subject {@link Subject}.
     */
    @Override
    public Model getById(Long id) {
        Model model = null;
        try {
            Optional<Model> o = repository.getById(Subject.class, id);
            if (o.isPresent()) {
                model = o.get();
                log.info("IN getById, subject with id:{} successfully loaded.", id);
            }
        } catch (Exception e) {
            log.error("IN getById, subject with id:{} error while loading.", id);
        }
        return model;
    }

    /**
     * Implementation of {@link Service#deleteById(Long)}.
     *
     * @param id of {@link Subject}.
     * @return int result.
     */
    @Override
    public int deleteById(Long id) {
        int res = 0;
        try {
           res = repository.deleteById(Subject.class, id);
           log.info("IN deleteById, subject with id:{} successfully removed.", id);
        } catch (Exception e) {
            log.error("IN deleteById, subject with id:{} error while removing.", id);
        }
        return res;
    }

    /**
     * Implementation of {@link Service#save(Model)}.
     *
     * @param model for saving {@link Subject}.
     * @return boolean result.
     */
    @Override
    public boolean save(Model model) {
        try {
            repository.saveOrUpdate(model);
            log.info("IN save, subject:{} successfully loaded.", model);
        } catch (Exception e) {
            log.error("IN save, subject:{} error while saving.", model);
            return false;
        }
        return true;
    }

    /**
     * Get instance of subject {@link Subject} by their name.
     *
     * @param name of subject {@link Subject}.
     * @return instance of model {@link Subject}.
     */
    public Model getByName(String name) {
        Model model = null;
        try {
            Optional<Model> o = repository.getSubjectByName(name);
            if (o.isPresent()) {
                model = o.get();
                log.info("IN getByName, subject with name:{} successfully loaded.", name);
            }
        } catch (Exception e) {
            log.error("IN getByName, subject with name:{} error while loading.", name);
        }
        return model;
    }
}
