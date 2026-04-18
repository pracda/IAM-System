package gov.sovereign.iam.agency;

import gov.sovereign.iam.user.AgencyName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AgencyRepository extends JpaRepository<Agency, Long> {
    Optional<Agency> findByName(AgencyName name);
}
