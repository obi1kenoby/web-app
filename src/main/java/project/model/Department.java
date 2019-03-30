package project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Simple JavaBean object that represents a Department
 * and extended {@link Model} class.
 *
 * @author Alexander Naumov.
 */
@Entity
@Table(name = "DEPARTMENT")
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"students", "subjects"}, callSuper = true)
@ToString(exclude = {"students", "subjects"})
public class Department extends Model implements Comparable<Department> {

    @Column(name = "NAME", length = 60)
    private String name;

    @OneToMany(mappedBy = "department")
    private Set<Student> students;

    @JsonIgnore
    @ManyToMany(mappedBy = "departments")
    private Set<Subject> subjects;

    public Department(String name){
        this.name = name;
        this.subjects = new HashSet<>();
        this.students = new HashSet<>();
    }

    @Override
    public int compareTo(Department o) {
        int result = name.compareTo(o.getName());
        return Integer.compare(result, 0);
    }
}