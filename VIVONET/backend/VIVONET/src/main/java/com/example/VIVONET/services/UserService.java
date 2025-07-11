package com.example.VIVONET.services;

import com.example.VIVONET.dtos.RegistrationDto;
import com.example.VIVONET.dtos.UserInfoDto;
import com.example.VIVONET.models.User;
import com.example.VIVONET.models.UserType;
import com.example.VIVONET.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    @Autowired
    PasswordEncoder encoder;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserById(Long id) {
        Optional<User> user=userRepository.findById(id);
        return user.orElse(null);
    }

    public String registerUser(RegistrationDto registrationDto) {
        logger.info("Starting user registration..");
        if (!registrationDto.isMacthingPassword()) {
            logger.error("Passwords do not match");
            return "Passwords do not match.";
        }

        if (userRepository.existsByUsername(registrationDto.getUsername())) {
            logger.error("Username already exists");
            return "Username already exists.";
        }

        if (!isValidPassword(registrationDto.getPassword())) {
            logger.error("Password must be at least 8 characters long, with at least one uppercase letter, one number, and one special character, such as #, !, %, ?, &.");
            return "Password must be at least 8 characters long, with at least one uppercase letter, one number, and one special character, such as #, !, %, ?, &.";
        }

        User newUser = new User(
                registrationDto.getFirstName(),
                registrationDto.getLastName(),
                registrationDto.getEmail(),
                registrationDto.getUsername(),
                encoder.encode(registrationDto.getPassword()),
                registrationDto.getUserType()
        );

        logger.info("User registered successfully");
        userRepository.save(newUser);
        return "Registration successful!";
    }

    private boolean isValidPassword(String password) {
        String regex = "^(?=.*[A-Z])(?=.*\\d)(?=.*[#!%?&])[A-Za-z\\d#!%?&]{8,}$";
        return password.matches(regex);
    }

    public Optional<UserInfoDto> getUserInfoById(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        return userOptional.map(this::mapToUserInfoDto);
    }

    public List<UserInfoDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::mapToUserInfoDto)
                .collect(Collectors.toList());
    }


    private UserInfoDto mapToUserInfoDto(User user) {
        return new UserInfoDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail()
        );
    }
}
