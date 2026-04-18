package gov.sovereign.iam.policy;

public record PolicyDecision(boolean allowed, String reason) {
}
