package project.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;

import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
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
    public DataSource dataSource() throws PropertyVetoException {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setDriverClass(env.getProperty("db.driver"));
        dataSource.setJdbcUrl(env.getProperty("db.url"));
        dataSource.setUser(env.getProperty("db.username"));
        dataSource.setPassword(env.getProperty("db.password"));
        dataSource.setMinPoolSize(Integer.parseInt(env.getProperty("c3p0.minPoolSize")));
        dataSource.setMaxPoolSize(Integer.parseInt(env.getProperty("c3p0.maxPoolSize")));
        dataSource.setMaxStatements(Integer.parseInt(env.getProperty("c3p0.maxStatements")));
        dataSource.setCheckoutTimeout(Integer.parseInt(env.getProperty("c3p0.timeout")));
        dataSource.setMaxIdleTime(Integer.parseInt(env.getProperty("c3p0.maxIdleTime")));
        return dataSource;
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory() throws PropertyVetoException {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan("project.model");
        sessionFactory.setHibernateProperties(hibernateProperties());
        return sessionFactory;
    }

    private Properties hibernateProperties() {
        return new Properties() {
            {
                setProperty("hibernate.dialect", env.getProperty("hibernate.dialect"));
                setProperty("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
                setProperty("hibernate.generate_statistics", env.getProperty("hibernate.generate_statistics"));
                setProperty("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
                setProperty("hibernate.format_sql", env.getProperty("hibernate.format_sql"));
                setProperty("hibernate.enable_lazy_load_no_trans", env.getProperty("hibernate.enable_lazy_load_no_trans"));
                setProperty("hibernate.connection.characterEncoding", env.getProperty("hibernate.connection.characterEncoding"));
                setProperty("hibernate.connection.useUnicode", env.getProperty("hibernate.connection.useUnicode"));
                setProperty("hibernate.event.merge.entity_copy_observer", env.getProperty("hibernate.event.merge.entity_copy_observer"));
                setProperty("hibernate.cache.use_second_level_cache", env.getProperty("hibernate.cache.use_second_level_cache"));
                setProperty("hibernate.cache.region.factory_class", env.getProperty("hibernate.cache.region.factory_class"));
                setProperty("hibernate.cache.use_query_cache", env.getProperty("hibernate.cache.use_query_cache"));
                setProperty("hibernate.cache.provider_configuration", env.getProperty("hibernate.cache.provider_configuration"));
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
