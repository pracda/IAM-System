package gov.sovereign.iam.user;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public UserResponse create(@Valid @RequestBody RegisterUserRequest request) {
        return userService.register(request);
    }

    @GetMapping
    public List<UserResponse> list() {
        return userService.listAll();
    }
}
