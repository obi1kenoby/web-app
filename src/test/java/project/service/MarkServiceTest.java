package project.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import project.model.Mark;
import project.model.Model;
import project.repository.ModelRepositoryImpl;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.mockito.Mockito.*;

/**
 * Test class for {@link MarkService} class.
 *
 * @author Alexander Naumov.
 */
@RunWith(MockitoJUnitRunner.class)
public class MarkServiceTest {

    @InjectMocks
    private MarkService service;

    @Mock
    private ModelRepositoryImpl repository;

    @Test
    public void getById() {
        Mark mark = Mark.builder().date(LocalDate.of(2001, 1, 1)).value(1).build();
        mark.setId(1L);
        when(repository.getById(Mark.class, 1L)).thenReturn(Optional.of(mark));

        Model model = service.getById(1L);

        Assert.assertNotNull(model);
        Assert.assertEquals(model.getId(), 1L);
        Assert.assertTrue(model instanceof Mark);
        verify(repository, only()).getById(Mark.class, 1L);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void getByIdWithError() {
        when(repository.getById(Mark.class, -1L)).thenThrow(new RuntimeException());

        Model model = service.getById(-1L);

        Assert.assertNull(model);
        verify(repository, only()).getById(Mark.class, -1L);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void getAll() {
        List<Model> marks = Arrays.asList(createMark(0), createMark(0), createMark(0));
        when(repository.getList(Mark.class)).thenReturn(Optional.of(marks));

        List<Model> res = service.getAll();

        res.forEach(m -> Assert.assertTrue(m instanceof Mark));
        Assert.assertNotNull(marks);
        Assert.assertEquals(marks.size(), res.size());
        verify(repository, only()).getList(Mark.class);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void deleteById() {
        when(repository.deleteById(Mark.class, 1L)).thenReturn(1);

        int result = service.deleteById(1L);

        Assert.assertEquals(result, 1);
        verify(repository).deleteById(Mark.class, 1L);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void save() {
        Mark mark = createMark(0);
        doNothing().when(repository).saveOrUpdate(mark);

        boolean res = service.save(mark);

        Assert.assertTrue(res);
        verify(repository, only()).saveOrUpdate(mark);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void getMarksByDateRange() {
        int mon = 4;
        List<Model> models = Arrays.asList(createMark(mon), createMark(mon), createMark(mon));
        when(repository.getList(Mark.class)).thenReturn(Optional.of(models));

        List<Model> marks = service.getMarksByDateRange(LocalDate.of(2001, mon, 1));

        Assert.assertNotNull(marks);
        Assert.assertEquals(marks.size(), models.size());
        marks.forEach(m -> Assert.assertTrue(((Mark)m).getDate().getMonthValue() == mon));
        verify(repository).getList(Mark.class);
        verifyNoMoreInteractions(repository);
    }

    private Mark createMark(int mon) {
        Random random = new Random();
        Mark mark = new Mark();
        mark.setId(random.nextInt(9) + 1);
        mark.setValue(random.nextInt(4) + 1);
        if (mon == 0) {
            mark.setDate(LocalDate.of(2001, (random.nextInt(12) + 1), random.nextInt(30) + 1));
        } else {
            mark.setDate(LocalDate.of(2001, mon, random.nextInt(25) + 1));
        }
        return mark;
    }
}
