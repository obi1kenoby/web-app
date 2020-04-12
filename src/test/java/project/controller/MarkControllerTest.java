package project.controller;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import project.model.Mark;
import project.model.Student;
import project.model.Subject;
import project.service.MarkService;
import project.service.StudentService;
import project.service.SubjectService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for controller {@link MarkController} class.
 *
 * @author Alexander Naumov.
 */
public class MarkControllerTest extends BaseControllerTest {

    private static final String path = "/api/mark";

    private MockMvc mockMvc;

    @InjectMocks
    private MarkController markController;

    @Mock
    private MarkService markService;

    @Mock
    private StudentService studentService;

    @Mock
    private SubjectService subjectService;

    @BeforeEach
    public void init() {
        if (mockMvc == null) {
            MockitoAnnotations.initMocks(this);
            mockMvc = MockMvcBuilders.standaloneSetup(markController).build();
        }
    }

    @Test
    public void getMarksByDateRangeTest() throws Exception {
        LocalDate date = LocalDate.of(2001, 1, 1);
        when(markService.getMarksByDateRange(date))
                .thenReturn(Arrays.asList(createMark(1L, 4, LocalDate.of(2001, 1, 1)),
                        createMark(2L, 3, LocalDate.of(2001, 1, 2)),
                        createMark(3L, 4, LocalDate.of(2001, 1, 1))));

        mockMvc.perform(get(path).param("date", "2001-01-01"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$", hasSize(3)));

        verify(markService, only()).getMarksByDateRange(date);
        verifyNoMoreInteractions(markService);
    }

    @Test
    public void getMarksByDateRangeBadDate() throws Exception {
        final String[] dates = {"$3sf5", "2001/11/01", "2001-dec-31"};
        for (String date: dates) {
            mockMvc.perform(get(path).param("date", date))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }
    }

    @Test
    public void getMarksByDateRangeMarksNotFound() throws Exception {
        final String date = "1999-01-01";
        final LocalDate localDate = LocalDate.of(1999, 1, 1);
        when(markService.getMarksByDateRange(localDate))
                .thenReturn(new ArrayList<>());

        mockMvc.perform(get(path).param("date", date))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(markService, only()).getMarksByDateRange(localDate);
        verifyNoMoreInteractions(markService);
    }

    @Test
    public void getMarkByIdTest() throws Exception {
        when(markService.getById(1L)).thenReturn(createMark(1L, 5, LocalDate.of(2001, 1, 1)));

        mockMvc.perform(get(path + "/{id}", 1))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.value").value("5"));

        verify(markService, times(1)).getById(1L);
        verifyNoMoreInteractions(markService);
    }

    @Test
    public void getMarkByIdIncorrectId() throws Exception {
        final Long[] ids = {-1L, 0L, null};

        for (Long id : ids) {
            mockMvc.perform(get(path + "/{id}", id))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }
    }

    @Test
    public void getMarkByIdNonexistentMark() throws Exception {
        final long id = 100L;
        when(markService.getById(id)).thenReturn(null);

        mockMvc.perform(get(path + "/{id}", id))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(markService, only()).getById(id);
        verifyNoMoreInteractions(markService);
    }

    @Test
    public void deleteMarkById() throws Exception {
        Mark mark = createMark(1L, 2, LocalDate.of(2001, 1, 1));
        when(markService.deleteById(mark.getId())).thenReturn(1);

        mockMvc.perform(delete(path + "/{id}", mark.getId()))
                .andDo(print())
                .andExpect(status().isOk());

        verify(markService, only()).deleteById(mark.getId());
        verifyNoMoreInteractions(markService);
    }

    @Test
    public void deleteMarkByIdIncorrectId() throws Exception {
        final Long[] ids = {-1L, 0L};

        for (Long id : ids) {
            mockMvc.perform(delete(path + "/{id}", id))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }
    }

    @Test
    public void deleteMarkByIdNonexistentMark() throws Exception {
        final long id = 100L;
        when(markService.deleteById(id)).thenReturn(0);

        mockMvc.perform(delete(path + "/{id}", id))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(markService, only()).deleteById(id);
        verifyNoMoreInteractions(markService);
    }

    @Test
    public void saveMark() throws Exception {
        Mark mark = createMark(1L, 3, LocalDate.of(2001, 1, 1));
        Student student = createStudent(1L, "firstName", "lastName");
        Subject subject = createSubject(1L, "subject");
        mark.setStudent(student);
        mark.setSubject(subject);
        when(markService.save(mark)).thenReturn(true);
        when(subjectService.getById(1L)).thenReturn(subject);
        when(studentService.getById(1L)).thenReturn(student);

        mockMvc.perform(post(path)
                .param("id", "1")
                .param("value", "3")
                .param("date", "2001-01-01")
                .param("stud_id", "1")
                .param("sub_id", "1"))
                .andDo(print())
                .andExpect(status().isCreated());

        mockMvc.perform(post(path)
                .param("id", "0")
                .param("value", "0")
                .param("date", "2002-03-08")
                .param("stud_id", "2")
                .param("sub_id", "4"))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(markService, times(1)).save(mark);
        verify(subjectService, times(1)).getById(1L);
        verify(studentService, times(1)).getById(1L);

        verifyNoMoreInteractions(markService);
        verifyNoMoreInteractions(studentService);
        verifyNoMoreInteractions(subjectService);
    }
}