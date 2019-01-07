package project.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import project.config.ApplicationConfig;
import project.config.DataConfig;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * Test class for controller {@link RedirectController} class.
 *
 * @author Alexander Naumov
 * @version 1.0
 */

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {ApplicationConfig.class, DataConfig.class})
public class CommonControllerTest {


    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @Before
    public void setup(){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void getHeaderTest() throws Exception {
        mockMvc.perform(get("/header"))
                .andExpect(status().isOk())
                .andExpect(view().name("header"))
                .andExpect(forwardedUrl("/WEB-INF/templates/header.html"));
    }

    @Test
    public void getFooterTest() throws Exception{
        mockMvc.perform(get("/footer"))
                .andExpect(status().isOk())
                .andExpect(view().name("footer"))
                .andExpect(forwardedUrl("/WEB-INF/templates/footer.html"));
    }

}