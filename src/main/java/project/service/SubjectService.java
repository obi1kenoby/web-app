package project.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
public class SubjectService implements Service {

    @Autowired
    private ModelRepository repository;

    @Override
    public List<Model> getAll() {
        List<Model> models = null;
        try {
            Optional o = repository.getList(Subject.class);
            models = (List<Model>) o.get();
            log.info("IN getAll, all subjects were successfully loaded.");
        } catch (Exception e) {
            log.error("IN getAll, error while loading.");
        }
        return models;
    }

    public List<Model> getListById(Long[] ids) {
        List<Model> models = null;
        try {
            Optional o = repository.getListById(Subject.class, ids);
            models = (List<Model>) o.get();
            log.info("IN getListById, subjects with ids:{} successfully loaded.", ids);
        } catch (Exception e) {
            log.error("IN getListById, subjects with ids:{} error while loading.", ids);
        }
        return models;
    }

    @Override
    public Model getById(Long id) {
        Model model = null;
        try {
            Optional o = repository.getById(Subject.class, id);
            model = (Model) o.get();
            log.info("IN getById, subject with id:{} successfully loaded.", id);
        } catch (Exception e) {
            log.error("IN getById, subject with id:{} error while loading.", id);
        }
        return model;
    }

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

    public Model getByName(String name) {
        Model model = null;
        try {
            Optional o = repository.getSubjectByName(name);
            model = (Model) o.get();
            log.info("IN getByName, subject with name:{} successfully loaded.");
        } catch (Exception e) {
            log.error("IN getByName, subject with name:{} error while loading.");
        }
        return model;
    }
}
