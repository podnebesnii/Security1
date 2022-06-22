package com.firstboot.springboot.configs;

import com.firstboot.springboot.model.Role;
import com.firstboot.springboot.model.User;
import com.firstboot.springboot.repository.RoleRepository;
import com.firstboot.springboot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 *
 */

@Component
public class FirstBootConfig implements ApplicationListener<ContextRefreshedEvent> {
    private final static long ROLE_ADMIN = 1;
    private final static long ROLE_USER = 2;

    boolean alreadySetup = false;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    FirstBootConfig(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        Iterable<User> users = userRepository.findAll();
        alreadySetup = users.iterator().hasNext();
        if (alreadySetup) {
            return;
        } else {
            Role adminRole = new Role(ROLE_ADMIN, "ROLE_ADMIN");
            Role userRole = new Role(ROLE_USER, "ROLE_USER");
            Set<Role> adminRoles = new HashSet<>();
            adminRoles.add(adminRole);
            adminRoles.add(userRole);
            Set<Role> userRoles = new HashSet<>();
            userRoles.add(userRole);
            roleRepository.save(adminRole);
            roleRepository.save(userRole);

            User admin = new User();
            admin.setName("admin");
            admin.setLastname("Admin");
            admin.setEmail("admin");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setRoles(adminRoles);
            userRepository.save(admin);

            User user = new User();
            user.setName("user");
            user.setLastname("User");
            user.setEmail("user");
            user.setPassword(passwordEncoder.encode("user"));
            user.setRoles(userRoles);
            userRepository.save(user);
            alreadySetup = true;
        }
    }

}

