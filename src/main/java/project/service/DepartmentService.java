package project.service;

import org.springframework.beans.factory.annotation.Autowired;
import project.model.Department;
import project.model.Model;
import project.model.Subject;
import project.repository.ModelRepository;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author Alexander Naumov.
 */
public class DepartmentService {

    @Autowired
    private ModelRepository repository;

    public Optional<List<Model>> getAll() {
        return this.repository.getList(Department.class);
    }

    public Optional<List> getListById(Long[] ids) {
        return repository.getListById(Department.class, ids);
    }

    public Optional getById(Long id) {
        return this.repository.getById(Department.class, id);
    }

    public void save(Department department, List<Model> subjects) {
        subjects.forEach(model -> {
            ((Subject)model).getDepartments().add(department);
            this.repository.saveOrUpdate(model);
        });
    }

    public void edit() {

    }

    public void remove() {

    }

    public Optional getByName(String name) {
        return this.repository.getDepByName(name);
    }
}
