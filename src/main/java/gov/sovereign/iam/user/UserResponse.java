package gov.sovereign.iam.user;

public record UserResponse(
        Long id,
        String email,
        RoleName role,
        AgencyName agency,
        ClearanceLevel clearanceLevel
) {
    public static UserResponse from(UserAccount user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getRole().getName(),
                user.getAgency().getName(),
                user.getClearanceLevel()
        );
    }
}
