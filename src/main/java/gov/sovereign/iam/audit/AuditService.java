package gov.sovereign.iam.audit;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class AuditService {

    private final AuditLogRepository auditLogRepository;

    public AuditService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    public void log(String userEmail, AuditAction action, String resource, String details) {
        auditLogRepository.save(AuditLog.builder()
                .userEmail(userEmail)
                .action(action)
                .resource(resource)
                .details(details)
                .timestamp(Instant.now())
                .build());
    }

    public List<AuditLog> findAll() {
        return auditLogRepository.findAll();
    }
}
