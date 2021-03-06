package project.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class that represents dates generator.
 *
 * @author Alexander Naumov.
 */
public class DateService {

    /**
     * Calculates and returns all weekdays within month.
     *
     * @param current_date represents date that in needed month.
     * @return list of dates {@link List<LocalDate>}.
     */
    public List<LocalDate> month(LocalDate current_date){
        List<LocalDate> dates = new ArrayList<>();
        for (int i = 1; i <= current_date.lengthOfMonth(); i++) {
            int year = current_date.getYear();
            int month = current_date.getMonthValue();
            if (LocalDate.of(year, month, i).getDayOfWeek().getValue() < 6) {
                dates.add(LocalDate.of(year, month, i));
            }
        }
        return dates;
    }
}
