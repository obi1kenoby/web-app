package project.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import project.model.Grade;
import project.model.Model;
import project.repository.ModelRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Implementation of {@link Service} interface for grade {@link Grade}.
 *
 * @author Alexander Naumov.
 */
@Slf4j
public class GradeService implements Service {

    @Autowired
    private ModelRepository repository;

    /**
     * Implementation of {@link Service#getAll()}.
     *
     * @return list of grades {@link List<Grade>}.
     */
    @Override
    public List<Model> getAll() {
        List<Model> models = null;
        try {
            Optional<List<Model>> o = repository.getList(Grade.class);
            if (o.isPresent()) {
                models = o.get();
            }
            log.info("IN getAll, all grades successfully loaded.");
        } catch (Exception e) {
            log.error("IN getAll, error while loading.");
        }
        return models;
    }

    /**
     * Implementation of {@link Service#getListById(Long[])}.
     *
     * @param ids of grades {@link Grade}.
     * @return list of grades {@link List< Grade >}.
     */
    @Override
    public List<Model> getListById(Long[] ids) {
        List<Model> models = null;
        try {
            Optional<List<Model>> o = repository.getListById(Grade.class, ids);
            if (o.isPresent()) {
                models = o.get();
                log.info("IN getListById, all grades with ids: {}, were successfully loaded.", Arrays.toString(ids));
            }
        } catch (Exception e) {
            log.error("IN getListById, error while loading grades with ids: {}", Arrays.toString(ids));
        }
        return models;
    }

    /**
     * Implementation of {@link Service#getById(Long)}.
     *
     * @param id of grade {@link Grade}.
     * @return unique grade {@link Grade}.
     */
    @Override
    public Model getById(Long id) {
        Model model = null;
        try {
            Optional<Model> o = repository.getById(Grade.class, id);
            if (o.isPresent()) {
                model = o.get();
                log.info("IN getById, grade with id: {} successfully loaded.", id);
            }
        } catch (Exception e) {
            log.error("IN getById, grade with id: {} error while loaded.", id);
        }
        return model;
    }

    /**
     * Implementation of {@link Service#deleteById(Long)}.
     *
     * @param id of grade {@link Model}.
     * @return boolean result of deleting.
     */
    @Override
    public int deleteById(Long id) {
        int res = 0;
        try {
            res = repository.deleteById(Grade.class, id);
            log.info("IN deleteById, grade with id: {} successfully removed.", id);
        } catch (Exception e) {
            log.error("IN deleteById, grade with id: {} error while removing.", id);
        }
        return res;
    }

    /**
     * Implementation of {@link Service#save(Model)}.
     *
     * @param model for saving {@link Grade}.
     * @return boolean result of saving.
     */
    @Override
    public boolean save(Model model) {
        try {
            repository.saveOrUpdate(model);
            log.info("IN save, grade: {} successfully saved.", model);
        } catch (Exception e) {
            log.error("IN save, grade: {} error while saving.", model);
            return false;
        }
        return true;
    }

    /**
     * Get list of all grades {@link Grade} whose date falls within the month range which is
     * formed by the specified date.
     *
     * @param date of month {@link LocalDate}.
     * @return list of grades {@link List< Grade >}.
     */
    public List<Model> getGradesByDateRange(LocalDate date) {
        LocalDate start = date.withDayOfMonth(1);
        LocalDate end = date.withDayOfMonth(date.lengthOfMonth());
        Predicate<Grade> filter = m -> m.getDate().isAfter(start) && m.getDate().isBefore(end);
        List<Model> grades = null;
        try {
            grades = getAll().stream()
                    .filter(m -> filter.test((Grade) m))
                    .map(m -> (Grade) m).collect(Collectors.toList());
            log.info("IN getGradesByDateRange, all grades between {} and {}, successfully loaded.", start, end);
        } catch (Exception e) {
            log.error("IN getGradesByDateRange, error while loading.");
        }
        return grades;
    }
}