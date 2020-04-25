package project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Simple JavaBean object that represents a Faculty
 * and extended {@link Model} class.
 *
 * @author Alexander Naumov.
 */
@Entity
@Table(name = "FACULTY")
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"students", "subjects"}, callSuper = true)
@ToString(exclude = {"students", "subjects"})
public class Faculty extends Model implements Comparable<Faculty> {

    @Column(name = "NAME", length = 60)
    private String name;

    @OneToMany(mappedBy = "faculty")
    private Set<Student> students;

    @JsonIgnore
    @ManyToMany(mappedBy = "faculties")
    private Set<Subject> subjects;

    public Faculty(String name){
        this.name = name;
        this.subjects = new HashSet<>();
        this.students = new HashSet<>();
    }

    @Override
    public int compareTo(Faculty o) {
        int result = name.compareTo(o.getName());
        return Integer.compare(result, 0);
    }
}