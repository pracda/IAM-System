package gov.sovereign.iam.resource;

import gov.sovereign.iam.audit.AuditAction;
import gov.sovereign.iam.audit.AuditService;
import gov.sovereign.iam.config.SecurityUserPrincipal;
import gov.sovereign.iam.policy.ClassificationLevel;
import gov.sovereign.iam.policy.PolicyDecision;
import gov.sovereign.iam.policy.PolicyService;
import gov.sovereign.iam.user.AgencyName;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping
public class ResourceController {

    private final PolicyService policyService;
    private final AuditService auditService;

    public ResourceController(PolicyService policyService, AuditService auditService) {
        this.policyService = policyService;
        this.auditService = auditService;
    }

    @GetMapping("/finance/records")
    public ResponseEntity<?> financeRecords(Authentication authentication) {
        return authorizeAndRespond(authentication, AgencyName.FINANCE, "read", ClassificationLevel.MEDIUM, "finance/records");
    }

    @GetMapping("/health/records")
    public ResponseEntity<?> healthRecords(Authentication authentication) {
        return authorizeAndRespond(authentication, AgencyName.HEALTH, "read", ClassificationLevel.HIGH, "health/records");
    }

    @GetMapping("/land/records")
    public ResponseEntity<?> landRecords(Authentication authentication) {
        return authorizeAndRespond(authentication, AgencyName.LAND, "read", ClassificationLevel.LOW, "land/records");
    }

    private ResponseEntity<?> authorizeAndRespond(Authentication authentication,
                                                  AgencyName agency,
                                                  String action,
                                                  ClassificationLevel classification,
                                                  String resource) {
        SecurityUserPrincipal principal = authentication != null ? (SecurityUserPrincipal) authentication.getPrincipal() : null;
        String email = principal != null ? principal.email() : "anonymous";

        PolicyDecision decision = policyService.evaluate(principal, agency, action, classification);
        if (!decision.allowed()) {
            auditService.log(email, AuditAction.ACCESS_DENIED, resource, decision.reason());
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("status", "denied", "reason", decision.reason()));
        }

        auditService.log(email, AuditAction.ACCESS_GRANTED, resource, decision.reason());
        return ResponseEntity.ok(Map.of(
                "status", "allowed",
                "agency", agency.name(),
                "resource", resource,
                "classification", classification.name()));
    }
}
