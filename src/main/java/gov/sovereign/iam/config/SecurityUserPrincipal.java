package gov.sovereign.iam.config;

import gov.sovereign.iam.user.AgencyName;
import gov.sovereign.iam.user.ClearanceLevel;
import gov.sovereign.iam.user.RoleName;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public record SecurityUserPrincipal(
        String email,
        RoleName role,
        AgencyName agency,
        ClearanceLevel clearance
) implements UserDetails {

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return email;
    }
}
