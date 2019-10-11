package project.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.extras.springsecurity5.dialect.SpringSecurityDialect;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import project.repository.ModelRepository;
import project.repository.ModelRepositoryImpl;
import project.service.*;

/**
 * Main configuration of application that
 * configure view and own beans.
 *
 * @author Alexander Naumov.
 */
@EnableWebMvc
@Configuration
@Import(value = {DataConfig.class, SecurityConfig.class})
@ComponentScan("project")
public class ApplicationConfig implements ApplicationContextAware, WebMvcConfigurer {

    @Autowired
    private ApplicationContext applicationContext;

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/image/**", "/css/**", "/js/**")
                .addResourceLocations("/WEB-INF/static/image/",
                        "/WEB-INF/static/css/", "/WEB-INF/static/js/");
    }

    @Bean
    @Description("Thymeleaf Template Resolver")
    public SpringResourceTemplateResolver templateResolver(){
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setApplicationContext(this.applicationContext);
        templateResolver.setPrefix("/WEB-INF/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML5");
        templateResolver.setCacheable(true);
        return templateResolver;
    }

    @Bean
    @Description("Thymeleaf Template Engine")
    public SpringTemplateEngine templateEngine(){
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());
        templateEngine.setEnableSpringELCompiler(true);
        templateEngine.addDialect(new SpringSecurityDialect());
        return templateEngine;
    }

    @Bean
    public ThymeleafViewResolver viewResolver(){
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setContentType("text/html;charset=UTF-8");
        viewResolver.setCharacterEncoding("utf-8");
        viewResolver.setTemplateEngine(templateEngine());
        return viewResolver;
    }

    @Bean
    public CommonsMultipartResolver getResolver() {
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        resolver.setMaxUploadSizePerFile(26214400);
        return resolver;
    }

    @Bean
    public DateService dateService(){
        return new DateService();
    }

    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ModelRepository modelRepository() {
        return new ModelRepositoryImpl();
    }

    @Bean
    public DepartmentService departmentService() {
        return new DepartmentService();
    }

    @Bean
    public SubjectService subjectService() {
        return new SubjectService();
    }

    @Bean
    public StudentService studentService() {
        return new StudentService();
    }

    @Bean
    public MarkService markService() {
        return new MarkService();
    }
}