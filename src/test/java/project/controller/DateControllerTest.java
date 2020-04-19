package project.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * Test class for {@link DateController} class.
 *
 * @author Alexander Naumov.
 */
public class DateControllerTest extends BaseControllerTest {

    private static final String path = "/api/date";

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webAppContext;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
    }

    @Test
    public void testSingleDate() throws Exception {
        final String date = "2001-01-01";

        mockMvc.perform(get(path)
        .param("date", date))
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(23)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    public void testWrongFormat() throws Exception {
        final String date = "2001/11/21";

        mockMvc.perform(get(path)
                .param("date", date))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testEmptyArgument() throws Exception {
        String date = "";

        mockMvc.perform(get(path)
                .param("date", date))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}