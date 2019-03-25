package project.service;

import org.springframework.beans.factory.annotation.Autowired;
import project.model.Department;
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
public class DepartmentService implements Service {

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
            Optional optional = repository.getList(Department.class);
            if (optional.isPresent()) {
                cache = new ArrayList<>((List<Model>) optional.get());
            }
        }
        return cache;
    }

    public List<Model> getListById(Long[] ids) {
        List<Model> results = cache.stream()
                .filter(model -> Arrays.asList(ids)
                        .contains(model.getId())).collect(Collectors.toList());
        if (results.size() == ids.length) {
            return results;
        }
        Optional optional = repository.getListById(Department.class, ids);
        if (optional.isPresent()) {
            List<Model> models = (List<Model>) optional.get();
            cache.stream().filter(model -> !cache.contains(model)).forEach(model -> cache.add(model));
            return models;
        }
        return null;
    }

    @Override
    public Model getById(Long id) {
        for (Model model : cache) {
            if (model.getId() == id) {
                return model;
            }
        }
        Optional optional = repository.getById(Department.class, id);
        if (optional.isPresent()) {
            Model dep = (Model) optional.get();
            cache.add(dep);
            return dep;
        }
        return null;
    }

    public void saveWithSubject(Department department, List<Model> subjects) {
        if (!cache.contains(subjects)) {
            subjects.forEach(model -> {
                ((Subject) model).getDepartments().add(department);
                repository.saveOrUpdate(model);
            });
            cache.add(department);
        }
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

    @Override
    public boolean deleteById(Long id) {
        try{
            if (!cache.isEmpty()) {
                Optional<Model> o = cache.stream().filter(d -> d.getId() == id).findAny();
                if (o.isPresent()) {
                    cache.remove(o.get());
                }
            }
            repository.deleteByid(Department.class, id);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public Model getByName(String name) {
        for (Model model : cache) {
            if (((Department) model).getName().equals(name)) {
                return model;
            }
        }
        Optional optional = repository.getDepByName(name);
        if (optional.isPresent()) {
            Model model = (Model) optional.get();
            cache.add(model);
            return model;
        }
        return null;
    }
}
