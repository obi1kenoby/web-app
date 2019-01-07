package project.model;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDate;

/**
 * Simple JavaBean object that represents a Mark.
 *
 * @author Alexander Naumov.
 */
@Entity
@Table(name = "MARK")
@Data
@EqualsAndHashCode(exclude = {"student", "subject"}, callSuper = true)
@NoArgsConstructor
public class Mark extends Model{

    @Column(name = "VALUE")
    private int value;

    @Column(name = "DATE", columnDefinition = "DATE")
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STUD_ID")
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SUBJ_ID")
    private Subject subject;
}