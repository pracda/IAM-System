package gov.sovereign.iam.user;

import gov.sovereign.iam.agency.Agency;
import gov.sovereign.iam.agency.AgencyRepository;
import gov.sovereign.iam.role.Role;
import gov.sovereign.iam.role.RoleRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserAccountRepository userAccountRepository;
    private final AgencyRepository agencyRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserAccountRepository userAccountRepository,
                       AgencyRepository agencyRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder) {
        this.userAccountRepository = userAccountRepository;
        this.agencyRepository = agencyRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse register(RegisterUserRequest request) {
        if (userAccountRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email already exists");
        }

        Agency agency = agencyRepository.findByName(request.agency())
                .orElseThrow(() -> new IllegalArgumentException("Unknown agency"));
        Role role = roleRepository.findByName(request.role())
                .orElseThrow(() -> new IllegalArgumentException("Unknown role"));

        UserAccount user = userAccountRepository.save(UserAccount.builder()
                .email(request.email())
                .passwordHash(passwordEncoder.encode(request.password()))
                .role(role)
                .agency(agency)
                .clearanceLevel(request.clearanceLevel())
                .build());

        return UserResponse.from(user);
    }

    public UserAccount getByEmail(String email) {
        return userAccountRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public List<UserResponse> listAll() {
        return userAccountRepository.findAll().stream().map(UserResponse::from).toList();
    }
}
