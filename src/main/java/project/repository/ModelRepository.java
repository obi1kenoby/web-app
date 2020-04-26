package project.repository;

import project.model.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Common repository of {@link Model}.
 *
 * @author Alexander Naumov.
 */
public interface ModelRepository {

    /**
     * Gets needed {@link Model} from DB by name.
     *
     * @param name of faculty.
     * @return {@link Model}.
     */
    Optional<Model> getFacByName(String name);

    /**
     * Gets needed {@link Model} from DB by name.
     *
     * @param name of subject.
     * @return {@link Model}.
     */
    Optional<Model> getSubjectByName(String name);

    /**
     * Gets needed {@link Model} from DB, by first name and last name.
     *
     * @param firstName of student.
     * @param lastName of student.
     * @return {@link Subject}.
     */
    Optional<Model> getStudByFullName(String firstName, String lastName);

    /**
     * Gets needed {@link Model} from DB, by e-mail.
     *
     * @param email of student.
     * @return {@link Model}.
     */
    Optional<Model> getStudByEmail(String email);

    /**
     * Gets needed {@link Student}'s from DB, by {@link Faculty} ID.
     *
     * @param id of department.
     * @return list of {@link Student}.
     */
    Optional<List<Model>> getStudsByFucId(Long id);

    /**
     * Gets needed {@link Grade}'s from DB, by {@link Subject} name, {@link Faculty} ID,
     * and special date range.
     *
     * @param depId id of department.
     * @param subject name of subject.
     * @param since start date.
     * @param to end date.
     * @return list of {@link Grade}.
     */
    Optional<List<Model>> getGradesForMonth(Long depId, String subject, LocalDate since, LocalDate to);

    /**
     * Saves or updates {@link Model} to DB.
     *
     * @param t entity for saving.
     */
    void saveOrUpdate(Model t);

    /**
     * Gets needed {@link Model} from DB, by ID.
     *
     * @param clazz {@link Class} of entity.
     * @param id of mark.
     * @return {@link Model}.
     */
    Optional<Model> getById(Class<? extends Model> clazz, Long id);

    /**
     * Gets all {@link Model} from DB.
     *
     * @param clazz {@link Class} of entity.
     * @return list of {@link Model}.
     */
    Optional<List<Model>> getList(Class<? extends Model> clazz);

    /**
     * Gets needed list of {@link Model} from DB by their IDs.
     *
     * @param ids array of needed departments.
     * @return List of {@link Model}.
     */
    Optional<List<Model>> getListById(Class<? extends Model> clazz, Long[] ids);

    /**
     * Deletes needed {@link Model} from DB, by it ID.
     *
     * @param id student ID.
     * @return 1 if deleting was success else 0.
     */
    int deleteById(Class<? extends Model> clazz, Long id);
}