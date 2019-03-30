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
@EqualsAndHashCode(exclude = {"departments", "students", "marks"}, callSuper = true)
@NoArgsConstructor
@ToString(exclude = {"marks", "departments", "students"})
public class Subject extends Model {

    @Column(name = "NAME")
    private String name;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "SUBJECT_DEPARTMENT",
            joinColumns = @JoinColumn(name = "SUB_ID"),
            inverseJoinColumns = @JoinColumn(name = "DEP_ID"))
    private Set<Department> departments;

    @JsonIgnore
    @ManyToMany(mappedBy = "subjects")
    private Set<Student> students;

    @JsonIgnore
    @OneToMany(mappedBy = "subject")
    private Set<Mark> marks;

    public Subject(String name) {
        this.name = name;
        this.departments = new HashSet<>();
        this.students = new HashSet<>();
        this.marks = new HashSet<>();
    }
}