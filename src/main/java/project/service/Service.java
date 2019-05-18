package project.service;

import project.model.Model;

import java.util.List;

/**
 * Universal interface for handling of {@link Model} instances.
 *
 * @author Alexander Naumov.
 */
public interface Service {

    /**
     * Get all {@link Model} from cache or database.
     *
     * @return list of {@link Model}.
     */
    List<Model> getAll();

    /**
     * Get {@link Model} from cache or database.
     *
     * @param id of {@link Model}.
     * @return {@link Model}
     */
    Model getById(Long id);

    /**
     * Delete {@link Model} from database and cache.
     *
     * @param id of {@link Model}.
     * @return 1 if deleting was success else 0.
     */
    int deleteById(Long id);

    /**
     * Save model to database and cache.
     *
     * @param model {@link Model}.
     * @return true if saving was success else false.
     */
    boolean save(Model model);
}
