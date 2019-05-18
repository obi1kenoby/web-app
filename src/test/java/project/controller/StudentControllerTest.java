package project.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import project.config.ApplicationConfig;
import project.config.DataConfig;
import project.model.Department;
import project.model.Student;
import project.service.DepartmentService;
import project.service.StudentService;

import java.util.Arrays;

import static junit.framework.TestCase.assertNotNull;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for controller {@link StudentController} class.
 *
 * @author Alexander Naumov
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {ApplicationConfig.class, DataConfig.class})
public class StudentControllerTest {

    private static final String path = "/api/student";

    private MockMvc mockMvc;

    @InjectMocks
    private StudentController controller;

    @Mock
    private StudentService studentService;

    @Mock
    private DepartmentService departmentService;

    @Spy
    private BCryptPasswordEncoder encoder;

    @Before
    public void setup() {
        if (mockMvc == null) {
            MockitoAnnotations.initMocks(this);
            mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        }
    }

    @Test
    public void getStudentByIdTest() throws Exception {
        Student student = new Student();
        student.setId(1L);
        student.setFirst_name("Firstname");
        student.setLast_name("Lastname");
        Department department = new Department();
        department.setId(1L);
        department.setName("Department");

        when(studentService.getById(1L)).thenReturn(student);

        mockMvc.perform(get(path + "/{id}", 1))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id", is(1)));

        verify(studentService, only()).getById(1L);
        verifyNoMoreInteractions(studentService);
    }

    @Test
    public void allTest() throws Exception {
        Department dep1 = new Department("Dep_1");
        dep1.setId(1);
        Department dep2 = new Department("Dep_2");
        dep2.setId(2);
        Department dep3 = new Department("Dep_3");
        dep3.setId(3);


        when(studentService.getAll()).thenReturn(Arrays.asList(dep1, dep2, dep3));

        mockMvc.perform(get(path))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(3)));

        verify(studentService, only()).getAll();
        verifyNoMoreInteractions(studentService);
    }

    @Test
    public void deleteTest() throws Exception {
        MockMvc mock = MockMvcBuilders.standaloneSetup(controller).build();
        assertNotNull(mock);

        Long id = 1L;
        when(studentService.deleteById(id)).thenReturn(1);

        mock.perform(delete(path + "/{id}", id))
                .andDo(print())
                .andExpect(status().isOk());

        verify(studentService, only()).deleteById(id);
        verifyNoMoreInteractions(studentService);
    }

    @Test
    public void saveTest() throws Exception {
        Department department = new Department();
        department.setId(1L);
        department.setName("Dep");
        when(departmentService.getById(1L)).thenReturn(department);

        when(studentService.save(any())).thenReturn(true);

        Student student = new Student();
        student.setFirst_name("Firstname");
        student.setLast_name("Lastname");
        student.setPassword("password");
        student.setEmail("email@gmail.com");

        MockMultipartFile file = new MockMultipartFile("file", "", "application/json", "{\"key1\": \"value1\"}".getBytes());

        mockMvc.perform(MockMvcRequestBuilders.fileUpload(path)
                .file(file)
                .flashAttr("student", student)
                .param("day", "01")
                .param("month", "01")
                .param("year", "2001")
                .param("depId", "1"))
                .andDo(print())
                .andExpect(status().isCreated());

        verify(departmentService, only()).getById(1L);
        verify(studentService, only()).save(any());
        verifyNoMoreInteractions(departmentService);
        verifyNoMoreInteractions(studentService);
    }
}