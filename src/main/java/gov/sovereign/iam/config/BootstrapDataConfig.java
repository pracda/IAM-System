package gov.sovereign.iam.config;

import gov.sovereign.iam.agency.Agency;
import gov.sovereign.iam.agency.AgencyRepository;
import gov.sovereign.iam.role.Role;
import gov.sovereign.iam.role.RoleRepository;
import gov.sovereign.iam.user.AgencyName;
import gov.sovereign.iam.user.RoleName;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class BootstrapDataConfig {

    @Bean
    CommandLineRunner seedBaseData(AgencyRepository agencyRepository, RoleRepository roleRepository) {
        return args -> {
            Arrays.stream(AgencyName.values()).forEach(a ->
                    agencyRepository.findByName(a)
                            .orElseGet(() -> agencyRepository.save(Agency.builder().name(a).build())));

            Arrays.stream(RoleName.values()).forEach(r ->
                    roleRepository.findByName(r)
                            .orElseGet(() -> roleRepository.save(Role.builder().name(r).build())));
        };
    }
}
