package project.controller;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import project.config.ApplicationConfig;
import project.config.DataConfig;

import javax.servlet.ServletContext;
import static junit.framework.TestCase.assertNotNull;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    private MockMvc mockMvc;

//    @Autowired
//    private WebApplicationContext context;
//
//    @Mock
//    private StudentDao studentDao;
//
//    @InjectMocks
//    private StudentController controller;
//
//    @Before
//    public void setup() {
//        MockitoAnnotations.initMocks(this);
//        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
//    }
//
//    @Test
//    public void webAppContextTest() {
//        ServletContext servletContext = context.getServletContext();
//
//        Assert.assertNotNull(servletContext);
//        Assert.assertTrue(servletContext instanceof MockServletContext);
//        Assert.assertNotNull(context.getBean("studentController"));
//    }
//
//    @Test
//    public void getStudentByIdTest() throws Exception {
//        mockMvc.perform(get("/student/{id}", 1))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
//                .andExpect(jsonPath("$.id", is(1)));
//    }
//
//    @Test
//    public void allStudentsTest() throws Exception {
//        mockMvc.perform(get("/students"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
//                .andExpect(jsonPath("$", hasSize(15)));
//    }
//
//    @Test
//    public void deleteByGroupTest() throws Exception {
//        MockMvc mock = MockMvcBuilders.standaloneSetup(controller).build();
//        assertNotNull(mock);
//
//        long[] id = {2, 5};
//        doNothing().when(studentDao).deleteByGroup(id);
//
//        mock.perform(post("/delete-group")
//                .param("array[]", "2")
//                .param("array[]", "5"))
//                .andExpect(status().isOk());
//
//        verify(studentDao, times(1)).deleteByGroup(id);
//        verifyNoMoreInteractions(studentDao);
//    }
//
//    @Test
//    public void studentOfDepartmentTest() throws Exception{
//        mockMvc.perform(get("/{department}/students", 1))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
//                .andExpect(jsonPath("$", hasSize(15)));
//    }
//
//    @Test
//    public void deleteStudentTest() throws Exception{
//        MockMvc mock = MockMvcBuilders.standaloneSetup(controller).build();
//        assertNotNull(mock);
//
//        when(studentDao.deleteById(1)).thenReturn(1);
//
//        mock.perform(get("/student/{id}/delete", 1))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", is(1)));
//
//        verify(studentDao, times(1)).deleteById(1);
//        verifyNoMoreInteractions(studentDao);
//    }
//
//    @Test
//    public void ratingBoardTest() throws Exception{
//        mockMvc.perform(get("/{department}/{subject}/marks", 1L, "Математика"))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    public void addStudentTest(){
//        MockMvc mock = MockMvcBuilders.standaloneSetup(controller).build();
//        assertNotNull(mock);
//    }
}