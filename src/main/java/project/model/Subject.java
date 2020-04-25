package project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

/**
 * Simple JavaBean object that represents a Subject
 * and extended {@link Model} class.
 *
 * @author Alexander Naumov.
 */
@Entity
@Table(name = "SUBJECT")
@Data
@EqualsAndHashCode(exclude = {"faculties", "students", "marks"}, callSuper = true)
@NoArgsConstructor
@ToString(exclude = {"marks", "faculties", "students"})
public class Subject extends Model {

    @Column(name = "NAME")
    private String name;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "SUBJECT_FACULTY",
            joinColumns = @JoinColumn(name = "SUB_ID"),
            inverseJoinColumns = @JoinColumn(name = "FAC_ID"))
    private Set<Faculty> faculties;

    @JsonIgnore
    @ManyToMany(mappedBy = "subjects")
    private Set<Student> students;

    @JsonIgnore
    @OneToMany(mappedBy = "subject")
    private Set<Mark> marks;

    public Subject(String name) {
        this.name = name;
        this.faculties = new HashSet<>();
        this.students = new HashSet<>();
        this.marks = new HashSet<>();
    }
}