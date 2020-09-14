package project.model;

import java.util.Set;

/**
 * Represents user permission access.
 *
 * @author Alexander Naumov.
 */
public enum Permission {

    STUDENT_READ("students:read"),
    STUDENT_WRITE("students:write"),
    DEPARTMENT_READ("departments:read"),
    DEPARTMENT_WRITE("departments:write"),
    SUBJECT_READ("subjects:read"),
    SUBJECT_WRITE("subjects:write"),
    MARK_READ("marks:read"),
    MARK_WRITE("marks:write");

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }

    public static final Set<Permission> READ = Set.of(DEPARTMENT_READ,
            STUDENT_READ, SUBJECT_READ, MARK_READ);

    public static final Set<Permission> WRITE = Set.of(DEPARTMENT_READ, DEPARTMENT_WRITE,
            STUDENT_READ, STUDENT_WRITE, SUBJECT_READ, SUBJECT_WRITE, MARK_READ, MARK_WRITE);
}
