package project.service;

import org.springframework.beans.factory.annotation.Autowired;
import project.model.Model;
import project.model.Student;
import project.repository.ModelRepository;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implimentation of {@link Service} interface.
 *
 * @author Alexander Naumov.
 */
public class StudentService implements Service {

    @Autowired
    private ModelRepository repository;

    private List<Model> cache;

    @PostConstruct
    private void init() {
        cache = new ArrayList<>();
        cache = getAll();
    }

    @Override
    public List<Model> getAll() {
        if (cache.isEmpty()) {
            Optional optional = repository.getList(Student.class);
            if (optional.isPresent()) {
                cache = new ArrayList<>((List<Model>) optional.get());
            }
        }
        return cache;
    }

    @Override
    public Model getById(Long id) {
        Optional<Model> optional = cache.stream().filter(stundet -> stundet.getId() == id).findAny();
        if (optional.isPresent()) {
            return optional.get();
        } else {
            Optional o = repository.getById(Student.class, id);
            if (optional.isPresent()) {
                Model model = (Model) o.get();
                cache.add(model);
                return model;
            }
        }
        return null;
    }

    @Override
    public boolean save(Model model) {
        try {
            repository.saveOrUpdate(model);
            cache.add(model);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean deleteById(Long id) {
        if (!cache.isEmpty()) {
            Optional<Boolean> o = cache.stream().filter(s -> s.getId() == id).map(s -> cache.remove(s)).findAny();
            if (o.isPresent() && o.get()) {
                try {
                    repository.deleteByid(Student.class, id);
                } catch (Exception e) {
                    return false;
                }
            }
        }
        return true;
    }
}
