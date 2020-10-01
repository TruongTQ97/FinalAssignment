package com.truongtq6.finalassignment.seeders;

import com.truongtq6.finalassignment.constant.CommonConstants;
import com.truongtq6.finalassignment.entity.RoleEntity;
import com.truongtq6.finalassignment.entity.UserEntity;
import com.truongtq6.finalassignment.repository.RoleRepository;
import com.truongtq6.finalassignment.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashSet;


@AllArgsConstructor
@Component
public class DataSeedingListener implements ApplicationListener<ContextRefreshedEvent> {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    private RoleEntity saveOrUpdateRole(String rolename){
        RoleEntity savedRole = roleRepository.findRoleEntityByName(rolename);
        if (savedRole == null) {
            RoleEntity role = new RoleEntity();
            role.setName(rolename);
            role.setCreatedAt(LocalDateTime.now());
            savedRole = roleRepository.save(role);
        }

        return savedRole;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent arg0) {
        // Roles
        RoleEntity savedAdmin = saveOrUpdateRole(CommonConstants.ROLE_ADMIN);
        RoleEntity savedMember = saveOrUpdateRole(CommonConstants.ROLE_MEMBER);

        String adminUsername = "admin@gmail.com";
        String password = "admin123";

        // Admin account
        if (userRepository.findUserEntityByUsername(adminUsername).orElse(null) == null) {
            UserEntity admin = new UserEntity();
            admin.setUsername(adminUsername);
            admin.setPassword(passwordEncoder.encode(password));
            HashSet<RoleEntity> roles = new HashSet<>();
            roles.add(savedAdmin);
            roles.add(savedMember);
            admin.setRoles(roles);
            userRepository.save(admin);
        }

        String memberUsername = "member@gmail.com";
        // Member account
        if (userRepository.findUserEntityByUsername(memberUsername).orElse(null) == null) {
            UserEntity user = new UserEntity();
            user.setUsername(memberUsername);
            user.setPassword(passwordEncoder.encode(password));
            HashSet<RoleEntity> roles = new HashSet<>();
            roles.add(savedMember);
            user.setRoles(roles);
            userRepository.save(user);
        }
    }
}