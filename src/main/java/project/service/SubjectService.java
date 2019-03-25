package project.service;

import org.springframework.beans.factory.annotation.Autowired;
import project.model.Model;
import project.model.Subject;
import project.repository.ModelRepository;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implimentation of {@link Service} interface.
 *
 * @author Alexander Naumov.
 */
public class SubjectService implements Service {

    private List<Model> cache;

    @Autowired
    private ModelRepository repository;

    @PostConstruct
    private void init() {
        cache = new ArrayList<>();
        cache = getAll();
    }

    @Override
    public List<Model> getAll() {
        if (cache.isEmpty()) {
            Optional optional = repository.getList(Subject.class);
            if (optional.isPresent()) {
                cache = new ArrayList<>((List<Model>) optional.get());
            }

        }
        return cache;
    }

    public List<Model> getListById(Long[] ids) {
        List<Model> models = new ArrayList<>();
        if (!cache.isEmpty()) {
            for (Long id : ids) {
                Optional<Model> o = cache.stream().filter(s -> s.getId() == id).findAny();
                if (o.isPresent()) {
                    models.add(o.get());
                } else {
                    Optional op = repository.getById(Subject.class, id);
                    if (op.isPresent()) models.add((Model) op.get());
                }
            }
        } else {
            models.addAll(Arrays.stream(ids).map(id -> repository.getById(Subject.class, id)).filter(Optional::isPresent).map(o -> (Subject) o.get()).collect(Collectors.toList()));
        }
        return models;
    }

    @Override
    public Model getById(Long id) {
        if (!cache.isEmpty()) {
            Optional<Model> o = cache.stream().filter(s -> s.getId() == id).findAny();
            if (o.isPresent()) {
                return o.get();
            } else {
                Optional<Model> op = repository.getById(Subject.class, id);
                if (op.isPresent()) {
                    cache.add(op.get());
                }
                return op.get();
            }
        } else {
            Optional<Model> op = repository.getById(Subject.class, id);
            if (op.isPresent()) {
                cache.add(op.get());
            }
            return op.get();
        }
    }

    @Override
    public boolean deleteById(Long id) {
        if (!cache.isEmpty()) {
            Optional<Boolean> o = cache.stream().filter(s -> s.getId() == id).map(s -> cache.remove(s)).findAny();
            if (o.isPresent() && o.get()) {
                try {
                    repository.deleteByid(Subject.class, id);
                } catch (Exception e) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean save(Model model) {
        try {
            repository.saveOrUpdate(model);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public Model getByName(String name) {
        if (!cache.isEmpty()) {
            Optional<Model> op = cache.stream().filter(s -> ((Subject) s).getName().equals(name)).findAny();
            if (op.isPresent()) {
                return op.get();
            } else {
                Optional<Model> o = repository.getSubjectByName(name);
                o.ifPresent(model -> cache.add(model));
                return o.get();
            }
        } else {
            Optional<Model> o = repository.getSubjectByName(name);
            o.ifPresent(model -> cache.add(model));
            return o.get();
        }
    }
}
