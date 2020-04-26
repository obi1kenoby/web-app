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
    public void getFacByNameNonexistentFacTest() {
        final String wrongName = "NON EXISTENT NAME";
        Optional<Model> optional = repository.getFacByName(wrongName);
        if (optional.isPresent()) {
            fail("optional must be empty!");
        }
    }

    @Test
    @Sql(scripts = "classpath:sql/tests/add_identical_faculties.sql", executionPhase = BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:sql/tests/delete_all.sql", executionPhase = AFTER_TEST_METHOD)
    public void getFacByNameMultipleResult() {
        final String name = "NON UNIQUE NAME";
        NonUniqueResultException exception = assertThrows(NonUniqueResultException.class,
                () -> repository.getFacByName(name));
        assertEquals("query did not return a unique result: 2", exception.getMessage());
    }

    @Test
    @Sql(scripts = "classpath:sql/tests/delete_student.sql", executionPhase = BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:sql/tests/delete_all.sql", executionPhase = AFTER_TEST_METHOD)
    public void deleteMarkByIdTest() {
        repository.deleteById(Grade.class, 1L);
        Optional<Model> optional = repository.getById(Grade.class, 1L);
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
    @Sql(scripts = "classpath:sql/tests/delete_faculty.sql", executionPhase = BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:sql/tests/delete_all.sql", executionPhase = AFTER_TEST_METHOD)
    public void deleteFacultyByIdTest() {
        Optional<Model> optional = repository.getFacByName("FACULTY");
        long id = 0;
        if (optional.isPresent()) {
            id = optional.get().getId();
        }
        repository.deleteById(Faculty.class, id);
        optional = repository.getById(Faculty.class, id);
        if (optional.isPresent()) {
            fail("optional must be empty!");
        }
    }

    @Test
    public void deleteNonexistentFacTest() {
        int result = repository.deleteById(Faculty.class, 4L);
        assertEquals(0, result);
    }

    @Test
    public void deleteNonexistentStudTest() {
        int result = repository.deleteById(Student.class, 4L);
        assertEquals(0, result);
    }

    @Test
    @Sql(scripts = "classpath:sql/tests/add_faculty.sql", executionPhase = BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:sql/tests/delete_all.sql", executionPhase = AFTER_TEST_METHOD)
    public void saveFacultyTest() {
        Faculty expected = new Faculty("FACULTY");
        Optional<List<Model>> optional = repository.getList(Subject.class);
        optional.ifPresent(list -> list.forEach(m -> {
            Subject subject = (Subject) m;
            subject.getFaculties().add(expected);
            repository.saveOrUpdate(subject);
        }));
        Optional<Model> optional1 = repository.getFacByName("FACULTY");
        optional1.ifPresent(m -> {
            Faculty actual = (Faculty) m;
            assertEquals(expected.getName(), actual.getName());
            assertTrue(actual.getSubjects().size() > 0);
        });
    }

    @Test
    @Sql(scripts = "classpath:sql/tests/add_subject.sql", executionPhase = BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:sql/tests/delete_all.sql", executionPhase = AFTER_TEST_METHOD)
    public void saveSubjectTest() {
        Subject expected = new Subject("SUBJECT");
        Optional<List<Model>> optional = repository.getList(Faculty.class);
        optional.ifPresent(list -> list.forEach(i -> expected.getFaculties().add((Faculty) i)));
        repository.saveOrUpdate(expected);
        Optional<Model> optional1 = repository.getSubjectByName("SUBJECT");
        optional1.ifPresentOrElse(n -> {
            Subject actual = (Subject) n;
            assertEquals(expected.getName(), actual.getName());
            assertTrue(actual.getFaculties().size() > 0);
        }, () -> fail("optional can't be empty!"));
    }

    @Test
    @Sql(scripts = "classpath:sql/tests/add_student.sql", executionPhase = BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:sql/tests/delete_all.sql", executionPhase = AFTER_TEST_METHOD)
    public void saveStudentTest() {
        Optional<Model> optional = repository.getFacByName("FACULTY");
        if (optional.isPresent()) {
            Faculty faculty = (Faculty) optional.get();
            Student expected = new Student();
            expected.setFirst_name("FIRST-NAME");
            expected.setLast_name("LAST-NAME");
            expected.setPassword("PASSWORD");
            expected.setEmail("EMAIL");
            expected.setRole(Role.USER);
            expected.setBirthday(LocalDate.of(2001, 4, 14));
            expected.setFaculty(faculty);
            repository.saveOrUpdate(expected);
            Optional<Model> optional1 = repository.getStudByFullName("FIRST-NAME", "LAST-NAME");
            optional1.ifPresentOrElse(m -> {
                Student actual = (Student) m;
                assertEquals(expected.getFirst_name(), actual.getFirst_name());
                assertEquals(expected.getLast_name(), actual.getLast_name());
                assertNotNull(expected.getFaculty());
            }, () -> fail("optional can't be empty!"));
        }
    }

    @Test
    @Sql(scripts = "classpath:sql/tests/add_grade.sql", executionPhase = BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:sql/tests/delete_all.sql", executionPhase = AFTER_TEST_METHOD)
    public void saveMarkTest() {
        Optional<Model> opStudent = repository.getStudByFullName("FIRST-NAME", "LAST-NAME");
        Optional<Model> opSubject = repository.getSubjectByName("SUBJECT");
        Grade grade = new Grade();
        grade.setValue(55.0);
        grade.setGrade(GradeValue.getValueFromPercent(55.0));
        grade.setDate(LocalDate.now());
        opSubject.ifPresent(model -> grade.setSubject((Subject) model));
        opStudent.ifPresent(model -> grade.setStudent((Student) model));
        repository.saveOrUpdate(grade);
        Optional<List<Model>> opMark = repository.getList(Grade.class);
        opMark.ifPresentOrElse(m -> m.forEach(i -> {
            Grade mark1 = (Grade)i ;
            assertEquals(grade.getValue(), mark1.getValue());
            assertEquals(grade.getDate(), mark1.getDate());
        }), () -> fail("optional can't be empty!"));
    }

    @Test
    @Sql(scripts = "classpath:sql/tests/add_student.sql", executionPhase = BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:sql/tests/delete_all.sql", executionPhase = AFTER_TEST_METHOD)
    public void getFacByNameTest() {
        Optional<Model> optional = repository.getFacByName("FACULTY");
        optional.ifPresentOrElse(m -> {
            Faculty faculty = (Faculty) m;
            assertEquals("FACULTY", faculty.getName());
        }, () -> fail("optional can't be empty!"));
    }

    @Test
    @Sql(scripts = "classpath:sql/tests/add_faculty.sql", executionPhase = BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:sql/tests/delete_all.sql", executionPhase = AFTER_TEST_METHOD)
    public void getSubjectByNameTest() {
        Optional<Model> optional = repository.getSubjectByName("SUBJECT-1");
        optional.ifPresentOrElse(m -> {
            Subject subject = (Subject) m;
            assertEquals("SUBJECT-1", subject.getName());
        }, () -> fail("optional can't be empty!"));
    }

    @Test
    @Sql(scripts = "classpath:sql/tests/add_grade.sql", executionPhase = BEFORE_TEST_METHOD)
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
    @Sql(scripts = "classpath:sql/tests/add_grade.sql", executionPhase = BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:sql/tests/delete_all.sql", executionPhase = AFTER_TEST_METHOD)
    public void getStudByEmailTest() {
        Optional<Model> optional = repository.getStudByEmail("EMAIL");
        optional.ifPresentOrElse(m -> {
            Student student = (Student) m;
            assertEquals("EMAIL", student.getEmail());
        }, () -> fail("optional can't be empty!"));
    }

    @Test
    @Sql(scripts = "classpath:sql/tests/add_grade.sql", executionPhase = BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:sql/tests/delete_all.sql", executionPhase = AFTER_TEST_METHOD)
    public void getStudsByFacIdTest() {
        Optional<Model> opFaculty = repository.getFacByName("TEST_FACULTY");
        long id = 0;
        if (opFaculty.isPresent()) {
            id = opFaculty.get().getId();
        }
        Optional<List<Model>> optional = repository.getStudsByFucId(id);
        long facId = id;
        optional.ifPresentOrElse(m -> {
            assertTrue(m.size() > 0);
            m.forEach(i -> {
                Student student = (Student) i;
                assertEquals(facId, student.getFaculty().getId());
            });
        }, () -> fail("optional can't be empty!"));
    }

    @Test
    @Sql(scripts = "classpath:sql/tests/delete_student.sql", executionPhase = BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:sql/tests/delete_all.sql", executionPhase = AFTER_TEST_METHOD)
    public void getMarksForMonthTest() {
        Optional<Model> optional = repository.getFacByName("FACULTY");
        long id = 0;
        if (optional.isPresent()) {
            id = optional.get().getId();
        }
        Optional<List<Model>> listOptional = repository.getGradesForMonth(id, "SUBJECT", LocalDate.of(2018, 4, 1), LocalDate.of(2018, 5, 1));
        listOptional.ifPresentOrElse(m -> {
            assertEquals(2, m.size());
            m.forEach(i -> {
                Grade mark = (Grade) i;
                assertTrue(mark.getDate().isAfter(LocalDate.of(2018, 4, 1).minusDays(1)) && mark.getDate().isBefore(LocalDate.of(2018, 5, 1)));
            });
        }, () ->  fail("optional can't be empty!"));
    }

    @Test
    @Sql(scripts = "classpath:sql/tests/add_student.sql", executionPhase = BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:sql/tests/delete_all.sql", executionPhase = AFTER_TEST_METHOD)
    public void getByIdTest() {
        Optional<Model> optional = repository.getById(Faculty.class, repository.getFacByName("FACULTY").get().getId());
        optional.ifPresentOrElse(m -> m.getId(), () -> fail("optional can't be empty!"));
    }

    @Test
    @Sql(scripts = "classpath:sql/tests/delete_faculty.sql", executionPhase = BEFORE_TEST_METHOD)
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
            List<Long> list = stud.getGrades().stream().map(Model::getId).collect(Collectors.toList());
            Long[] ids = new Long[list.size()];
            for (Long id : list) {
                ids[list.indexOf(id)] = id;
            }
            Optional<List<Model>> optional = repository.getListById(Grade.class, ids);
            optional.ifPresentOrElse(m -> assertTrue(m.size() > 0), () -> fail("optional can't be empty!"));
        });
    }
}