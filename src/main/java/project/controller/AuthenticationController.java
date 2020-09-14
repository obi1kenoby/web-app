package project.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.model.Student;
import project.repository.ModelRepository;
import project.security.AuthenticationRequestDTO;
import project.security.JwtTokenProvider;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private AuthenticationManager authenticationManager;
    private JwtTokenProvider jwtTokenProvider;
    private ModelRepository repository;

    public AuthenticationController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, ModelRepository repository) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.repository = repository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequestDTO request) {
        try {
            String email = request.getEmail();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
            Student user = (Student) repository.getStudByEmail(request.getEmail()).orElseThrow(() -> new UsernameNotFoundException("User doesn't exist."));
            String token = jwtTokenProvider.createToken(request.getEmail(), user.getRole().name());
            return ResponseEntity.ok(generateResponse(user, token));
        } catch (AuthenticationException e) {
            return new ResponseEntity<>("Invalid email/password combination.", HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextLogoutHandler handler = new SecurityContextLogoutHandler();
        handler.logout(request, response, null);
    }

    private Map<Object, Object> generateResponse(Student user, String token) {
        Map<Object, Object> response = new LinkedHashMap<>();
        response.put("id", user.getId());
        response.put("last_name", user.getLast_name());
        response.put("first_name", user.getFirst_name());
        response.put("email", user.getEmail());
        response.put("roles", user.getRole());
        response.put("permission", user.getRole().getAuthorities());
        response.put("token", token);
        return response;
    }
}
