package project.model;

import lombok.Data;

import javax.persistence.*;

/**
 * Basic abstact class that represents application entity.
 *
 * @author Alexander Naumov.
 */
@Data
@MappedSuperclass
public abstract class Model {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
}
