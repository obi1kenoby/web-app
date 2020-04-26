package project.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import javax.persistence.*;
import java.time.LocalDate;

/**
 * Simple JavaBean object that represents a Grade
 * and extended {@link Model} class.
 *
 * @author Alexander Naumov.
 */
@Entity
@Table(name = "GRADE")
@Data
@EqualsAndHashCode(exclude = {"student", "subject"}, callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Grade extends Model {

    @Enumerated(EnumType.STRING)
    @Column(name = "GRADE")
    private GradeValue grade;

    @Column(name = "PERCENT")
    private double value;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "DATE", columnDefinition = "DATE")
    private LocalDate date;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STUD_ID")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "SUBJ_ID")
    private Subject subject;
}