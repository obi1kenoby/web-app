package project.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Test class for {@link DateService} class.
 *
 * @author Alexander Naumov.
 */
public class DateServiceTest {

    private static DateService service;

    static Stream<Arguments> data() {
        return Stream.of(
                Arguments.of(LocalDate.of(2001, 1, 1), 23),
                Arguments.of(LocalDate.of(2008, 3, 1), 21),
                Arguments.of(LocalDate.of(2014, 11, 1), 20),
                Arguments.of(LocalDate.of(1996, 9, 1), 21),
                Arguments.of(LocalDate.of(2019, 9, 1), 21)
        );
    }

    @BeforeAll
    public static void setUp() {
        service = new DateService();
    }

    @ParameterizedTest(name = "date = {0}, weekdays = {1}")
    @MethodSource("data")
    public void month(LocalDate inputDate, int datesSize) {
        assertThat(service.month(inputDate).size(), is(datesSize));
    }
}