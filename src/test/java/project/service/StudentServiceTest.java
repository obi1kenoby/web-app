package project.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import project.model.Model;
import project.model.Student;
import project.repository.ModelRepositoryImpl;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link StudentService} class.
 *
 * @author Alexander Naumov.
 */
@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    private static Random random = new Random();

    @InjectMocks
    private StudentService service;

    @Mock
    private ModelRepositoryImpl repository;

    @Test
    public void getAll() {
        List<Model> list = Arrays.asList(createStudent(0), createStudent(0), createStudent(0));
        when(repository.getList(Student.class)).thenReturn(Optional.of(list));

        List<Model> students = service.getAll();

        assertNotNull(students);
        assertTrue(!students.isEmpty());
        assertArrayEquals(list.toArray(), students.toArray());
        verify(repository, only()).getList(Student.class);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void getById() {
        when(repository.getById(Student.class, 100L)).thenReturn(Optional.of(createStudent(100L)));

        Model student = service.getById(100L);

        assertNotNull(student);
        assertEquals(student.getId(), 100L);
        assertTrue(student instanceof Student);
        verify(repository, only()).getById(Student.class, 100L);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void save() {
        Student student = createStudent(0);
        doNothing().when(repository).saveOrUpdate(student);

        service.save(student);

        verify(repository, only()).saveOrUpdate(student);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void deleteById() {
        when(repository.deleteById(Student.class, 100L)).thenReturn(1);

        int res = service.deleteById(100L);

        assertTrue(res > 0);
        verify(repository, only()).deleteById(Student.class, 100L);
        verifyNoMoreInteractions(repository);
    }

    private Student createStudent(long id) {
        Student student = new Student();
        if (id == 0) {
            student.setId(random.nextInt(1000) + 1);
        } else {
            student.setId(id);
        }
        student.setFirst_name("FirstName" + random.nextInt(1000));
        student.setLast_name("LastName" + random.nextInt(1000));
        student.setEmail("email." + random.nextInt(1000) + "@gmail.com");
        student.setPassword("****************");
        student.setBirthday(LocalDate.of(2001, random.nextInt(12) + 1, random.nextInt(25) + 1));
        return student;
    }
}