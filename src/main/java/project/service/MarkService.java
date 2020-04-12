package project.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import project.model.Mark;
import project.model.Model;
import project.repository.ModelRepository;
import project.repository.ModelRepositoryImpl;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Implementation of {@link Service} interface for mark {@link Mark}.
 *
 * @author Alexander Naumov.
 */
@Slf4j
public class MarkService implements Service {

    @Autowired
    private ModelRepository repository;

    /**
     * Implementation of {@link Service#getAll()}.
     *
     * @return list of marks {@link List<Mark>}.
     */
    @Override
    public List<Model> getAll() {
        List<Model> models = null;
        try {
            Optional<List<Model>> o = repository.getList(Mark.class);
            if (o.isPresent()) {
                models = o.get();
            }
            log.info("IN getAll, all marks successfully loaded.");
        } catch (Exception e) {
            log.error("IN getAll, error while loading.");
        }
        return models;
    }

    /**
     * Implementation of {@link Service#getListById(Long[])}.
     *
     * @param ids of marks {@link Mark}.
     * @return list of marks {@link List<Mark>}.
     */
    @Override
    public List<Model> getListById(Long[] ids) {
        List<Model> models = null;
        try {
            Optional<List<Model>> o = repository.getListById(Mark.class, ids);
            if (o.isPresent()) {
                models = o.get();
                log.info("IN getListById, all marks with ids: {}, were successfully loaded.", Arrays.toString(ids));
            }
        } catch (Exception e) {
            log.error("IN getListById, error while loading marks with ids: {}", Arrays.toString(ids));
        }
        return models;
    }

    /**
     * Implementation of {@link Service#getById(Long)}.
     *
     * @param id of mark {@link Mark}.
     * @return unique mark {@link Mark}.
     */
    @Override
    public Model getById(Long id) {
        Model model = null;
        try {
            Optional<Model> o = repository.getById(Mark.class, id);
            if (o.isPresent()) {
                model = o.get();
                log.info("IN getById, mark with id: {} successfully loaded.", id);
            }
        } catch (Exception e) {
            log.error("IN getById, mark with id: {} error while loaded.", id);
        }
        return model;
    }

    /**
     * Implementation of {@link Service#deleteById(Long)}.
     *
     * @param id of mark {@link Model}.
     * @return boolean result of deleting.
     */
    @Override
    public int deleteById(Long id) {
        int res = 0;
        try {
            res = repository.deleteById(Mark.class, id);
            log.info("IN deleteById, mark with id: {} successfully removed.", id);
        } catch (Exception e) {
            log.error("IN deleteById, mark with id: {} error while removing.", id);
        }
        return res;
    }

    /**
     * Implementation of {@link Service#save(Model)}.
     *
     * @param model for saving {@link Mark}.
     * @return boolean result of saving.
     */
    @Override
    public boolean save(Model model) {
        try {
            repository.saveOrUpdate(model);
            log.info("IN save, mark: {} successfully saved.", model);
        } catch (Exception e) {
            log.error("IN save, mark: {} error while saving.", model);
            return false;
        }
        return true;
    }

    /**
     * Get list of all marks {@link Mark} whose date falls within the month range which is
     * formed by the specified date.
     *
     * @param date of month {@link LocalDate}.
     * @return list of marks {@link List<Mark>}.
     */
    public List<Model> getMarksByDateRange(LocalDate date) {
        LocalDate start = date.withDayOfMonth(1);
        LocalDate end = date.withDayOfMonth(date.lengthOfMonth());
        Predicate<Mark> filter = m -> m.getDate().isAfter(start) && m.getDate().isBefore(end);
        List<Model> marks = null;
        try {
            marks = getAll().stream()
                    .filter(m -> filter.test((Mark) m))
                    .map(m -> (Mark) m).collect(Collectors.toList());
            log.info("IN getMarksByDateRange, all marks between {} and {}, successfully loaded.", start, end);
        } catch (Exception e) {
            log.error("IN getMarksByDateRange, error while loading.");
        }
        return marks;
    }
}
