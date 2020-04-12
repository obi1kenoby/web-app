package project.repository;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.NonUniqueResultException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import project.model.*;

import javax.persistence.NoResultException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of {@link ModelRepository} interface.
 *
 * @author Alexander Naumov.
 */
@Slf4j
@Transactional
public class ModelRepositoryImpl implements ModelRepository {

    @Autowired
    private SessionFactory sessionFactory;

    /**
     * Implementation of ${@link ModelRepository#getDepByName(String)}.
     *
     * @param name of department {@link Department}.
     * @return instance of {@link Optional<Model>}.
     */
    @Override
    public Optional<Model> getDepByName(String name) {
        Session session = getSession();
        Query<Model> query = session.createQuery("SELECT DISTINCT d FROM Department d WHERE d.name =:name", Model.class);
        query.setParameter("name", name);
        try {
            List<Model> models = query.getResultList();
            if (!models.isEmpty()) {
                if (models.size() > 1) {
                    throw new NonUniqueResultException(models.size());
                } else {
                    log.info("IN getDepByName, Department with name: {} successfully loaded.", name);
                    return Optional.of((Department)models.get(0));
                }
            }
        } catch (NoResultException e) {
            log.error("IN getDepByName, error was occurred while loading Department with name: {}.", name, e);
        }
        log.info("IN getDepByName, Department with name: {} is miss in db.", name);
        return Optional.empty();
    }

    /**
     * Implementation of ${@link ModelRepository#getSubjectByName(String)}.
     *
     * @param name of subject {@link Subject}.
     * @return instance of {@link Optional<Model>}.
     */
    @Override
    public Optional<Model> getSubjectByName(String name) {
        Session session = getSession();
        Query<Model> query = session.createQuery("SELECT DISTINCT s FROM Subject s WHERE s.name =:name", Model.class);
        query.setParameter("name", name);
        Subject result = (Subject) query.getSingleResult();
        if (result == null) {
            log.info("IN getSubjectByName, you try load non existing subject with name: {} from DB.", name);
            return Optional.empty();
        } else {
            log.info("IN getSubjectByName, the subject with name: {} was successfully loaded from DB.", name);
            return Optional.of(result);
        }
    }

    /**
     * Load unique instance of {@link Student} from DB by their first name and last name.
     *
     * @param firstName of student {@link Student}.
     * @param lastName of student {@link Student}.
     * @return instance of {@link Optional<Student>}.
     */
    @Override
    public Optional<Model> getStudByFullName(String firstName, String lastName) {
        Session session = getSession();
        Query<Model> query = session.createQuery("SELECT DISTINCT s FROM Student s WHERE s.first_name =:firstName AND s.last_name =:lastName", Model.class);
        query.setParameter("firstName", firstName);
        query.setParameter("lastName", lastName);
        Student result = (Student) query.getSingleResult();
        if (result == null) {
            log.info("IN getStudByFullName, you try to load non existing student with name: {} and last name: {}, from DB.", firstName, lastName);
            return Optional.empty();
        } else {
            log.info("IN getStudByFullName, the student with name: {} and last name: {}, was successfully loaded from DB.", firstName, lastName);
            return Optional.of(result);
        }
    }

    /**
     * Load unique instance of {@link Student} from DB by their email.
     * @param email of student {@link Student}.
     *
     * @return instance of {@link Optional<Student>}.
     */
    @Override
    public Optional<Model> getStudByEmail(String email) {
        Session session = getSession();
        Query<Model> query = session.createQuery("SELECT DISTINCT s FROM Student s WHERE s.email =:email", Model.class);
        query.setParameter("email", email);
        Student result = (Student) query.getSingleResult();
        if (result == null) {
            log.info("IN getStudByEmail, you try to load non existing student with email: {} from DB.", email);
            return Optional.empty();
        } else {
            log.info("IN getStudByEmail, the student with email: {} was successfully loaded from DB.", email);
            return Optional.of(result);
        }
    }

    /**
     * Load list fo students {@link List<Student>} from DB by their department.
     *
     * @param id of department {@link Department}.
     * @return instance of {@link Optional<Student>}.
     */
    @Override
    public Optional<List<Model>> getStudsByDepId(Long id) {
        Session session = getSession();
        Query<Model> query = session.createQuery("SELECT s FROM Student s WHERE s.department.id =:id", Model.class);
        query.setParameter("id", id);
        List<Model> result = query.getResultList();
        if (result.isEmpty()) {
            log.info("IN getStudsByDepId, the students with department id: {} are missing in DB.", id);
            return Optional.empty();
        } else {
            log.info("IN getStudsByDepId, all students with department id: {} were successfully loaded from DB.", id);
            return Optional.of(result);
        }
    }

    /**
     * Loads list of all marks {@link Mark} by subject {@link Subject}, date between special dates range, and
     * student {@link Student} with special department id.
     *
     * @param depId id of department {@link Department}.
     * @param subject name of subject {@link Subject}.
     * @param since start date {@link LocalDate}.
     * @param to end date {@link LocalDate}.
     * @return list of marks {@link Optional<List<Mark>>}.
     */
    @Override
    public Optional<List<Model>> getMarksForMonth(Long depId, String subject, LocalDate since, LocalDate to) {
        Session session = getSession();
        Query<Model> query = session.createQuery("SELECT m FROM Mark m WHERE m.date BETWEEN :since AND :to AND (m.subject.name =:subject) AND (m.student IN (SELECT s FROM Student s WHERE s.department.id =:depId))", Model.class);
        query.setParameter("depId", depId);
        query.setParameter("subject", subject);
        query.setParameter("since", since);
        query.setParameter("to", to);
        List<Model> result = query.getResultList();
        if (result.isEmpty()) {
            log.info("IN getMarksForMonth, the marks with subject: {}, department id:{} and date between" +
                    "{} and {} are missing in DB.", subject, depId, since, to);
        } else {
            log.info("IN getMarksForMonth, the marks with subject: {}, department id:{} and date between" +
                    "{} and {} were successfully loaded from DB.", subject, depId, since, to);
        }
        return Optional.ofNullable(query.getResultList());
    }

    /**
     * Implementation of ${@link ModelRepository#saveOrUpdate(Model)}.
     *
     * @param model {@link Model} that need save or update.
     */
    @Override
    public void saveOrUpdate(Model model) {
        Session session = getSession();
        try {
            if (model.getId() != 0) {
                session.update(model);
                log.info("IN saveOrUpdate, the instance of {}, was successfully updated.", model);
            } else {
                session.save(model);
                log.info("IN saveOrUpdate, the instance of {}, was successfully saved.", model);
            }
        } catch (Exception e) {
            log.error("IN saveOrUpdate, error was occurred while saving instance of {}.", model, e);
        }
    }

    /**
     * Implementation of ${@link ModelRepository#getById(Class, Long)}.
     *
     * @param clazz subclass of {@link Model}.
     * @param id of mark {@link Mark}.
     * @return instance of model {@link Optional<Model>}.
     */
    @Override
    public Optional<Model> getById(Class<? extends Model> clazz, Long id) {
        Session session = getSession();
        Model model = session.get(clazz, id);
        if (model == null) {
            log.info("IN getById, you try to get non existing {} with id: {} from DB.", clazz, id);
            return Optional.empty();
        } else {
            log.info("IN getById, the {} with id: {}, was successfully loaded from DB.", clazz, id);
            return Optional.of(model);
        }
    }

    /**
     * Implementation of ${@link ModelRepository#getList}.
     *
     * @param clazz subclass of {@link Model}.
     * @return list of models {@link Optional<List<Mark>>}.
     */
    @Override
    public Optional<List<Model>> getList(Class<? extends Model> clazz) {
        Session session = getSession();
        Query<Model> query = session.createQuery("SELECT c FROM " + clazz.getName() + " c",  Model.class);
        List<Model> result = query.getResultList();
        if (result.isEmpty()) {
            log.info("IN getList, the instances of {}, are missing in DB.", clazz);
            return Optional.empty();
        } else {
            log.info("IN getList, all instances of {}, were successfully loaded from DB.", clazz);
            return Optional.of(result);
        }
    }

    /**
     * Implementation of ${@link ModelRepository#getListById(Class, Long[])}.
     *
     * @param clazz subclass of {@link Model}.
     * @param ids array of needed departments {@link Department}.
     * @return list of models {@link Optional<List<Mark>>}.
     */
    @Override
    public Optional<List<Model>> getListById(Class<? extends Model> clazz, Long[] ids) {
        Session session = getSession();
        Query<Model> query = session.createQuery("SELECT c FROM " + clazz.getName() + " c WHERE c.id IN :ids", Model.class);
        query.setParameter("ids", Arrays.asList(ids));
        List<Model> result = query.getResultList();
        if (result.isEmpty()) {
            log.info("IN getListById, you try to get non existing instances of {}, " +
                    "with ids: {}, from DB.", clazz, Arrays.toString(ids));
            return Optional.empty();
        } else {
            log.info("IN getListById, the instances of {} with ids: {}, " +
                    "were successfully loaded from DB.", clazz, Arrays.toString(ids));
            return Optional.of(result);
        }
    }

    /**
     * Implementation of ${@link ModelRepository#getById(Class, Long)}.
     *
     * @param clazz subclass of {@link Model}.
     * @param id of student {@link Student}.
     * @return int result.
     */
    @Override
    public int deleteById(Class<? extends Model> clazz, Long id) {
        Session session = getSession();
        int res = 0;
        if (!clazz.equals(Student.class)) {
            Query query = session
                    .createQuery("DELETE FROM " + clazz.getName() + " d WHERE d.id =:id")
                    .setParameter("id", id);
            res = query.executeUpdate();
        } else {
            Optional<Model> o = getById(Student.class, id);
            if (o.isPresent()) {
                try {
                    session.delete(o.get());
                    res = 1;
                } catch (Exception e) {
                    return 0;
                }
            }
        }
        if (res == 1) {
            log.info("IN deleteById, {} with id: {} was successfully deleted from DB.", clazz.getName(), id);
        } else {
            log.info("IN deleteById, you try to delete non existing {} with id: {}", clazz.getName(), id);
        }
        return res;
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