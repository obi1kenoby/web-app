package project.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;

import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Objects;
import java.util.Properties;

/**
 * Configuration of application that
 * configure data layer.
 *
 * @author Alexander Naumov
 */
@Configuration
@PropertySource("classpath:db.properties")
@EnableTransactionManagement
public class DataConfig {

    @Autowired
    private Environment env;

    @Bean
    public DataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(env.getProperty("db.driver"));
        dataSource.setUrl(env.getProperty("db.url"));
        dataSource.setUsername(env.getProperty("db.username"));
        dataSource.setPassword(env.getProperty("db.password"));
        dataSource.setMaxTotal(Integer.parseInt(Objects.requireNonNull(env.getProperty("dbcp.maxPoolSize"))));
        dataSource.setMaxWaitMillis(Long.parseLong(Objects.requireNonNull(env.getProperty("dbcp.timeout"))));
        dataSource.setMaxIdle(Integer.parseInt(Objects.requireNonNull(env.getProperty("dbcp.maxIdleTime"))));
        dataSource.setMaxOpenPreparedStatements(Integer.parseInt(Objects.requireNonNull(env.getProperty("dbcp.maxStatements"))));
        DatabasePopulatorUtils.execute(dbInitializer(), dataSource);
        return dataSource;
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan("project.model");
        sessionFactory.setHibernateProperties(hibernateProperties());
        return sessionFactory;
    }

    private DatabasePopulator dbInitializer() {
        ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator();
        resourceDatabasePopulator.setContinueOnError(true);
        Resource createSchema = new ClassPathResource("sql/build/schema.sql");
        Resource initData = new ClassPathResource("sql/build/data.sql");
        resourceDatabasePopulator.addScripts(createSchema, initData);
        return resourceDatabasePopulator;
    }

    private Properties hibernateProperties() {
        return new Properties() {
            {
                setProperty("hibernate.dialect", env.getProperty("hibernate.dialect"));
                setProperty("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
                setProperty("hibernate.generate_statistics", env.getProperty("hibernate.generate_statistics"));
                setProperty("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
                setProperty("hibernate.format_sql", env.getProperty("hibernate.format_sql"));
                setProperty("hibernate.jdbc.time_zone", env.getProperty("hibernate.jdbc.time_zone"));
                setProperty("hibernate.enable_lazy_load_no_trans", env.getProperty("hibernate.enable_lazy_load_no_trans"));
                setProperty("hibernate.connection.characterEncoding", env.getProperty("hibernate.connection.characterEncoding"));
                setProperty("hibernate.connection.useUnicode", env.getProperty("hibernate.connection.useUnicode"));
                setProperty("hibernate.event.merge.entity_copy_observer", env.getProperty("hibernate.event.merge.entity_copy_observer"));
            }
        };
    }

    @Bean
    @Autowired
    public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
        HibernateTransactionManager txManager = new HibernateTransactionManager();
        txManager.setSessionFactory(sessionFactory);
        return txManager;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }
}
