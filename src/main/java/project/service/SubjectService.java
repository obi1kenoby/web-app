package project.service;

import org.springframework.beans.factory.annotation.Autowired;
import project.model.Model;
import project.model.Subject;
import project.repository.ModelRepository;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author Alexander Naumov.
 */
public class SubjectService  {

    @Autowired
    private ModelRepository repository;


    public Optional<List<Model>> getAll() {
        return repository.getList(Subject.class);
    }


    public Optional<List<Model>> getListById(Long[] ids) {
        return repository.getListById(Subject.class, ids);
    }

}
