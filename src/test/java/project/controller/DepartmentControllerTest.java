package project.controller;


import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import project.config.ApplicationConfig;
import project.config.DataConfig;
import project.model.Department;
import project.model.Model;
import project.model.Subject;
import project.service.DepartmentService;
import project.service.SubjectService;


import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for controller {@link DepartmentController} class.
 *
 * @author Alexander Naumov.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {ApplicationConfig.class, DataConfig.class})
public class DepartmentControllerTest {

    private static final String PATH = "/api/department";

    private MockMvc mockMvc;

    @Mock
    private DepartmentService departmentService;

    @Mock
    private SubjectService subjectService;

    @InjectMocks
    private DepartmentController controller;

    @Before
    public void setup() {
        if (mockMvc == null) {
            MockitoAnnotations.initMocks(this);
            mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        }
    }

    @Test
    public void listTest() throws Exception {
        List<Model> models = Arrays.asList(createDepartment(1L, "Dep_1"),
        createDepartment(2L, "Dep_2"), createDepartment(3L, "Dep_3"));

        when(departmentService.getAll()).thenReturn(models);

        MvcResult result = mockMvc.perform(get(PATH))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andReturn();

        String body = result.getResponse().getContentAsString();
        String[] objects = body.split(",");
        Assert.assertTrue(objects.length > 0);

        verify(departmentService, only()).getAll();
        verifyNoMoreInteractions(departmentService);
    }

    @Test
    public void saveTest() throws Exception {
        Long[] ids = {1L, 2L};
        Department department = createDepartment(1L, "DepName");
        List<Model> subjects = Arrays.asList(createSubject(ids[0], "Subject_1"),
                createSubject(ids[1], "Subject_2"));

        when(subjectService.getListById(ids)).thenReturn(subjects);
        when(departmentService.saveWithSubject(department, subjects)).thenReturn(true);
        when((departmentService).getByName("DepName")).thenReturn(department);


        mockMvc.perform(post(PATH + "/{name}", "DepName")
                .param("array[]", "1")
                .param("array[]", "2")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andDo(print())
                .andExpect(status().isOk());

        verify(subjectService, only()).getListById(ids);
        verifyNoMoreInteractions(subjectService);
        verify(departmentService, times(1)).saveWithSubject(any(), any());
        verify(departmentService, times(1)).getByName(any());
        verifyNoMoreInteractions(departmentService);
    }

    @Test
    public void getDepartmentByIdTest() throws Exception {
        when(departmentService.getById(1L)).thenReturn(createDepartment(1L, "depName"));

        MvcResult result = this.mockMvc.perform(get(PATH + "/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("depName"))
                .andDo(print())
                .andReturn();

        Assert.assertEquals("application/json;charset=UTF-8", result.getResponse().getContentType());
    }

    @Test
    public void deleteDeparmtnetTest() throws Exception {
        when(departmentService.deleteById(1L)).thenReturn(1);

        mockMvc.perform(delete(PATH + "/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andDo(print())
                .andExpect(status().isOk());

        verify(departmentService, times(1)).deleteById(1L);
        verifyNoMoreInteractions(departmentService);
    }

    private static Department createDepartment(Long id, String name) {
        Department department = new Department();
        department.setId(id);
        department.setName(name);
        return department;
    }

    private static Subject createSubject(Long id, String name) {
        Subject subject = new Subject();
        subject.setId(id);
        subject.setName(name);
        return subject;
    }
}