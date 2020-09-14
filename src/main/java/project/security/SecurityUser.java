package project.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import project.model.Status;
import project.model.Student;

import java.util.Collection;
import java.util.Set;

/**
 * Wrapper over simple user as user for Spring security.
 *
 * @author Alexander Naumov.
 */
@Data
@AllArgsConstructor
public class SecurityUser implements UserDetails {

    private final String username;
    private final String password;
    private final boolean isActive;
    private final Set<SimpleGrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isActive;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isActive;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isActive;
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }

    public static UserDetails fromStudent(Student student) {
        return new User(student.getEmail(), student.getPassword(),
                student.getStatus().equals(Status.ACTIVE),
                student.getStatus().equals(Status.ACTIVE),
                student.getStatus().equals(Status.ACTIVE),
                student.getStatus().equals(Status.ACTIVE),
                student.getRole().getAuthorities());
    }
}
