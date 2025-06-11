package com.myproject.service;

import com.myproject.dto.UserBasicDTO;
import com.myproject.dto.UserDetailDTO;
import com.myproject.dto.UserRegistrationDTO;
import com.myproject.mapper.UserMapper;
import com.myproject.model.User;
import com.myproject.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Value;
import jakarta.annotation.PostConstruct;



import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.password-migration.enabled:false}")
    private boolean enableMigration;

    @PostConstruct
    public void hashExistingPlaintextPasswords() {
        if (!enableMigration) return;
        List<User> users = userRepository.findAll();

        boolean updated = false;

        for (User user : users) {
            String currentPassword = user.getPassword();
            if (currentPassword != null && !currentPassword.startsWith("$2a$")) {
                // Not a bcrypt hash — assume it's plain text
                user.setPassword(passwordEncoder.encode(currentPassword));
                updated = true;
            }
        }

        if (updated) {
            userRepository.saveAll(users);
            System.out.println("✔ Existing plain-text passwords have been hashed.");
        } else {
            System.out.println("ℹ No plain-text passwords found. Skipping update.");
        }
    }

    // Create a new user with hashed password
    public UserDetailDTO registerUser(UserRegistrationDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email already in use.");
        }

        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setAddress(dto.getAddress());
        user.setCity(dto.getCity());
        user.setPostalCode(dto.getPostalCode());
        user.setCountry(dto.getCountry());

        // Hash the password
        String hashedPassword = passwordEncoder.encode(dto.getPassword());
        user.setPassword(hashedPassword);

        // Defaults, set default role as customer, emailverified as false and get time of account creation
        user.setEmailVerified(false);
        user.setRole("customer");
        user.setCreatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);
        return userMapper.toUserDetailDTO(savedUser);
    }

    // Get all users (basic info)
    public List<UserBasicDTO> getAllUsersBasic() {
        List<User> users = userRepository.findAll();
        return userMapper.toUserBasicDTOs(users);
    }

    // Get user by ID (detailed info)
    public UserDetailDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return userMapper.toUserDetailDTO(user);
    }


    // Update a user
    public UserDetailDTO updateUser(Long id, UserDetailDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setAddress(userDTO.getAddress());
        user.setCity(userDTO.getCity());
        user.setPostalCode(userDTO.getPostalCode());
        user.setCountry(userDTO.getCountry());

        User updatedUser = userRepository.save(user);
        return userMapper.toUserDetailDTO(updatedUser);
    }

    // Delete a user
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User not found");
        }
        userRepository.deleteById(id);
    }


}
