package project.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import project.model.Mark;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class MarkController {
//
//    @Autowired
//    private MarkDao markDao;
//    @Autowired
//    private SubjectDao subjectDao;
//    @Autowired
//    private StudentDao studentDao;
//    private static Logger logger = Logger.getLogger(MarkController.class);
//
//    // return mark by its id.
//
//    @RequestMapping(value = "marks/get/{id}", method = RequestMethod.GET)
//    public @ResponseBody Mark getMarkById(@PathVariable("id") long id){
//        return markDao.findOne(id);
//    }
//
//    // update mark by id if its id more than 0 else save new mark and return it.
//
//    @RequestMapping(value = "/marks/{id}/{value}/{subject}/{student}/{date}/save", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    public @ResponseBody Mark saveMark(@PathVariable("id") String markId,  @PathVariable("value") String value, @PathVariable("subject")String subjId,
//                         @PathVariable("student") String studName, @PathVariable("date")String date){
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
//        String firstName = studName.split("-")[1];
//        String lastName = studName.split("-")[0];
//        long id = Long.parseLong(markId);
//        Mark mark = new Mark();
//        if (id > 0){
//            markDao.updateById(Integer.parseInt(markId), Integer.parseInt(value));
//            logger.info("mark with ID " + markId + " was changed the value to " + value + ".");
//        } else {
//            mark.setSubject(subjectDao.findOne(Long.parseLong(subjId)));
//            mark.setStudent(studentDao.getByNameAndLastName(firstName, lastName));
//            mark.setDate(LocalDate.parse(date, formatter).plusDays(1));
//            mark.setValue(Integer.parseInt(value));
//            markDao.save(mark);
//            logger.info("new mark " + mark + " was successfully saved.");
//        }
//        return mark;
//    }
//
//    // remove mark by its id and return 1 if delete was successful,
//    // else 0 when mark  already was removed earlier.
//
//    @RequestMapping(value = "/marks/{id}/delete", method = RequestMethod.GET)
//    public @ResponseBody int deleteMark(@PathVariable("id") long id){
//        int value = markDao.deleteById(id);
//        if (value > 0){
//            logger.info("the mark with ID is " + id + " was successfully removed.");
//        } else {
//            logger.info("the mark with ID is " + id + "not exist in database or already removed earlier.");
//        }
//        return value;
//    }
//
//    // select marks which subject is equal 'subject', students from studies in department with id equal 'id'
//    // and marks date between 'since' and 'to' dates.
//
//    @RequestMapping(value = "/{dep}/{subject}/{date}/marks", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    public @ResponseBody List<Mark> getForTable(@PathVariable("dep") long dep, @PathVariable("subject") String subject,
//                                                @PathVariable("date") String date){
//        String[] strings = date.split("-");
//        int year = Integer.parseInt(strings[0]);
//        int month = Integer.parseInt(strings[1]);
//        LocalDate since = LocalDate.of(year, month, 1);
//        if (month == 12) {
//            year++;
//            month = 1;
//        }
//        LocalDate to = LocalDate.of(year, month, 1).plusMonths(1).minusDays(1);
//        return markDao.getForMonth(dep, subject, since, to);
//    }
}
