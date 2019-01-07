package project.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import project.security.StudentDetailService;

/**
 * Security configuration of application.
 *
 * @author Alexander Naumov.
 */
//TODO: configure application security.
//@Configuration
//@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("admin").password("admin").roles("USER");
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {

//        http.formLogin()
//                .loginPage("/login")
//                .loginProcessingUrl("/perform_login")
//                .defaultSuccessUrl("/home",true)
//                .failureUrl("/login?error=true");

    }



    @Bean
    public StudentDetailService studentDetailService() {
        return new StudentDetailService();
    }
}
