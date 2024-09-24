package com.example.tareaTecnica1.logic.entity.rol;

import com.example.tareaTecnica1.logic.entity.user.User;
import com.example.tareaTecnica1.logic.entity.user.UserRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserSeeder implements ApplicationListener<ContextRefreshedEvent> {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;


    public UserSeeder(
            RoleRepository roleRepository,
            UserRepository  userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        this.createSuperAdministrator();
    }

    private void createSuperAdministrator() {
        User regularUser = new User();
        regularUser.setName("Regular");
        regularUser.setLastname("User");
        regularUser.setEmail("regular.user@gmail.com");
        regularUser.setPassword("regularuser123");

        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.USER);
        Optional<User> optionalUser = userRepository.findByEmail(regularUser.getEmail());

        if (optionalRole.isEmpty() || optionalUser.isPresent()) {
            return;
        }

        var user = new User();
        user.setName(regularUser.getName());
        user.setLastname(regularUser.getLastname());
        user.setEmail(regularUser.getEmail());
        user.setPassword(passwordEncoder.encode(regularUser.getPassword()));
        user.setRole(optionalRole.get());

        userRepository.save(user);
    }
}
