package gov.sovereign.iam.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegisterUserRequest(
        @Email String email,
        @NotBlank String password,
        @NotNull RoleName role,
        @NotNull AgencyName agency,
        @NotNull ClearanceLevel clearanceLevel
) {
}
