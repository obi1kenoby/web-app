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
import project.model.Faculty;
import project.model.Model;
import project.service.FacultytService;
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
 * Test class for controller {@link FacultyController} class.
 *
 * @author Alexander Naumov.
 */
public class FacultyControllerTest extends BaseControllerTest {

    private static final String PATH = "/api/faculty";

    private MockMvc mockMvc;

    @Mock
    private FacultytService facultytService;

    @Mock
    private SubjectService subjectService;

    @InjectMocks
    private FacultyController controller;

    @BeforeEach
    public void setup() {
        if (mockMvc == null) {
            MockitoAnnotations.initMocks(this);
            mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        }
    }

    @Test
    public void testList() throws Exception {
        List<Model> models = Arrays.asList(createFaculty(1L, "Fac_1"),
        createFaculty(2L, "Fac_2"), createFaculty(3L, "Fac_3"));

        when(facultytService.getAll()).thenReturn(models);

        MvcResult result = mockMvc.perform(get(PATH))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        String body = result.getResponse().getContentAsString();
        String[] objects = body.split(",");
        assertTrue(objects.length > 0);

        verify(facultytService, only()).getAll();
        verifyNoMoreInteractions(facultytService);
    }

    @Test
    public void testEmptyList() throws Exception {
        List<Model> models = new ArrayList<>();

        when(facultytService.getAll()).thenReturn(models);

        mockMvc.perform(get(PATH))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();

        verify(facultytService, only()).getAll();
        verifyNoMoreInteractions(facultytService);
    }

    @Test
    public void testSave() throws Exception {
        Long[] ids = {1L, 2L};
        Faculty faculty = createFaculty(1L, "FacName");
        List<Model> subjects = Arrays.asList(createSubject(ids[0], "Subject_1"),
                createSubject(ids[1], "Subject_2"));

        when(subjectService.getListById(ids)).thenReturn(subjects);
        when(facultytService.saveWithSubject(faculty, subjects)).thenReturn(true);
        when((facultytService).getByName("FacName")).thenReturn(faculty);


        mockMvc.perform(post(PATH + "/{name}", "FacName")
                .param("array[]", "1")
                .param("array[]", "2")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk());

        verify(subjectService, only()).getListById(ids);
        verifyNoMoreInteractions(subjectService);
        verify(facultytService, times(1)).saveWithSubject(any(), any());
        verify(facultytService, times(1)).getByName(any());
        verifyNoMoreInteractions(facultytService);
    }

    @Test
    public void testNotExistSubjectsSave() throws Exception {
        Long[] ids = {1L, 2L};
        List<Model> subjects = new ArrayList<>();

        when(subjectService.getListById(ids)).thenReturn(subjects);

        mockMvc.perform(post(PATH + "/{name}", "FacName")
                .param("array[]", "1")
                .param("array[]", "2")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(subjectService, only()).getListById(ids);
        verifyNoMoreInteractions(subjectService);
    }

    @Test
    public void testSaveWithEmptyNameFaculty() throws Exception {
        final String facName = "";

        mockMvc.perform(post(PATH + "/{name}", facName)
                .param("array[]", "1")
                .param("array[]", "2")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetById() throws Exception {
        final String facName = "TEST FACULTY";
        final long facId = 1L;
        when(facultytService.getById(1L)).thenReturn(createFaculty(facId, facName));

        MvcResult result = this.mockMvc.perform(get(PATH + "/{id}", facId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(String.valueOf(facId)))
                .andExpect(jsonPath("$.name").value(facName))
                .andDo(print())
                .andReturn();

        assertEquals("application/json", result.getResponse().getContentType());

        verify(facultytService, only()).getById(facId);
        verifyNoMoreInteractions(facultytService);
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
        when(facultytService.deleteById(id)).thenReturn(1);

        mockMvc.perform(delete(PATH + "/{id}", id)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk());

        verify(facultytService, only()).deleteById(id);
        verifyNoMoreInteractions(facultytService);
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
        when(facultytService.deleteById(id)).thenReturn(0);

        mockMvc.perform(delete(PATH + "/{id}", id)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(facultytService, only()).deleteById(id);
        verifyNoMoreInteractions(facultytService);
    }
}