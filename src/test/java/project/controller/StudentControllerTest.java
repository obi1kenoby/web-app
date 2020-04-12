package project.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import project.model.Department;
import project.model.Student;
import project.service.DepartmentService;
import project.service.StudentService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

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
 * @author Alexander Naumov.
 */
public class StudentControllerTest extends BaseControllerTest {

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

    @BeforeEach
    public void setup() {
        if (mockMvc == null) {
            MockitoAnnotations.initMocks(this);
            mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        }
    }

    @Test
    public void testList() throws Exception {
        Department dep1 = createDepartment(1L, "TEST DEPARTMENT 1");
        Department dep2 = createDepartment(1L, "TEST DEPARTMENT 2");
        Department dep3 = createDepartment(1L, "TEST DEPARTMENT 3");
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
    public void testListNotFound() throws Exception {
        when(studentService.getAll()).thenReturn(new ArrayList<>());

        mockMvc.perform(get(path))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(studentService, only()).getAll();
        verifyNoMoreInteractions(studentService);
    }

    @Test
    public void testGetById() throws Exception {
        Student student = createStudent(1L, "FIRST NAME", "LAST NAME");
        when(studentService.getById(student.getId())).thenReturn(student);

        mockMvc.perform(get(path + "/{id}", student.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id", is(Long.valueOf(student.getId()).intValue())))
                .andExpect(jsonPath("$.first_name", is(student.getFirst_name())))
                .andExpect(jsonPath("$.last_name", is(student.getLast_name())));

        verify(studentService, only()).getById(student.getId());
        verifyNoMoreInteractions(studentService);
    }

    @Test
    public void testGetByIdNonexistentId() throws Exception {
        final long id = 100L;
        when(studentService.getById(id)).thenReturn(null);

        mockMvc.perform(get(path + "/{id}", id))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(studentService, only()).getById(id);
        verifyNoMoreInteractions(studentService);
    }

    @Test
    public void testGetByIdNegativeId() throws Exception {
        long studentId = -1L;

        mockMvc.perform(get(path + "/{id}", studentId))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDeleteById() throws Exception {
        final long id = 1L;
        when(studentService.deleteById(id)).thenReturn(1);

        mockMvc.perform(delete(path + "/{id}", id))
                .andDo(print())
                .andExpect(status().isOk());

        verify(studentService, only()).deleteById(id);
        verifyNoMoreInteractions(studentService);
    }

    @Test
    public void testDeleteByIdIncorrectId() throws Exception {
        final long id = -1L;

        mockMvc.perform(delete(path + "/{id}", id))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDeleteByIdBadRequest() throws Exception {
        final long id = 100L;
        when(studentService.deleteById(id)).thenReturn(0);

        mockMvc.perform(delete(path + "/{id}", id))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(studentService, only()).deleteById(id);
        verifyNoMoreInteractions(studentService);
    }

    @Test
    public void testSaveSuccessful() throws Exception {
        Department department = new Department();
        department.setId(1L);
        department.setName("Dep");
        Student student = new Student();
        student.setFirst_name("Firstname");
        student.setLast_name("Lastname");
        student.setPassword("password");
        student.setEmail("email@gmail.com");
        when(departmentService.getById(1L)).thenReturn(department);
        when(studentService.save(student)).thenReturn(true);

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

    @Test
    public void getStudentsByDep() throws Exception {
        Student student1 = createStudent(1L, "FIRST NAME 1", "LAST NAME 1");
        Student student2 = createStudent(2L, "FIRST NAME 2", "LAST NAME 2");
        Student student3 = createStudent(3L, "FIRST NAME 3", "LAST NAME 3");

        Department department = createDepartment(1L, "TEST DEPARTMENT");
        department.setStudents(new HashSet<>(Arrays.asList(student1, student2, student3)));

        when(departmentService.getById(department.getId())).thenReturn(department);

        mockMvc.perform(get(path + "/department/{id}", department.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(3)));

        verify(departmentService, only()).getById(department.getId());
        verifyNoMoreInteractions(studentService);
    }

    @Test
    public void getStudentsByDepIncorrectId() throws Exception {
        final long id = 0;

        mockMvc.perform(get(path + "/department/{id}", id))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}