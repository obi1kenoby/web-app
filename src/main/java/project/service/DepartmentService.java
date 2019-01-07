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
 *
 * @author Alexander Naumov.
 */
public class DepartmentService {

    private Set<Model> cache = new HashSet<>();

    @Autowired
    private ModelRepository repository;

    @PostConstruct
    private void init() {
        cache = getAll();
    }

    public Set<Model> getAll() {
        if (cache.isEmpty()) {
            Optional optional = repository.getList(Department.class);
            if (optional.isPresent()) {
                cache = new HashSet<>((List<Model>) optional.get());
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

    public void save(Department department, List<Model> subjects) {
        if (!cache.contains(subjects)) {
            subjects.forEach(model -> {
                ((Subject) model).getDepartments().add(department);
                repository.saveOrUpdate(model);
            });
            cache.add(department);
        }
    }

    public void edit(Department department) {
        if (cache.contains(department)) {
            repository.saveOrUpdate(department);
            for(Model model : cache) {
                if (((Department)model).getName().equals(department.getName())) {
                    cache.remove(model);
                    cache.add(department);
                }
            }
        }
    }

    public void remove(Department department) {
        if (cache.contains(department)) {
            cache.remove(department);
        }
        repository.deleteByid(department.getClass(), department.getId());
    }

    public Model getByName(String name) {
        for (Model model : cache) {
            if (((Department)model).getName().equals(name)) {
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
