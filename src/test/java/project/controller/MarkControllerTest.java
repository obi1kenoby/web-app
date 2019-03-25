package project.controller;


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
import project.model.Mark;
import project.model.Student;
import project.model.Subject;
import project.service.MarkService;
import project.service.StudentService;
import project.service.SubjectService;

import java.time.LocalDate;
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
 * @author Alexander Naumov
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ApplicationConfig.class, DataConfig.class})
@WebAppConfiguration
public class MarkControllerTest {

    public static final String path = "/api/mark";

    private MockMvc mockMvc;

    @InjectMocks
    private MarkController markController;

    @Mock
    private MarkService markService;

    @Mock
    private StudentService studentService;

    @Mock
    private SubjectService subjectService;

    @Before
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

        verify(markService, times(1)).getMarksByDateRange(date);
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
    public void deleteMark() throws Exception {
        Mark mark = createMark(1L, 2, LocalDate.of(2001, 1, 1));
        when(markService.deleteById(mark.getId())).thenReturn(true);

        mockMvc.perform(delete(path + "/{id}", mark.getId()))
                .andDo(print())
                .andExpect(status().isOk());

        verify(markService, times(1)).deleteById(mark.getId());
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

    private static Mark createMark(Long id, Integer value, LocalDate date) {
        Mark mark = new Mark();
        mark.setId(id);
        mark.setDate(date);
        mark.setValue(value);
        return mark;
    }

    private static Student createStudent(Long id, String firstName, String lastName) {
        Student student = new Student();
        student.setId(id);
        student.setFirst_name(firstName);
        student.setLast_name(lastName);
        return student;
    }

    private static Subject createSubject(Long id, String name) {
        Subject subject = new Subject();
        subject.setId(id);
        subject.setName(name);
        return subject;
    }
}