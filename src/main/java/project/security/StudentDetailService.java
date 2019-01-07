package project.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import project.model.Student;


public class StudentDetailService implements UserDetailsService {
//
//    @Autowired
//    private StudentDao studentDao;
//
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//        Student student = studentDao.getByEmail(email);
//
//        UserBuilder builder = null;
//        if (student != null){
//            builder = User.withUsername(email);
//            builder.password(new BCryptPasswordEncoder().encode(student.getPassword()));
//            builder.roles(student.getRole().name());
//        } else {
//            throw new UsernameNotFoundException("User not found.");
//        }
//
        return null;
    }
}