package project.controller;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import project.model.Department;
import project.model.Model;
import project.service.DepartmentService;
import project.service.SubjectService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * Test class for controller {@link DepartmentController} class.
 *
 * @author Alexander Naumov.
 */
public class DepartmentControllerTest extends BaseControllerTest {

    private static final String PATH = "/api/department";

    private MockMvc mockMvc;

    @Mock
    private DepartmentService departmentService;

    @Mock
    private SubjectService subjectService;

    @InjectMocks
    private DepartmentController controller;

    @BeforeEach
    public void setup() {
        if (mockMvc == null) {
            MockitoAnnotations.initMocks(this);
            mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        }
    }

    @Test
    public void testList() throws Exception {
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
        assertTrue(objects.length > 0);

        verify(departmentService, only()).getAll();
        verifyNoMoreInteractions(departmentService);
    }

    @Test
    public void testEmptyList() throws Exception {
        List<Model> models = new ArrayList<>();

        when(departmentService.getAll()).thenReturn(models);

        mockMvc.perform(get(PATH))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();

        verify(departmentService, only()).getAll();
        verifyNoMoreInteractions(departmentService);
    }

    @Test
    public void testSave() throws Exception {
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
    public void testNotExistSubjectsSave() throws Exception {
        Long[] ids = {1L, 2L};
        List<Model> subjects = new ArrayList<>();

        when(subjectService.getListById(ids)).thenReturn(subjects);

        mockMvc.perform(post(PATH + "/{name}", "DepName")
                .param("array[]", "1")
                .param("array[]", "2")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(subjectService, only()).getListById(ids);
        verifyNoMoreInteractions(subjectService);
    }

    @Test
    public void testSaveWithEmptyNameDepartment() throws Exception {
        final String depName = "";

        mockMvc.perform(post(PATH + "/{name}", depName)
                .param("array[]", "1")
                .param("array[]", "2")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetById() throws Exception {
        final String depName = "TEST DEPARTMENT";
        final long depId = 1L;
        when(departmentService.getById(1L)).thenReturn(createDepartment(depId, depName));

        MvcResult result = this.mockMvc.perform(get(PATH + "/{id}", depId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id").value(String.valueOf(depId)))
                .andExpect(jsonPath("$.name").value(depName))
                .andDo(print())
                .andReturn();

        assertEquals("application/json;charset=UTF-8", result.getResponse().getContentType());

        verify(departmentService, only()).getById(depId);
        verifyNoMoreInteractions(departmentService);
    }

    @Test
    public void testGetByIdNegativeId() throws Exception {
        this.mockMvc.perform(get(PATH + "/{id}", -1))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetByIdNonexistentId() throws Exception {
        this.mockMvc.perform(get(PATH + "/{id}", 100))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteById() throws Exception {
        final long id = 1L;
        when(departmentService.deleteById(id)).thenReturn(1);

        mockMvc.perform(delete(PATH + "/{id}", id)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andDo(print())
                .andExpect(status().isOk());

        verify(departmentService, only()).deleteById(id);
        verifyNoMoreInteractions(departmentService);
    }

    @Test
    public void testDeleteByIdNegativeId() throws Exception {
        mockMvc.perform(delete(PATH + "/{id}", -1L))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDeleteByIdNonexistentId() throws Exception {
        final long id = 100L;
        when(departmentService.deleteById(id)).thenReturn(0);

        mockMvc.perform(delete(PATH + "/{id}", id)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(departmentService, only()).deleteById(id);
        verifyNoMoreInteractions(departmentService);
    }
}