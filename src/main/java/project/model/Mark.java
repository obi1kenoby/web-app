package project.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import javax.persistence.*;
import java.time.LocalDate;

/**
 * Simple JavaBean object that represents a Mark
 * and extended {@link Model} class.
 *
 * @author Alexander Naumov.
 */
@Entity
@Table(name = "MARK")
@Data
@EqualsAndHashCode(exclude = {"student", "subject"}, callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Mark extends Model {

    @Column(name = "VALUE")
    private int value;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "DATE", columnDefinition = "DATE")
    private LocalDate date;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STUD_ID")
    private Student student;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SUBJ_ID")
    private Subject subject;
}