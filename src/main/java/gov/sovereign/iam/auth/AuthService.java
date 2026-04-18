package gov.sovereign.iam.auth;

import gov.sovereign.iam.audit.AuditAction;
import gov.sovereign.iam.audit.AuditService;
import gov.sovereign.iam.user.UserAccount;
import gov.sovereign.iam.user.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuditService auditService;

    public AuthService(UserService userService,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       AuditService auditService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.auditService = auditService;
    }

    public AuthResponse login(LoginRequest request) {
        try {
            UserAccount user = userService.getByEmail(request.email());
            if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
                auditService.log(request.email(), AuditAction.LOGIN_FAILURE, "auth/login", "Invalid credentials");
                throw new IllegalArgumentException("Invalid credentials");
            }

            String token = jwtService.generateToken(user);
            auditService.log(user.getEmail(), AuditAction.LOGIN_SUCCESS, "auth/login", "JWT issued");
            return new AuthResponse(token);
        } catch (Exception e) {
            auditService.log(request.email(), AuditAction.LOGIN_FAILURE, "auth/login", "Login failed");
            throw e;
        }
    }
}
