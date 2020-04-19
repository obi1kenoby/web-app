package project.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import project.service.DateService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * REST controller class, that handling requests
 * for dates range.
 *
 * @author Alexander Naumov.
 */
@Slf4j
@RestController
public class DateController {

    @Autowired
    private DateService service;

    /**
     * Returns all dates of month without weekends days.
     *
     * @param string {@link String} that represents assigned month and year in (yyyy-mm-dd) format.
     * @return {@link List} of {@link LocalDate}.
     */
    @GetMapping(value = "/api/date", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<LocalDate>> dateList(@RequestParam(name="date") String string) {
        LocalDate date;
        if (string.isEmpty()) {
            log.error("IN dateList, argument is empty or is NULL.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        try {
            date = LocalDate.parse(string, DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (DateTimeParseException e) {
            log.error("IN dateList, argument has incorrect format: {}.", string);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        log.info("IN dateList, dates were successfully loaded.");
        return new ResponseEntity<>(service.month(date), HttpStatus.OK);
    }
}
