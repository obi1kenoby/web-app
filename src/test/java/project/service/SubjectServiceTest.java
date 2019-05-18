package project.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import project.model.Model;
import project.model.Subject;
import project.repository.ModelRepositoryImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;

/**
 * Test class for {@link SubjectService} class.
 *
 * @author Alexander Naumov.
 */
@RunWith(MockitoJUnitRunner.class)
public class SubjectServiceTest {

    private static Random random = new Random();

    @InjectMocks
    private SubjectService service;

    @Mock
    private ModelRepositoryImpl repository;

    @Test
    public void getAll() {
        List<Model> list = Arrays.asList(createSubject(0), createSubject(0), createSubject(0));
        when(repository.getList(Subject.class)).thenReturn(Optional.of(list));

        List<Model> subjects = service.getAll();

        Assert.assertNotNull(subjects);
        Assert.assertTrue(!subjects.isEmpty());
        subjects.forEach(s -> Assert.assertTrue(s instanceof Subject));
        Assert.assertArrayEquals(list.toArray(), subjects.toArray());
        verify(repository, only()).getList(Subject.class);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void getListById() {
        final Long[] ids = {100L, 200L, 300L};
        List<Model> list = Arrays.stream(ids).map(this::createSubject).collect(Collectors.toList());
        when(repository.getListById(Subject.class, ids)).thenReturn(Optional.of(list));

        List<Model> subjects = service.getListById(ids);

        Assert.assertNotNull(subjects);
        Assert.assertTrue(!subjects.isEmpty());
        subjects.forEach(s -> Assert.assertTrue(s instanceof Subject));
        Assert.assertArrayEquals(list.toArray(), subjects.toArray());
        verify(repository, only()).getListById(Subject.class, ids);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void getById() {
        when(repository.getById(Subject.class, 100L)).thenReturn(Optional.of(createSubject(100L)));

        Model subject = service.getById(100L);

        Assert.assertNotNull(subject);
        Assert.assertTrue(subject instanceof Subject);
        Assert.assertEquals(subject.getId(), 100L);
        verify(repository, only()).getById(Subject.class, 100L);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void deleteById() {
        when(repository.deleteById(Subject.class, 100L)).thenReturn(1);

        int result = service.deleteById(100L);

        Assert.assertTrue(result > 0);
        verify(repository, only()).deleteById(Subject.class, 100L);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void save() {
        Subject subject = createSubject(0);
        doNothing().when(repository).saveOrUpdate(subject);

        boolean result = service.save(subject);

        Assert.assertTrue(result);
        verify(repository, only()).saveOrUpdate(subject);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void getByName() {
        final String name = "Special_Name";
        Subject subject = createSubject(0);
        subject.setName(name);
        when(repository.getSubjectByName(name)).thenReturn(Optional.of(subject));

        Model result = service.getByName(name);

        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof Subject);
        Assert.assertEquals(((Subject) result).getName(), name);
        verify(repository, only()).getSubjectByName(name);
        verifyNoMoreInteractions(repository);
    }

    private Subject createSubject(long id) {
        Subject subject = new Subject();
        if (id == 0) {
            subject.setId(random.nextInt(1000) + 1);
        } else {
            subject.setId(id);
        }
        subject.setName("Subject" + random.nextInt(1000) + 1);
        return subject;
    }
}