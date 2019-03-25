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
public interface ModelRepository<T> {

    /**
     * Gets needed {@link T} from DB by name.
     *
     * @param name of department.
     * @return {@link T}.
     */
    Optional<T> getDepByName(String name);

    /**
     * Gets needed {@link T} from DB by name.
     *
     * @param name of subject.
     * @return {@link T}.
     */
    Optional<T> getSubjectByName(String name);

    /**
     * Gets needed {@link T} from DB, by first name and last name.
     *
     * @param firstName of student.
     * @param lastName of student.
     * @return {@link Subject}.
     */
    Optional<T> getStudByFullName(String firstName, String lastName);

    /**
     * Gets needed {@link T} from DB, by e-mail.
     *
     * @param email of student.
     * @return {@link T}.
     */
    Optional<T> getStudByEmail(String email);

    /**
     * Gets needed {@link Student}'s from DB, by {@link Department} ID.
     *
     * @param id of department.
     * @return list of {@link Student}.
     */
    Optional<List<T>> getStudsByDepId(Long id);

    /**
     * Gets needed {@link Mark}'s from DB, by {@link Subject} name, {@link Department} ID,
     * and special date range.
     *
     * @param depId id of department.
     * @param subject name of subject.
     * @param since start date.
     * @param to end date.
     * @return list of {@link Mark}.
     */
    Optional<List<T>> getMarksForMonth(Long depId, String subject, LocalDate since, LocalDate to);

    /**
     * Saves or updates {@link T} to DB.
     *
     * @param t entity for saving.
     */
    void saveOrUpdate(T t);

    /**
     * Gets neeeded {@link T} from DB, by ID.
     *
     * @param clazz {@link Class} of entity.
     * @param id of mark.
     * @return {@link T}.
     */
    Optional<T> getById(Class<? extends T> clazz, Long id);

    /**
     * Gets all {@link T} from DB.
     *
     * @param clazz {@link Class} of entity.
     * @return list of {@link T}.
     */
    Optional<List<T>> getList(Class<? extends T> clazz);

    /**
     * Gets needed list of {@link T} from DB by their IDs.
     *
     * @param ids array of needed departments.
     * @return List of {@link T}.
     */
    Optional<List<T>> getListById(Class<? extends T> clazz, Long[] ids);

    /**
     * Deletes needed {@link T} from DB, by it ID.
     *
     * @param id student ID.
     * @return 1 if deleting was success else 0.
     */
    int deleteByid(Class<? extends T> clazz, Long id);
}