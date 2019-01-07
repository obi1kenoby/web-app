package project.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import project.model.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Implimentation of {@link ModelRepository} interface.
 *
 * @author Alexander Naumov.
 */
@Transactional
public class ModelRepositoryImpl implements ModelRepository<Model> {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Optional<Model> getDepByName(String name) {
        Session session = getSession();
        Query query = session.createQuery("SELECT DISTINCT d FROM Department d WHERE d.name =:name");
        query.setParameter("name", name);
        return Optional.ofNullable((Department)query.getSingleResult());
    }

    @Override
    public Optional<Model> getSubjectByName(String name) {
        Session session = getSession();
        Query query = session.createQuery("SELECT DISTINCT s FROM Subject s WHERE s.name =:name");
        query.setParameter("name", name);
        return Optional.ofNullable((Subject)query.getSingleResult());
    }

    @Override
    public Optional<Model> getStudByFullName(String firstName, String lastName) {
        Session session = getSession();
        Query query = session.createQuery("SELECT DISTINCT s FROM Student s WHERE s.first_name =:firstName AND s.last_name =:lastName");
        query.setParameter("firstName", firstName);
        query.setParameter("lastName", lastName);
        return Optional.ofNullable((Student)query.getSingleResult());
    }

    @Override
    public Optional<Model> getStudByEmail(String email) {
        Session session = getSession();
        Query query = session.createQuery("SELECT DISTINCT s FROM Student s WHERE s.email =:email");
        query.setParameter("email", email);
        return Optional.ofNullable((Student)query.getSingleResult());
    }

    @Override
    public Optional<List<Model>> getStudsByDepId(Long id) {
        Session session = getSession();
        Query query = session.createQuery("SELECT s FROM Student s WHERE s.department.id =:id");
        query.setParameter("id", id);
        return Optional.ofNullable((List<Model>)query.getResultList());
    }

    @Override
    public Optional<List<Model>> getMarksForMonth(Long depId, String subject, LocalDate since, LocalDate to) {
        Session session = getSession();
        Query query = session.createQuery("SELECT m FROM Mark m WHERE m.date BETWEEN :since AND :to AND (m.subject.name =:subject) AND (m.student IN (SELECT s FROM Student s WHERE s.department.id =:depId))");
        query.setParameter("depId", depId);
        query.setParameter("subject", subject);
        query.setParameter("since", since);
        query.setParameter("to", to);
        return Optional.ofNullable((List<Model>)query.getResultList());
    }

    @Override
    public void saveOrUpdate(Model model) {
        Session session = getSession();
        if (model.getId() != 0) {
            session.update(model);
        } else {
            session.save(model);
        }
    }

    @Override
    public Optional<Model> getById(Class<? extends Model> clazz, Long id) {
        Session session = getSession();
        Model model = session.get(clazz, id);
        return Optional.ofNullable(model);
    }

    @Override
    public Optional<List<Model>> getList(Class<? extends Model> clazz) {
        Session session = getSession();
        Query query = session.createQuery("SELECT c FROM " + clazz.getName() + " c");
        return Optional.ofNullable(query.getResultList());
    }

    @Override
    public Optional<List<Model>> getListById(Class<? extends Model> clazz, Long[] ids) {
        Session session = getSession();
        Query query = session.createQuery("SELECT c FROM " + clazz.getName() + " c WHERE c.id IN :ids");
        query.setParameter("ids", Arrays.asList(ids));
        return Optional.ofNullable((List<Model>)query.getResultList());
    }

    @Override
    public void deleteByid(Class<? extends Model> clazz, Long id) {
        Session session = getSession();
        if (clazz.equals(Department.class)) {
            Query query = session.createQuery("DELETE FROM " + clazz.getName() + " d WHERE d.id =:id");
            query.setParameter("id", id);
            query.executeUpdate();
        }
        Model model = session.get(clazz, id);
        if (model != null) {
            session.delete(model);
        }
    }

    /**
     * Returns current session.
     *
     * @return instance of {@link Session}.
     */
    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }
}
