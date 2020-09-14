package project.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import project.model.Model;
import project.model.Student;
import project.repository.ModelRepository;

import java.util.*;


/**
 * Implementation of {@link Service} interface for student {@link Student}.
 *
 * @author Alexander Naumov.
 */
@Slf4j
public class StudentService implements Service {

    @Autowired
    private ModelRepository repository;

    /**
     * Implementation of {@link Service#getAll()}.
     *
     * @return list of students {@link Student}.
     */
    @Override
    public List<Model> getAll() {
        List<Model> models = null;
        try {
            Optional<List<Model>> o = repository.getList(Student.class);
            if (o.isPresent()) {
                models = o.get();
                log.info("IN getAll, all {} students were successfully loaded.", models.size());
            }
        } catch (Exception e) {
            log.error("IN getAll, error while loading.");
        }
        return models;
    }

    /**
     * Implementation of {@link Service#getListById(Long[])}.
     *
     * @param ids of students {@link Student}.
     * @return list of students {@link Student}.
     */
    @Override
    public List<Model> getListById(Long[] ids) {
        List<Model> models = null;
        try {
            Optional<List<Model>> o = repository.getListById(Student.class, ids);
            if (o.isPresent()) {
                models = o.get();
                log.info("IN getListById, students with ids: {}, were successfully loaded.", Arrays.toString(ids));
            }
        } catch (Exception e) {
            log.error("IN getListById, error while loading students with ids: {}", Arrays.toString(ids));
        }
        return models;
    }

    /**
     * Implementation of {@link Service#getById(Long)}.
     *
     * @param id of student {@link Student}.
     * @return unique student {@link Student}.
     */
    @Override
    public Model getById(Long id) {
        Model model = null;
        try {
            Optional<Model> o = repository.getById(Student.class, id);
            if (o.isPresent()) {
                model = o.get();
                log.info("IN getById, student with id: {} was successfully loaded.", id);
            }
        } catch (Exception e) {
            log.error("IN getById, student with id: {} was error while loaded.", id);
        }
        return model;
    }

    /**
     * Implementation of {@link Service#save(Model)}.
     *
     * @param model student for saving {@link Student}.
     * @return boolean result of saving.
     */
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

    /**
     * Implementation of {@link Service#deleteById(Long)}.
     *
     * @param id of student {@link Student}.
     * @return boolean result of deleting.
     */
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