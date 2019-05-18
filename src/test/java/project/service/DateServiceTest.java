package project.service;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Test class for {@link DateService} class.
 *
 * @author Alexander Naumov.
 */
@RunWith(Parameterized.class)
public class DateServiceTest {

    private static DateService service;

    private LocalDate inputDate;
    private int dateSize;

    public DateServiceTest(LocalDate inputDate, int dateSize) {
        this.inputDate = inputDate;
        this.dateSize = dateSize;
    }

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {LocalDate.of(2001, 1, 1), 23},
                {LocalDate.of(2008, 3, 1), 21},
                {LocalDate.of(2014, 11, 1), 20},
                {LocalDate.of(1996, 9, 1), 21},
                {LocalDate.of(2019, 9, 1), 21}
        });
    }

    @BeforeClass
    public static void setUp() {
        service = new DateService();
    }

    @Test
    public void month() {
        assertThat(service.month(inputDate).size(), is(dateSize));
    }
}