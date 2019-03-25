package project.service;

import org.springframework.beans.factory.annotation.Autowired;
import project.model.Mark;
import project.model.Model;
import project.repository.ModelRepository;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of {@link Service} interface.
 *
 * @author Alexander Naumov.
 */
public class MarkService implements Service {

    private List<Model> cache;

    @Autowired
    private ModelRepository repository;

    @PostConstruct
    public void init() {
        cache = new ArrayList<>();
        cache = getAll();
    }

    @Override
    public List<Model> getAll() {
        if (cache.isEmpty()) {
            Optional optional = repository.getList(Mark.class);
            if (optional.isPresent()) {
                cache = new ArrayList<>((List<Model>) optional.get());
            }
        }
        return cache;
    }

    @Override
    public Model getById(Long id) {
        if (!cache.isEmpty()) {
            Optional<Model> o = cache.stream().filter(m -> m.getId() == id).findAny();
            if (o.isPresent()) {
                return o.get();
            } else {
                Optional op = repository.getById(Mark.class, id);
                if (op.isPresent()) {
                    cache.add((Model) op.get());
                }
                return (Model) op.get();
            }
        } else {
            Optional op = repository.getById(Mark.class, id);
            if (op.isPresent()) {
                cache.add((Model) op.get());
            }
            return (Model) op.get();
        }
    }

    @Override
    public boolean deleteById(Long id) {
        if (!cache.isEmpty()) {
            Optional<Boolean> o = cache.stream().filter(s -> s.getId() == id).map(s -> cache.remove(s)).findAny();
            if (o.isPresent() && o.get()) {
                try {
                    repository.deleteByid(Mark.class, id);
                } catch (Exception e) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean save(Model model) {
        if (model.getId() != 0) {
            Optional<Model> o = cache.stream().filter(m -> m.getId() == model.getId()).findAny();
            if (o.isPresent()) {
                cache.remove(o.get());
                cache.add(model);
            }
        }
        try {
            repository.saveOrUpdate(model);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public List<Model> getMarksByDateRange(LocalDate date) {
        LocalDate start = date.withDayOfMonth(1);
        LocalDate end = date.withDayOfMonth(date.lengthOfMonth());
        return getAll().stream()
                .filter(m -> ((Mark)m).getDate().isAfter(start) && ((Mark)m).getDate().isBefore(end))
                .map(m ->(Mark)m).collect(Collectors.toList());
    }
}
