package project.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import project.model.Student;
import project.repository.ModelRepository;

/**
 * Implementation of {@link UserDetailsService}.
 * Load user-specific data.
 *
 * @author Alexander Naumov.
 */
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private ModelRepository repository;

    /**
     * Load {@link Student} by email and converts it to {@link UserDetails}.
     *
     * @param email of student.
     * @return {@link SecurityUser}
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Student student = (Student) repository.getStudByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found."));
        return SecurityUser.fromStudent(student);
    }
}