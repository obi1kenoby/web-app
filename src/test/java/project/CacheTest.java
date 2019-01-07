package project;

import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import project.config.ApplicationConfig;
import project.config.DataConfig;
import project.model.Department;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;


/**
 * @author Alexander Naumov
 */

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {ApplicationConfig.class, DataConfig.class})
public class CacheTest {

//    @Autowired
//    private DepartmentDao dao;
//
//    @Autowired
//    private EntityManagerFactory managerFactory;
//
//    @Test
//    public void test(){
//        Statistics statistics = managerFactory.unwrap(SessionFactory.class).getStatistics();
//        System.out.println("Stats enabled = " + statistics.isStatisticsEnabled());
//        statistics.setStatisticsEnabled(true);
//        System.out.println("Stats enabled = " + statistics.isStatisticsEnabled() + "\n");
//
//        printStats(statistics, 0);
//
//        EntityManager em1 = managerFactory.createEntityManager();
//        EntityManager em2 = managerFactory.createEntityManager();
//        EntityTransaction trans1 = em1.getTransaction();
//        EntityTransaction trans2 = em2.getTransaction();
//        trans1.begin();
//        trans2.begin();
//
//        Department department = em1.find(Department.class, 1L);
//        printStats(statistics, 1);
//        department = em1.find(Department.class, 1L);
//        printStats(statistics, 2);
//        em1.clear();
//        department = em1.find(Department.class, 1L);
//        printStats(statistics, 3);
//        department = em1.find(Department.class, 3L);
//        printStats(statistics, 4);
//        department = em2.find(Department.class, 1L);
//        printStats(statistics, 4);
//
//        trans1.commit();
//        trans2.commit();
//        managerFactory.close();
//    }
//
//
//    private static void printStats(Statistics stats, int i) {
//        System.out.println("***** " + i + " *****");
//        System.out.println("Fetch Count = " + stats.getEntityFetchCount());
//        System.out.println("Second Level Hit Count = " + stats.getSecondLevelCacheHitCount());
//        System.out.println("Second Level Miss Coun t = " + stats.getSecondLevelCacheMissCount());
//        System.out.println("Second Level Put Count = " + stats.getSecondLevelCachePutCount() + "\n");
//    }
}
