package gov.sovereign.iam.policy;

import gov.sovereign.iam.config.SecurityUserPrincipal;
import gov.sovereign.iam.user.AgencyName;
import gov.sovereign.iam.user.ClearanceLevel;
import gov.sovereign.iam.user.RoleName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PolicyServiceTest {

    private final PolicyService policyService = new PolicyService();

    @Test
    void adminHasOverride() {
        var admin = new SecurityUserPrincipal("admin@test.gov", RoleName.ADMIN, AgencyName.FINANCE, ClearanceLevel.LOW);
        PolicyDecision decision = policyService.evaluate(admin, AgencyName.HEALTH, "read", ClassificationLevel.HIGH);
        assertTrue(decision.allowed());
    }

    @Test
    void officerDeniedAcrossAgencies() {
        var officer = new SecurityUserPrincipal("officer@test.gov", RoleName.OFFICER, AgencyName.FINANCE, ClearanceLevel.HIGH);
        PolicyDecision decision = policyService.evaluate(officer, AgencyName.HEALTH, "read", ClassificationLevel.MEDIUM);
        assertFalse(decision.allowed());
    }

    @Test
    void officerDeniedByClearance() {
        var officer = new SecurityUserPrincipal("officer@test.gov", RoleName.OFFICER, AgencyName.FINANCE, ClearanceLevel.LOW);
        PolicyDecision decision = policyService.evaluate(officer, AgencyName.FINANCE, "read", ClassificationLevel.HIGH);
        assertFalse(decision.allowed());
    }
}
