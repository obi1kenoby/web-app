package project.security;

import lombok.Data;

/**
 * Simple DTO for authentication params.
 */
@Data
public class AuthenticationRequestDTO {
    private String email;
    private String password;
}
