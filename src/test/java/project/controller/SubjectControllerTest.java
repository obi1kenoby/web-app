package project.controller;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import project.config.ApplicationConfig;
import project.config.DataConfig;
import project.model.Department;
import project.model.Subject;
import project.service.DepartmentService;
import project.service.SubjectService;

import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for controller {@link SubjectController} class.
 *
 * @author Alexander Naumov
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {ApplicationConfig.class, DataConfig.class})
public class SubjectControllerTest {

    private static final String path = "/api/subject";

    private MockMvc mockMvc;

    @InjectMocks
    private SubjectController controller;

    @Mock
    private SubjectService subjectService;

    @Mock
    private DepartmentService departmentService;

    @Before
    public void setUp() {
        if (mockMvc == null) {
            MockitoAnnotations.initMocks(this);
            mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        }
    }

    @Test
    public void listTest() throws Exception {
        Subject sub1 = new Subject();
        sub1.setId(1L);
        sub1.setName("Sub1");
        Subject sub2 = new Subject();
        sub2.setId(2L);
        sub2.setName("Sub2");
        Subject sub3 = new Subject();
        sub3.setId(3L);
        sub3.setName("Sub3");

        when(subjectService.getAll()).thenReturn(Arrays.asList(sub1, sub2, sub3));
        mockMvc.perform(get(path))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(status().isOk());

        verify(subjectService, only()).getAll();
        verifyNoMoreInteractions(subjectService);
    }

    @Test
    public void getByIdTest() throws Exception {
        Subject subject = new Subject();
        subject.setId(1L);
        subject.setName("Sub1");

        when(subjectService.getById(1L)).thenReturn(subject);

        mockMvc.perform(get(path + "/{id}", 1L))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id", Matchers.is(1)))
                .andExpect(jsonPath("$.name", Matchers.is("Sub1")))
                .andExpect(status().isOk());

        verify(subjectService, only()).getById(1L);
        verifyNoMoreInteractions(subjectService);
    }

    @Test
    public void deleteTest() throws Exception {
        when(subjectService.deleteById(1L)).thenReturn(1);

        mockMvc.perform(delete(path + "/{id}", 1L))
                .andDo(print())
                .andExpect(status().isOk());

        verify(subjectService, only()).deleteById(1L);
        verifyNoMoreInteractions(subjectService);
    }

    @Test
    public void saveTest() throws Exception {
        Department dep1 = new Department();
        dep1.setId(1L);
        dep1.setName("Dep1");
        Department dep2 = new Department();
        dep2.setId(2L);
        dep2.setName("Dep2");
        Department dep3 = new Department();
        dep3.setId(3L);
        dep3.setName("Dep3");

        Subject subject = new Subject();
        subject.setName("Subject");

        Long[] ids = {1L, 2L, 3L};

        when(departmentService.getListById(ids)).thenReturn(Arrays.asList(dep1, dep2, dep3));
        when(subjectService.save(subject)).thenReturn(true);
        when(subjectService.getByName("Subject")).thenReturn(subject);

        mockMvc.perform(post(path + "/{name}", "Subject")
                .param("ids", "1")
                .param("ids", "2")
                .param("ids", "3"))
                .andDo(print())
                .andExpect(status().isCreated());

        verify(departmentService, only()).getListById(ids);
        verifyNoMoreInteractions(departmentService);
        verify(subjectService, times(1)).save(subject);
        verify(subjectService, times(1)).getByName("Subject");
        verifyNoMoreInteractions(subjectService);
    }
}
