package project.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import project.model.Role;
import project.model.Student;
import project.repository.ModelRepository;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static project.model.Role.USER;

@ExtendWith(MockitoExtension.class)
public class StudentDetailServiceTest {

    @InjectMocks
    private StudentDetailService detailService;

    @Mock
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

    @BeforeEach
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

        assertNotNull(detailService);
        UserDetails details = detailService.loadUserByUsername(email);
        assertEquals(details, userDetails);

        verify(repository, only()).getStudByEmail(email);
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void wrongUsername() {
        when(repository.getStudByEmail(incorrectEmail)).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class,
                () -> detailService.loadUserByUsername(incorrectEmail));
        assertEquals("User not found.", exception.getMessage());

        verify(repository, only()).getStudByEmail(incorrectEmail);
        verifyNoMoreInteractions(repository);
    }
}