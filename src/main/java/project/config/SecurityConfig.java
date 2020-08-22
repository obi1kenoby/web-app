package project.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import project.model.Permission;
import project.security.StudentDetailService;


/**
 * Security configuration of application.
 *
 * @author Alexander Naumov.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/css/**", "/js/**", "/image/**").permitAll()
                .antMatchers("/contacts").access("hasAnyAuthority('ADMIN', 'USER')")
                .antMatchers(HttpMethod.GET, "/api/department/**").hasAuthority(Permission.DEPARTMENT_READ.getPermission())
                .antMatchers(HttpMethod.GET, "/api/subject/**").hasAuthority(Permission.SUBJECT_READ.getPermission())
                .antMatchers(HttpMethod.GET, "/api/student/**").hasAuthority(Permission.STUDENT_READ.getPermission())
                .antMatchers(HttpMethod.GET, "/api/mark/**").hasAuthority(Permission.MARK_READ.getPermission())
                .antMatchers(HttpMethod.POST, "/api/department/**").hasAuthority(Permission.DEPARTMENT_WRITE.getPermission())
                .antMatchers(HttpMethod.POST, "/api/subject/**").hasAuthority(Permission.SUBJECT_WRITE.getPermission())
                .antMatchers(HttpMethod.POST, "/api/student/**").hasAuthority(Permission.STUDENT_WRITE.getPermission())
                .antMatchers(HttpMethod.POST, "/api/mark/**").hasAuthority(Permission.MARK_WRITE.getPermission())
                .antMatchers(HttpMethod.DELETE, "/api/department/**").hasAuthority(Permission.SUBJECT_WRITE.getPermission())
                .antMatchers(HttpMethod.DELETE, "/api/subject/**").hasAuthority(Permission.DEPARTMENT_WRITE.getPermission())
                .antMatchers(HttpMethod.DELETE, "/api/student/**").hasAuthority(Permission.STUDENT_WRITE.getPermission())
                .antMatchers(HttpMethod.DELETE, "/api/mark/**").hasAuthority(Permission.MARK_WRITE.getPermission())
                .antMatchers("/admin").access("hasRole('ADMIN')")
                .antMatchers("/table/**").access("hasAnyRole('ADMIN', 'USER')")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login").loginProcessingUrl("/perform_login").permitAll()
                .defaultSuccessUrl("/table", true)
                .failureUrl("/login.html?error=true")
                .and()
                .logout().logoutSuccessUrl("/login")
                .and()
                .csrf()
                .disable();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new StudentDetailService();
    }
}
