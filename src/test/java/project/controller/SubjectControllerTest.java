package project.controller;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import project.model.Faculty;
import project.model.Subject;
import project.service.FacultytService;
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
 * @author Alexander Naumov.
 */
public class SubjectControllerTest extends BaseControllerTest {

    private static final String path = "/api/subject";

    private MockMvc mockMvc;

    @InjectMocks
    private SubjectController controller;

    @Mock
    private SubjectService subjectService;

    @Mock
    private FacultytService facultytService;

    @BeforeEach
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
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
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
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
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
        Faculty fac1 = new Faculty();
        fac1.setId(1L);
        fac1.setName("Fac1");
        Faculty fac2 = new Faculty();
        fac2.setId(2L);
        fac2.setName("Fac2");
        Faculty fac3 = new Faculty();
        fac3.setId(3L);
        fac3.setName("Fac3");

        Subject subject = new Subject();
        subject.setName("Subject");

        Long[] ids = {1L, 2L, 3L};

        when(facultytService.getListById(ids)).thenReturn(Arrays.asList(fac1, fac2, fac3));
        when(subjectService.save(subject)).thenReturn(true);
        when(subjectService.getByName("Subject")).thenReturn(subject);

        mockMvc.perform(post(path + "/{name}", "Subject")
                .param("ids", "1")
                .param("ids", "2")
                .param("ids", "3"))
                .andDo(print())
                .andExpect(status().isCreated());

        verify(facultytService, only()).getListById(ids);
        verifyNoMoreInteractions(facultytService);
        verify(subjectService, times(1)).save(subject);
        verify(subjectService, times(1)).getByName("Subject");
        verifyNoMoreInteractions(subjectService);
    }
}
