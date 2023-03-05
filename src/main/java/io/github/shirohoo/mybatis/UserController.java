package io.github.shirohoo.mybatis;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserMybatisMapper userRepository;

    @GetMapping
    public User fetchUser(User user) {
        return userRepository.findByUsernameAndPassword(user.getUsername(), user.getPassword());
    }

    @PostMapping
    public void createUser(@RequestBody User user) {
        userRepository.save(user.getUsername(), user.getPassword());
    }
}
