package project.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import project.model.Model;
import project.model.Student;
import project.repository.ModelRepository;
import project.repository.ModelRepositoryImpl;

import java.util.Optional;

/**
 * Implementation of {@link UserDetailsService}.
 * Load user-specific data.
 *
 * @author Alexander Naumov.
 */
public class StudentDetailService implements UserDetailsService {

    @Autowired
    private ModelRepository repository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Model> o = repository.getStudByEmail(email);
        User.UserBuilder builder;
        if (o.isPresent()) {
            Student user = (Student) o.get();
            builder = org.springframework.security.core.userdetails.User.withUsername(email);
            builder.password(user.getPassword());
            String authorities = user.getRole().toString();
            builder.authorities(authorities);
        } else {
            throw new UsernameNotFoundException("User not found.");
        }
        return builder.build();
    }
}