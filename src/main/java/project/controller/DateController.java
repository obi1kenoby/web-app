package project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.service.DateService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * REST controller class, that handling requests
 * for dates range.
 *
 * @author Alexander Naumov.
 */
@RestController
@RequestMapping("/api/date")
public class DateController {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

    @Autowired
    private DateService service;

    /**
     * Returns all dates of month without weekends days.
     *
     * @param date {@link String} that represents assigned month and year.
     * @return {@link List} of {@link LocalDate}.
     */
    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<LocalDate>> dates(@RequestParam(name="date") String date) {
        if (date == null || date.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(service.month(LocalDate.parse(date, formatter)), HttpStatus.OK);
    }
}
