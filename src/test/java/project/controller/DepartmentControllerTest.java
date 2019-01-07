package project.controller;


import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import project.config.ApplicationConfig;
import project.config.DataConfig;
import javax.servlet.ServletContext;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    @Autowired
    private WebApplicationContext context;

    @Before
    public void setup() {
        if (mockMvc == null) {
            mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        }
    }

    @Test
    public void webAppContextTest() {
        ServletContext servletContext = context.getServletContext();
        Assert.assertNotNull(servletContext);
        Assert.assertTrue(servletContext instanceof MockServletContext);
        Assert.assertNotNull(context.getBean("departmentController"));
    }

    /**
     * OK
     */
    @Test
    @Sql(scripts = "classpath:sql/tests/department-list-before.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:sql/tests/department-list-after.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void listTest() throws Exception {
        MvcResult result = mockMvc.perform(get(PATH))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andReturn();

        String body = result.getResponse().getContentAsString();
        String[] objects = body.split(",");
        Assert.assertTrue(objects.length > 0);
    }

    /**
     * OK
     */
    @Test
    @Sql(scripts = "classpath:sql/tests/save-department-before.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void saveTest() throws Exception {
        mockMvc.perform(post( PATH + "/{name}", "TEMP")
                .param("array[]", "995")
                .param("array[]", "996")
                .param("array[]", "997")
                .param("array[]", "998")
                .param("array[]", "999")
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk());
    }

    /**
     * OK
     */
    @Test
    @Sql(scripts = "classpath:sql/tests/get-department-by-name-before.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:sql/tests/get-department-by-name-after.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getDepartmentByIdTest() throws Exception {
        mockMvc.perform(get(PATH + "/{id}", 999))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id", is(999)));
    }
}