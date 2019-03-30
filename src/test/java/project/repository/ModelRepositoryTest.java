package project.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import project.config.ApplicationConfig;
import project.config.DataConfig;
import project.model.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

/**
 * Test class for {@link ModelRepository} class.
 *
 * @author Alexander Naumov.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {ApplicationConfig.class, DataConfig.class})
public class ModelRepositoryTest {

    @Autowired
    private ModelRepository repository;


    @Test
    @Sql(scripts = "classpath:sql/tests/delete_student.sql", executionPhase = BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:sql/tests/delete_all.sql", executionPhase = AFTER_TEST_METHOD)
    public void deleteMarkByIdTest() {
        repository.deleteByid(Mark.class, 1L);
        Optional optional = repository.getById(Mark.class, 1L);
        if (optional.isPresent()) {
            fail("optional must be empty!");
        }
    }

    @Test
    @Sql(scripts = "classpath:sql/tests/delete_subject.sql", executionPhase = BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:sql/tests/delete_all.sql", executionPhase = AFTER_TEST_METHOD)
    public void deleteSubjectByIdTest() {
        repository.deleteByid(Subject.class, 1L);
        Optional optional = repository.getById(Subject.class, 1L);
        if (optional.isPresent()) {
            fail("optional must be empty!");
        }
    }

    @Test
    @Sql(scripts = "classpath:sql/tests/delete_student.sql", executionPhase = BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:sql/tests/delete_all.sql", executionPhase = AFTER_TEST_METHOD)
    public void deleteStudentByIdTest() {
        repository.deleteByid(Student.class, 1L);
        Optional optional = repository.getById(Student.class, 1L);
        if (optional.isPresent()) {
            fail("optional must be empty!");
        }
    }

    @Test
    @Sql(scripts = "classpath:sql/tests/delete_department.sql", executionPhase = BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:sql/tests/delete_all.sql", executionPhase = AFTER_TEST_METHOD)
    public void deleteDepartmentByIdTest() {
        repository.deleteByid(Department.class, 1L);
        Optional optional = repository.getById(Department.class, 1L);
        if (optional.isPresent()) {
            fail("optional must be empty!");
        }
    }

    @Test
    @Sql(scripts = "classpath:sql/tests/add_department.sql", executionPhase = BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:sql/tests/delete_all.sql", executionPhase = AFTER_TEST_METHOD)
    public void saveDepartmentTest() {
        Department expected = new Department("DEPARTMENT");
        Optional optional = repository.getList(Subject.class);
        if (optional.isPresent()) {
            List<Subject> subjects = (List<Subject>) optional.get();
            for (Subject subject : subjects) {
                subject.getDepartments().add(expected);
                repository.saveOrUpdate(subject);
            }
        }
        Optional op = repository.getDepByName("DEPARTMENT");
        if (op.isPresent()) {
            Department actual = (Department) op.get();
            assertEquals(expected.getName(), actual.getName());
            assertTrue(actual.getSubjects().size() > 0);
        } else {
            fail("optional can't be empty!");
        }
    }

    @Test
    @Sql(scripts = "classpath:sql/tests/add_subject.sql", executionPhase = BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:sql/tests/delete_all.sql", executionPhase = AFTER_TEST_METHOD)
    public void saveSubjectTest() {
        Subject expected = new Subject("SUBJECT");
        Optional optional = repository.getList(Department.class);
        if (optional.isPresent()) {
            expected.getDepartments().addAll((List<? extends Department>) optional.get());
        }
        repository.saveOrUpdate(expected);
        Optional op = repository.getSubjectByName("SUBJECT");
        if (op.isPresent()) {
            Subject actual = (Subject) op.get();
            assertEquals(expected.getName(), actual.getName());
            assertTrue(actual.getDepartments().size() > 0);
        } else {
            fail("optional can't be empty!");
        }
    }

    @Test
    @Sql(scripts = "classpath:sql/tests/add_student.sql", executionPhase = BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:sql/tests/delete_all.sql", executionPhase = AFTER_TEST_METHOD)
    public void saveStudentTest() {
        Optional optional = repository.getDepByName("DEPARTMENT");
        Department department = (Department)optional.get();
        Student expected = new Student();
        expected.setFirst_name("FIRST-NAME");
        expected.setLast_name("LAST-NAME");
        expected.setPassword("PASSWORD");
        expected.setEmail("EMAIL");
        expected.setRole(Role.USER);
        expected.setBirthday(LocalDate.of(2001, 4, 14));
        expected.setDepartment(department);
        repository.saveOrUpdate(expected);
        Optional op = repository.getStudByFullName("FIRST-NAME", "LAST-NAME");
        if (op.isPresent()) {
            Student actual = (Student) op.get();
            assertEquals(expected.getFirst_name(), actual.getFirst_name());
            assertEquals(expected.getLast_name(), actual.getLast_name());
            assertNotNull(expected.getDepartment());
        } else {
            fail("optional can't be empty!");
        }
    }

    @Test
    @Sql(scripts = "classpath:sql/tests/add_mark.sql", executionPhase = BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:sql/tests/delete_all.sql", executionPhase = AFTER_TEST_METHOD)
    public void saveMarkTest() {
        Optional opStudent = repository.getStudByFullName("FIRST-NAME", "LAST-NAME");
        Optional opSubject = repository.getSubjectByName("SUBJECT");
        Mark mark = new Mark();
        mark.setValue(5);
        mark.setDate(LocalDate.now());
        mark.setSubject((Subject)opSubject.get());
        mark.setStudent((Student)opStudent.get());
        repository.saveOrUpdate(mark);
        Optional opMark = repository.getList(Mark.class);
        if (opMark.isPresent()) {
            List<Mark> marks = (List<Mark>)opMark.get();
            for (Mark m: marks) {
                assertEquals(mark.getValue(), m.getValue());
                assertEquals(mark.getDate(), m.getDate());
            }
        } else {
            fail("optional can't be empty!");
        }
    }

    @Test
    @Sql(scripts = "classpath:sql/tests/add_student.sql", executionPhase = BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:sql/tests/delete_all.sql", executionPhase = AFTER_TEST_METHOD)
    public void getDepByNameTest() {
        Optional optional = repository.getDepByName("DEPARTMENT");
        if (optional.isPresent()) {
            Department department = (Department) optional.get();
            assertEquals("DEPARTMENT", department.getName());
        } else {
            fail("optional can't be empty!");
        }
    }

    @Test
    @Sql(scripts = "classpath:sql/tests/add_department.sql", executionPhase = BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:sql/tests/delete_all.sql", executionPhase = AFTER_TEST_METHOD)
    public void getSubjectByNameTest() {
        Optional optional = repository.getSubjectByName("SUBJECT-1");
        if (optional.isPresent()) {
            Subject subject = (Subject) optional.get();
            assertEquals("SUBJECT-1", subject.getName());
        } else {
            fail("optiional can't be empty!");
        }
    }

    @Test
    @Sql(scripts = "classpath:sql/tests/add_mark.sql", executionPhase = BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:sql/tests/delete_all.sql", executionPhase = AFTER_TEST_METHOD)
    public void getStudByFullNameTest() {
        Optional optional = repository.getStudByFullName("FIRST-NAME", "LAST-NAME");
        if (optional.isPresent()) {
            Student student = (Student) optional.get();
            assertEquals("FIRST-NAME", student.getFirst_name());
            assertEquals("LAST-NAME", student.getLast_name());
        } else {
            fail("optional can't be empty!");
        }
    }

    @Test
    @Sql(scripts = "classpath:sql/tests/add_mark.sql", executionPhase = BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:sql/tests/delete_all.sql", executionPhase = AFTER_TEST_METHOD)
    public void getStudByEmailTest() {
        Optional optional = repository.getStudByEmail("EMAIL");
        if (optional.isPresent()) {
            Student student = (Student) optional.get();
            assertEquals("EMAIL", student.getEmail());
        } else {
            fail("optional can't be empty!");
        }
    }

    @Test
    @Sql(scripts = "classpath:sql/tests/add_mark.sql", executionPhase = BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:sql/tests/delete_all.sql", executionPhase = AFTER_TEST_METHOD)
    public void getStudsByDepIdTest() {
        Optional optional = repository.getStudsByDepId(1L);
        if (optional.isPresent()) {
            List<Student> students = (List<Student>) optional.get();
            assertTrue(students.size() > 0);
            for(Student student : students) {
                assertEquals(1L, student.getDepartment().getId());
            }
        } else {
            fail("optional can't be empty!");
        }
    }

    @Test
    @Sql(scripts = "classpath:sql/tests/delete_student.sql", executionPhase = BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:sql/tests/delete_all.sql", executionPhase = AFTER_TEST_METHOD)
    public void getMarksForMonthTest() {
        Optional optional = repository.getMarksForMonth(1L, "SUBJECT", LocalDate.of(2018, 4, 1), LocalDate.of(2018, 5, 1));
        if (optional.isPresent()) {
            List<Mark> marks = (List<Mark>)optional.get();
            assertTrue(marks.size() == 2);
            for (Mark mark : marks) {
                assertTrue(mark.getDate().isAfter(LocalDate.of(2018, 4, 1).minusDays(1)) && mark.getDate().isBefore(LocalDate.of(2018, 5, 1)));
            }
        } else {
            fail("optional can't be empty!");
        }
    }

    @Test
    @Sql(scripts = "classpath:sql/tests/add_student.sql", executionPhase = BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:sql/tests/delete_all.sql", executionPhase = AFTER_TEST_METHOD)
    public void getByIdTest() {
        Optional optional = repository.getById(Department.class, 1L);
        if (optional.isPresent()) {
            Department department = (Department) optional.get();
            assertEquals(1L, department.getId());
        } else {
            fail("optional can't be empty!");
        }
    }

    @Test
    @Sql(scripts = "classpath:sql/tests/delete_department.sql", executionPhase = BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:sql/tests/delete_all.sql", executionPhase = AFTER_TEST_METHOD)
    public void getListByIdTest() {
        Long[] ids = {1L, 2L, 3L};
        Optional optional = repository.getListById(Subject.class, ids);
        if (optional.isPresent()) {
            List<Subject> subjects = (List<Subject>) optional.get();
            assertTrue(subjects.size() > 0);
            for (Subject subject : subjects) {
                assertTrue(Arrays.asList(ids).contains(subject.getId()));
            }
        } else {
            fail("optional can't be empty!");
        }
    }

    @Test
    @Sql(scripts = "classpath:sql/tests/delete_student.sql", executionPhase = BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:sql/tests/delete_all.sql", executionPhase = AFTER_TEST_METHOD)
    public void getListTest() {
        Long[] ids = {1L, 2L, 3L};
        Optional optional = repository.getListById(Mark.class, ids);
        if (optional.isPresent()) {
            List<Subject> subjects = (List<Subject>) optional.get();
            assertTrue(subjects.size() > 0);
        } else {
            fail("optional can't be empty!");
        }
    }

}
