package project.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import project.config.ApplicationConfig;
import project.config.DataConfig;
import project.model.*;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Base test class for {@link project.controller} classes.
 *
 * @author Alexander Naumov.
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ApplicationConfig.class, DataConfig.class})
@WebAppConfiguration
public class BaseControllerTest {

    @Autowired
    private ApplicationContext context;

    @Test
    public void testAppContextNotNull() {
        assertNotNull(context);
    }

    static Grade createGrade(Long id, Double percent, LocalDate date) {
        Grade grade = new Grade();
        grade.setId(id);
        grade.setDate(date);
        grade.setValue(percent);
        grade.setGrade(GradeValue.getValueFromPercent(percent));
        return grade;
    }

    static Student createStudent(Long id, String firstName, String lastName) {
        Student student = new Student();
        student.setId(id);
        student.setFirst_name(firstName);
        student.setLast_name(lastName);
        return student;
    }

    static Faculty createFaculty(Long id, String name) {
        Faculty faculty = new Faculty();
        faculty.setId(id);
        faculty.setName(name);
        return faculty;
    }

    static Subject createSubject(Long id, String name) {
        Subject subject = new Subject();
        subject.setId(id);
        subject.setName(name);
        return subject;
    }
}
