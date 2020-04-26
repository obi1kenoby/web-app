package project.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import project.model.Faculty;
import project.model.Model;
import project.model.Subject;
import project.repository.ModelRepositoryImpl;

import java.util.*;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;


/**
 * Test class for {@link FacultyService} class.
 *
 * @author Alexander Naumov.
 */
@ExtendWith(MockitoExtension.class)
public class FacultyServiceTest {

    private static Random random = new Random();

    @InjectMocks
    private FacultyService service;

    @Mock
    private ModelRepositoryImpl repository;

    @Test
    public void getAll() {
        List<Model> list = Arrays.asList(createFaculty(0), createFaculty(0), createFaculty(0));
        when(repository.getList(Faculty.class)).thenReturn(Optional.of(list));

        List<Model> faculties = service.getAll();

        assertNotNull(faculties);
        assertTrue(!faculties.isEmpty());
        faculties.forEach(d -> assertTrue(d instanceof Faculty));
        verify(repository, only()).getList(Faculty.class);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void getListById() {
        final Long[] ids = {100L, 200L, 300L};
        List<Model> list = Arrays.stream(ids).map(this::createFaculty).collect(toList());
        when(repository.getListById(Faculty.class, ids)).thenReturn(Optional.of(list));

        List<Model> faculties = service.getListById(ids);

        assertNotNull(faculties);
        assertTrue(!faculties.isEmpty());
        faculties.forEach(d -> assertTrue(d instanceof Faculty));
        verify(repository, only()).getListById(Faculty.class, ids);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void testGetById() {
        Faculty faculty = createFaculty(100L);
        when(repository.getById(Faculty.class, 100L)).thenReturn(Optional.of(faculty));

        Model result = service.getById(100L);

        assertNotNull(result);
        assertTrue(result instanceof Faculty);
        assertEquals(result.getId(), 100L);
        verify(repository, only()).getById(Faculty.class, 100L);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void saveWithSubject() {
        List<Model> subjects = Arrays.asList(createSubject(), createSubject(), createSubject());
        Faculty faculty = createFaculty(0);
        doNothing().when(repository).saveOrUpdate(any());

        boolean result = service.saveWithSubject(faculty, subjects);

        assertTrue(result);
        verify(repository, times(subjects.size())).saveOrUpdate(any());
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void save() {
        Model faculties = createFaculty(0);
        doNothing().when(repository).saveOrUpdate(faculties);

        boolean result = service.save(faculties);

        assertTrue(result);
        verify(repository, only()).saveOrUpdate(faculties);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void deleteById() {
        when(repository.deleteById(Faculty.class, 100L)).thenReturn(1);

        int result = service.deleteById(100L);

        assertTrue(result > 0);
        verify(repository, only()).deleteById(Faculty.class, 100L);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void getByName() {
        final String name = "Special_Name";
        Faculty faculty = createFaculty(0);
        faculty.setName(name);
        when(repository.getFacByName(name)).thenReturn(Optional.of(faculty));

        Model result = service.getByName(name);

        assertNotNull(result);
        assertTrue(result instanceof Faculty);
        assertEquals(((Faculty)result).getName(), name);
        verify(repository, only()).getFacByName(name);
        verifyNoMoreInteractions(repository);
    }

    private Faculty createFaculty(long id) {
        Faculty faculty = new Faculty();
        if (id == 0) {
            faculty.setId(random.nextInt(1000) + 1);
        } else {
            faculty.setId(id);
        }
        faculty.setName("Faculties" + random.nextInt(1000));
        return faculty;
    }

    private Subject createSubject() {
        Subject subject = new Subject();
        subject.setId(random.nextInt(1000) + 1);
        subject.setName("Subject" + random.nextInt(1000));
        subject.setFaculties(new HashSet<>());
        return subject;
    }
}