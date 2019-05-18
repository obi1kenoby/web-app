package project.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import project.model.Department;
import project.model.Model;
import project.model.Subject;
import project.repository.ModelRepositoryImpl;

import java.util.*;

import static java.util.stream.Collectors.toList;
import static org.mockito.Mockito.*;


/**
 * Test class for {@link DepartmentService} class.
 *
 * @author Alexander Naumov.
 */
@RunWith(MockitoJUnitRunner.class)
public class DepartmentServiceTest {

    private static Random random = new Random();

    @InjectMocks
    private DepartmentService service;

    @Mock
    private ModelRepositoryImpl repository;

    @Test
    public void getAll() {
        List<Model> list = Arrays.asList(createDepartment(0), createDepartment(0), createDepartment(0));
        when(repository.getList(Department.class)).thenReturn(Optional.of(list));

        List<Model> departments = service.getAll();

        Assert.assertNotNull(departments);
        Assert.assertTrue(!departments.isEmpty());
        departments.forEach(d -> Assert.assertTrue(d instanceof Department));
        verify(repository, only()).getList(Department.class);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void getListById() {
        final Long[] ids = {100L, 200L, 300L};
        List<Model> list = Arrays.stream(ids).map(this::createDepartment).collect(toList());
        when(repository.getListById(Department.class, ids)).thenReturn(Optional.of(list));

        List<Model> departments = service.getListById(ids);

        Assert.assertNotNull(departments);
        Assert.assertTrue(!departments.isEmpty());
        departments.forEach(d -> Assert.assertTrue(d instanceof  Department));
        verify(repository, only()).getListById(Department.class, ids);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void getById() {
        Department department = createDepartment(100L);
        when(repository.getById(Department.class, 100L)).thenReturn(Optional.of(department));

        Model result = service.getById(100L);

        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof Department);
        Assert.assertEquals(result.getId(), 100L);
        verify(repository, only()).getById(Department.class, 100L);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void saveWithSubject() {
        List<Model> subjects = Arrays.asList(createSubject(), createSubject(), createSubject());
        Department department = createDepartment(0);
        doNothing().when(repository).saveOrUpdate(any());

        boolean result = service.saveWithSubject(department, subjects);

        Assert.assertTrue(result);
        verify(repository, times(subjects.size())).saveOrUpdate(any());
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void save() {
        Model department = createDepartment(0);
        doNothing().when(repository).saveOrUpdate(department);

        boolean result = service.save(department);

        Assert.assertTrue(result);
        verify(repository, only()).saveOrUpdate(department);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void deleteById() {
        when(repository.deleteById(Department.class, 100L)).thenReturn(1);

        int result = service.deleteById(100L);

        Assert.assertTrue(result > 0);
        verify(repository, only()).deleteById(Department.class, 100L);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void getByName() {
        final String name = "Special_Name";
        Department department = createDepartment(0);
        department.setName(name);
        when(repository.getDepByName(name)).thenReturn(Optional.of(department));

        Model result = service.getByName(name);

        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof Department);
        Assert.assertEquals(((Department)result).getName(), name);
        verify(repository, only()).getDepByName(name);
        verifyNoMoreInteractions(repository);
    }

    private Department createDepartment(long id) {
        Department department = new Department();
        if (id == 0) {
            department.setId(random.nextInt(1000) + 1);
        } else {
            department.setId(id);
        }
        department.setName("Department" + random.nextInt(1000));
        return department;
    }

    private Subject createSubject() {
        Subject subject = new Subject();
        subject.setId(random.nextInt(1000) + 1);
        subject.setName("Subject" + random.nextInt(1000));
        subject.setDepartments(new HashSet<>());
        return subject;
    }
}