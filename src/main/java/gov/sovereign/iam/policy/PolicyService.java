package gov.sovereign.iam.policy;

import gov.sovereign.iam.config.SecurityUserPrincipal;
import gov.sovereign.iam.user.AgencyName;
import gov.sovereign.iam.user.ClearanceLevel;
import gov.sovereign.iam.user.RoleName;
import org.springframework.stereotype.Service;

@Service
public class PolicyService {

    public PolicyDecision evaluate(SecurityUserPrincipal user, AgencyName resourceAgency, String action, ClassificationLevel classification) {
        if (user == null) {
            return new PolicyDecision(false, "Unauthenticated request");
        }

        if (user.role() == RoleName.ADMIN) {
            return new PolicyDecision(true, "Admin override");
        }

        // RBAC: auditors are read-only
        if (user.role() == RoleName.AUDITOR && !"read".equalsIgnoreCase(action)) {
            return new PolicyDecision(false, "Auditor role is read-only");
        }

        // ABAC: non-admin users can only access resources in their own agency
        if (user.agency() != resourceAgency) {
            return new PolicyDecision(false, "Cross-agency access denied");
        }

        // ABAC: clearance check
        if (!hasClearance(user.clearance(), classification)) {
            return new PolicyDecision(false, "Insufficient clearance level");
        }

        return new PolicyDecision(true, "Policy checks passed");
    }

    private boolean hasClearance(ClearanceLevel userClearance, ClassificationLevel resourceClassification) {
        return userClearance.ordinal() >= resourceClassification.ordinal();
    }
}
