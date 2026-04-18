package gov.sovereign.iam.config;

import gov.sovereign.iam.agency.Agency;
import gov.sovereign.iam.agency.AgencyRepository;
import gov.sovereign.iam.role.Role;
import gov.sovereign.iam.role.RoleRepository;
import gov.sovereign.iam.user.AgencyName;
import gov.sovereign.iam.user.RoleName;
import gov.sovereign.iam.user.UserAccount;
import gov.sovereign.iam.user.UserAccountRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;

@Configuration
public class BootstrapDataConfig {

    @Bean
    CommandLineRunner seedBaseData(AgencyRepository agencyRepository,
                                   RoleRepository roleRepository,
                                   UserAccountRepository userAccountRepository,
                                   PasswordEncoder passwordEncoder,
                                   BootstrapAdminProperties bootstrapAdminProperties) {
        return args -> {
            Arrays.stream(AgencyName.values()).forEach(a ->
                    agencyRepository.findByName(a)
                            .orElseGet(() -> agencyRepository.save(Agency.builder().name(a).build())));

            Arrays.stream(RoleName.values()).forEach(r ->
                    roleRepository.findByName(r)
                            .orElseGet(() -> roleRepository.save(Role.builder().name(r).build())));

            if (!bootstrapAdminProperties.enabled()) {
                return;
            }

            String email = bootstrapAdminProperties.email() == null ? "" : bootstrapAdminProperties.email().trim().toLowerCase();
            String password = bootstrapAdminProperties.password() == null ? "" : bootstrapAdminProperties.password().trim();
            if (email.isBlank() || password.isBlank()) {
                throw new IllegalStateException("bootstrap.admin is enabled but email/password are missing");
            }

            userAccountRepository.findByEmail(email).orElseGet(() -> {
                var adminRole = roleRepository.findByName(RoleName.ADMIN)
                        .orElseThrow(() -> new IllegalStateException("ADMIN role not found"));
                var adminAgency = agencyRepository.findByName(bootstrapAdminProperties.agency())
                        .orElseThrow(() -> new IllegalStateException("Bootstrap admin agency not found"));

                return userAccountRepository.save(UserAccount.builder()
                        .email(email)
                        .passwordHash(passwordEncoder.encode(password))
                        .role(adminRole)
                        .agency(adminAgency)
                        .clearanceLevel(bootstrapAdminProperties.clearance())
                        .build());
            });
        };
    }
}
