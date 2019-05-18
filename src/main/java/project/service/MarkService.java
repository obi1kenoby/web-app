package project.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import project.model.Mark;
import project.model.Model;
import project.repository.ModelRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Implementation of {@link Service} interface.
 *
 * @author Alexander Naumov.
 */
@Slf4j
public class MarkService implements Service {

    @Autowired
    private ModelRepository repository;

    @Override
    public List<Model> getAll() {
        Optional o;
        List<Model> models = null;
        try {
            o = repository.getList(Mark.class);
            models = (List<Model>) o.get();
            log.info("IN getAll, all marks successfully loaded.");
        } catch (Exception e) {
            log.error("IN getAll, error while loading.");
        }
        return models;
    }

    @Override
    public Model getById(Long id) {
        Optional o;
        Model model = null;
        try {
            o = repository.getById(Mark.class, id);
            model = (Model) o.get();
            log.info("IN getById, mark with id: {} successfully loaded.", id);
        } catch (Exception e) {
            log.error("IN getById, mark with id: {} error while loaded.", id);
        }
        return model;
    }

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
