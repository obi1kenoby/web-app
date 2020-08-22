package project.model;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Simple JavaBean object that represents role of {@link Student}.
 *
 * @author Alexander Naumov.
 */
public enum Role {
    /**
     * Access rights for simple application users.
     */
    USER(Permission.READ),

    /**
     * Access rights for application admin.
     */
    ADMIN(Permission.WRITE);

    private final Set<Permission> permissions;

    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<SimpleGrantedAuthority> getAuthorities() {
        return permissions.stream()
                .map(p -> new SimpleGrantedAuthority(p.getPermission()))
                .collect(Collectors.toSet());
    }
}
