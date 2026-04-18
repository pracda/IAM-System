package gov.sovereign.iam.config;

import gov.sovereign.iam.user.AgencyName;
import gov.sovereign.iam.user.ClearanceLevel;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "bootstrap.admin")
public record BootstrapAdminProperties(
        boolean enabled,
        String email,
        String password,
        AgencyName agency,
        ClearanceLevel clearance
) {
}
