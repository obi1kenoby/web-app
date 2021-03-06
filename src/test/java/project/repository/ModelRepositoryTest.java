package project.repository;

import org.hibernate.NonUniqueResultException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import project.config.ApplicationConfig;
import project.config.DataConfig;
import project.model.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.util.AssertionErrors.fail;

/**
 * Integration test class for {@link ModelRepository} class.
 *
 * @author Alexander Naumov.
 */
@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {ApplicationConfig.class, DataConfig.class})
public class ModelRepositoryTest {

    @Autowired
    private ModelRepository repository;

    @Test
    public void getDepByNameNonexistentDepTest() {
        final String wrongName = "NON EXISTENT NAME";
        Optional<Model> optional = repository.getDepByName(wrongName);
        if (optional.isPresent()) {
            fail("optional must be empty!");
        }
    }

    @Test
    @Sql(scripts = "classpath:sql/tests/add_identical_departments.sql", executionPhase = BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:sql/tests/delete_all.sql", executionPhase = AFTER_TEST_METHOD)
    public void getDepByNameMultipleResult() {
        final String name = "NON UNIQUE NAME";
        NonUniqueResultException exception = assertThrows(NonUniqueResultException.class,
                () -> repository.getDepByName(name));
        assertEquals("query did not return a unique result: 2", exception.getMessage());
    }

    @Test
    @Sql(scripts = "classpath:sql/tests/delete_student.sql", executionPhase = BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:sql/tests/delete_all.sql", executionPhase = AFTER_TEST_METHOD)
    public void deleteMarkByIdTest() {
        repository.deleteById(Mark.class, 1L);
        Optional<Model> optional = repository.getById(Mark.class, 1L);
        if (optional.isPresent()) {
            fail("optional must be empty!");
        }
    }

    @Test
    @Sql(scripts = "classpath:sql/tests/delete_subject.sql", executionPhase = BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:sql/tests/delete_all.sql", executionPhase = AFTER_TEST_METHOD)
    public void deleteSubjectByIdTest() {
        Optional<Model> subject = repository.getSubjectByName("SUBJECT");
        subject.ifPresent(s -> {
            repository.deleteById(Subject.class, s.getId());
            Optional<Model> optional = repository.getById(Subject.class, s.getId());
            optional.ifPresent(i -> fail("optional must be empty!"));
        });
    }

    @Test
    @Sql(scripts = "classpath:sql/tests/delete_student.sql", executionPhase = BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:sql/tests/delete_all.sql", executionPhase = AFTER_TEST_METHOD)
    public void deleteStudentByIdTest() {
        Optional<Model> optional = repository.getStudByFullName("FIRST-NAME", "LAST-NAME");
        long id = 0;
        if (optional.isPresent()) {
            id = optional.get().getId();
        }

        int result = repository.deleteById(Student.class, id);
        assertEquals(1, result);
        optional = repository.getById(Student.class, id);
        if (optional.isPresent()) {
            fail("optional must be empty!");
        }
    }

    @Test
    @Sql(scripts = "classpath:sql/tests/delete_department.sql", executionPhase = BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:sql/tests/delete_all.sql", executionPhase = AFTER_TEST_METHOD)
    public void deleteDepartmentByIdTest() {
        Optional<Model> optional = repository.getDepByName("DEPARTMENT");
        long id = 0;
        if (optional.isPresent()) {
            id = optional.get().getId();
        }
        repository.deleteById(Department.class, id);
        optional = repository.getById(Department.class, id);
        if (optional.isPresent()) {
            fail("optional must be empty!");
        }
    }

    @Test
    public void deleteNonexistentDepTest() {
        int result = repository.deleteById(Department.class, 4L);
        assertEquals(0, result);
    }

    @Test
    public void deleteNonexistentStudTest() {
        int result = repository.deleteById(Student.class, 4L);
        assertEquals(0, result);
    }

    @Test
    @Sql(scripts = "classpath:sql/tests/add_department.sql", executionPhase = BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:sql/tests/delete_all.sql", executionPhase = AFTER_TEST_METHOD)
    public void saveDepartmentTest() {
        Department expected = new Department("DEPARTMENT");
        Optional<List<Model>> optional = repository.getList(Subject.class);
        optional.ifPresent(list -> list.forEach(m -> {
            Subject subject = (Subject) m;
            subject.getDepartments().add(expected);
            repository.saveOrUpdate(subject);
        }));
        Optional<Model> optional1 = repository.getDepByName("DEPARTMENT");
        optional1.ifPresent(m -> {
            Department actual = (Department) m;
            assertEquals(expected.getName(), actual.getName());
            assertTrue(actual.getSubjects().size() > 0);
        });
    }

    @Test
    @Sql(scripts = "classpath:sql/tests/add_subject.sql", executionPhase = BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:sql/tests/delete_all.sql", executionPhase = AFTER_TEST_METHOD)
    public void saveSubjectTest() {
        Subject expected = new Subject("SUBJECT");
        Optional<List<Model>> optional = repository.getList(Department.class);
        optional.ifPresent(list -> list.forEach(i -> expected.getDepartments().add((Department) i)));
        repository.saveOrUpdate(expected);
        Optional<Model> optional1 = repository.getSubjectByName("SUBJECT");
        optional1.ifPresentOrElse(n -> {
            Subject actual = (Subject) n;
            assertEquals(expected.getName(), actual.getName());
            assertTrue(actual.getDepartments().size() > 0);
        }, () -> fail("optional can't be empty!"));
    }

    @Test
    @Sql(scripts = "classpath:sql/tests/add_student.sql", executionPhase = BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:sql/tests/delete_all.sql", executionPhase = AFTER_TEST_METHOD)
    public void saveStudentTest() {
        Optional<Model> optional = repository.getDepByName("DEPARTMENT");
        if (optional.isPresent()) {
            Department department = (Department) optional.get();
            Student expected = new Student();
            expected.setFirst_name("FIRST-NAME");
            expected.setLast_name("LAST-NAME");
            expected.setPassword("PASSWORD");
            expected.setEmail("EMAIL");
            expected.setRole(Role.USER);
            expected.setBirthday(LocalDate.of(2001, 4, 14));
            expected.setDepartment(department);
            repository.saveOrUpdate(expected);
            Optional<Model> optional1 = repository.getStudByFullName("FIRST-NAME", "LAST-NAME");
            optional1.ifPresentOrElse(m -> {
                Student actual = (Student) m;
                assertEquals(expected.getFirst_name(), actual.getFirst_name());
                assertEquals(expected.getLast_name(), actual.getLast_name());
                assertNotNull(expected.getDepartment());
            }, () -> fail("optional can't be empty!"));
        }
    }

    @Test
    @Sql(scripts = "classpath:sql/tests/add_mark.sql", executionPhase = BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:sql/tests/delete_all.sql", executionPhase = AFTER_TEST_METHOD)
    public void saveMarkTest() {
        Optional<Model> opStudent = repository.getStudByFullName("FIRST-NAME", "LAST-NAME");
        Optional<Model> opSubject = repository.getSubjectByName("SUBJECT");
        Mark mark = new Mark();
        mark.setValue(5);
        mark.setDate(LocalDate.now());
        opSubject.ifPresent(model -> mark.setSubject((Subject) model));
        opStudent.ifPresent(model -> mark.setStudent((Student) model));
        repository.saveOrUpdate(mark);
        Optional<List<Model>> opMark = repository.getList(Mark.class);
        opMark.ifPresentOrElse(m -> m.forEach(i -> {
            Mark mark1 = (Mark)i ;
            assertEquals(mark.getValue(), mark1.getValue());
            assertEquals(mark.getDate(), mark1.getDate());
        }), () -> fail("optional can't be empty!"));
    }

    @Test
    @Sql(scripts = "classpath:sql/tests/add_student.sql", executionPhase = BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:sql/tests/delete_all.sql", executionPhase = AFTER_TEST_METHOD)
    public void getDepByNameTest() {
        Optional<Model> optional = repository.getDepByName("DEPARTMENT");
        optional.ifPresentOrElse(m -> {
            Department department = (Department) m;
            assertEquals("DEPARTMENT", department.getName());
        }, () -> fail("optional can't be empty!"));
    }

    @Test
    @Sql(scripts = "classpath:sql/tests/add_department.sql", executionPhase = BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:sql/tests/delete_all.sql", executionPhase = AFTER_TEST_METHOD)
    public void getSubjectByNameTest() {
        Optional<Model> optional = repository.getSubjectByName("SUBJECT-1");
        optional.ifPresentOrElse(m -> {
            Subject subject = (Subject) m;
            assertEquals("SUBJECT-1", subject.getName());
        }, () -> fail("optional can't be empty!"));
    }

    @Test
    @Sql(scripts = "classpath:sql/tests/add_mark.sql", executionPhase = BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:sql/tests/delete_all.sql", executionPhase = AFTER_TEST_METHOD)
    public void getStudByFullNameTest() {
        Optional<Model> optional = repository.getStudByFullName("FIRST-NAME", "LAST-NAME");
        optional.ifPresentOrElse(m -> {
            Student student = (Student) m;
            assertEquals("FIRST-NAME", student.getFirst_name());
            assertEquals("LAST-NAME", student.getLast_name());
        }, () -> fail("optional can't be empty!"));
    }

    @Test
    @Sql(scripts = "classpath:sql/tests/add_mark.sql", executionPhase = BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:sql/tests/delete_all.sql", executionPhase = AFTER_TEST_METHOD)
    public void getStudByEmailTest() {
        Optional<Model> optional = repository.getStudByEmail("EMAIL");
        optional.ifPresentOrElse(m -> {
            Student student = (Student) m;
            assertEquals("EMAIL", student.getEmail());
        }, () -> fail("optional can't be empty!"));
    }

    @Test
    @Sql(scripts = "classpath:sql/tests/add_mark.sql", executionPhase = BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:sql/tests/delete_all.sql", executionPhase = AFTER_TEST_METHOD)
    public void getStudsByDepIdTest() {
        Optional<Model> opDepartment = repository.getDepByName("TEST_DEPARTMENT");
        long id = 0;
        if (opDepartment.isPresent()) {
            id = opDepartment.get().getId();
        }
        Optional<List<Model>> optional = repository.getStudsByDepId(id);
        long depId = id;
        optional.ifPresentOrElse(m -> {
            assertTrue(m.size() > 0);
            m.forEach(i -> {
                Student student = (Student) i;
                assertEquals(depId, student.getDepartment().getId());
            });
        }, () -> fail("optional can't be empty!"));
    }

    @Test
    @Sql(scripts = "classpath:sql/tests/delete_student.sql", executionPhase = BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:sql/tests/delete_all.sql", executionPhase = AFTER_TEST_METHOD)
    public void getMarksForMonthTest() {
        Optional<Model> optional = repository.getDepByName("DEPARTMENT");
        long id = 0;
        if (optional.isPresent()) {
            id = optional.get().getId();
        }
        Optional<List<Model>> listOptional = repository.getMarksForMonth(id, "SUBJECT", LocalDate.of(2018, 4, 1), LocalDate.of(2018, 5, 1));
        listOptional.ifPresentOrElse(m -> {
            assertEquals(2, m.size());
            m.forEach(i -> {
                Mark mark = (Mark) i;
                assertTrue(mark.getDate().isAfter(LocalDate.of(2018, 4, 1).minusDays(1)) && mark.getDate().isBefore(LocalDate.of(2018, 5, 1)));
            });
        }, () ->  fail("optional can't be empty!"));
    }

    @Test
    @Sql(scripts = "classpath:sql/tests/add_student.sql", executionPhase = BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:sql/tests/delete_all.sql", executionPhase = AFTER_TEST_METHOD)
    public void getByIdTest() {
        Optional<Model> optional = repository.getById(Department.class, repository.getDepByName("DEPARTMENT").get().getId());
        optional.ifPresentOrElse(m -> m.getId(), () -> fail("optional can't be empty!"));
    }

    @Test
    @Sql(scripts = "classpath:sql/tests/delete_department.sql", executionPhase = BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:sql/tests/delete_all.sql", executionPhase = AFTER_TEST_METHOD)
    public void getListByIdTest() {
        Optional<List<Model>> optionalList = repository.getList(Subject.class);
        if (optionalList.isPresent()) {
            List<Model> list = optionalList.get();
            List<Long> models = list.stream().map(Model::getId).collect(Collectors.toList());
            Long[] ids = new Long[models.size()];
            for (Long id : models) {
                ids[models.indexOf(id)] = id;
            }
            Optional<List<Model>> optional = repository.getListById(Subject.class, ids);
            optional.ifPresentOrElse(m -> assertTrue(m.size() > 0), () -> fail("optional can't be empty!"));
        }
    }

    @Test
    @Sql(scripts = "classpath:sql/tests/delete_student.sql", executionPhase = BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:sql/tests/delete_all.sql", executionPhase = AFTER_TEST_METHOD)
    public void getListTest() {
        Optional<Model> student = repository.getStudByFullName("FIRST-NAME", "LAST-NAME");
        student.ifPresent(s -> {
            Student stud = (Student) s;
            List<Long> list = stud.getMarks().stream().map(Model::getId).collect(Collectors.toList());
            Long[] ids = new Long[list.size()];
            for (Long id : list) {
                ids[list.indexOf(id)] = id;
            }
            Optional<List<Model>> optional = repository.getListById(Mark.class, ids);
            optional.ifPresentOrElse(m -> assertTrue(m.size() > 0), () -> fail("optional can't be empty!"));
        });
    }
}