package project.security;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import project.model.Role;
import project.model.Student;
import project.repository.ModelRepository;

import java.time.LocalDate;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static project.model.Role.USER;

@RunWith(MockitoJUnitRunner.class)
public class StudentDetailServiceTest {

    @InjectMocks
    private StudentDetailService detailService;

    @Spy
    private ModelRepository repository;

    private UserDetails userDetails;
    private Student student;

    final String firstName = "First";
    final String lastName = "Last";
    final String email = "test@gmail.com";
    final String incorrectEmail = "test1@gmail.com";
    final String password = "$2a$10$0dTU0xPhG5k1uO279gGCVuypHzjcvbmnMwqxbnf/.7FbeCgDE3UU.";
    final LocalDate birthday = LocalDate.of(2019, 10, 8);
    final Role role = USER;

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Before
    public void setUp() {
        student = new Student();
        student.setFirst_name(firstName);
        student.setLast_name(lastName);
        student.setBirthday(birthday);
        student.setPassword(password);
        student.setEmail(email);
        student.setRole(role);

        userDetails = new User(email, password, true, true, true, true, Collections.singletonList(new SimpleGrantedAuthority("USER")));
    }

    @Test
    public void loadUserByUsername() {
        when(repository.getStudByEmail(any())).thenReturn(Optional.of(student));

        Assert.assertNotNull(detailService);
        UserDetails details = detailService.loadUserByUsername(email);
        Assert.assertEquals(details, userDetails);

        verify(repository, only()).getStudByEmail(email);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void wrongUsername() {
        when(repository.getStudByEmail(incorrectEmail)).thenReturn(Optional.empty());
        exceptionRule.expect(UsernameNotFoundException.class);
        exceptionRule.expectMessage("User not found.");

        Assert.assertNotNull(detailService);
        detailService.loadUserByUsername(incorrectEmail);

        verify(repository, only()).getStudByEmail(incorrectEmail);
        verifyNoMoreInteractions(repository);
    }
}