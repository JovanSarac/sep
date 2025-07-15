package com.example.PSP.services;

import com.example.PSP.dtos.CredentialDto;
import com.example.PSP.dtos.RegistrationDto;
import com.example.PSP.exceptions.ResourceNotFoundException;
import com.example.PSP.models.User;
import com.example.PSP.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.PSP.dtos.UserInfoDto;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private static final int MAX_FAILED_ATTEMPTS = 5;
    private static final long LOCK_TIME_DURATION = 2 * 60 * 1000; //2 min zakljucano

    public User getUserById(Long id) {
        Optional<User> user=userRepository.findById(id);
        return user.orElse(null);
    }

    public String registerUser(RegistrationDto registrationDto) {
        if (!registrationDto.isMacthingPassword()) {
            return "Passwords do not match.";
        }

        if (userRepository.existsByUsername(registrationDto.getUsername())) {
            return "Username already exists.";
        }

        if (!isValidPassword(registrationDto.getPassword())) {
            return "Password must be at least 8 characters long, with at least one uppercase letter, one number, and one special character, such as #, !, %, ?, &.";
        }

        User newUser = new User(
                registrationDto.getFirstName(),
                registrationDto.getLastName(),
                registrationDto.getEmail(),
                registrationDto.getCompanyName(),
                registrationDto.getWebsiteUrl(),
                registrationDto.getCompanyAddress(),
                registrationDto.getUsername(),
                encoder.encode(registrationDto.getPassword()),
                false
        );

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
                user.getEmail(),
                user.getCompanyName(),
                user.getWebURL(),
                user.getCopmanyAddress()
        );
    }

    public void increaseFailedAttempts(String username) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            int attempts = user.getFailedAttempts() + 1;
            user.setFailedAttempts(attempts);

            if (attempts >= MAX_FAILED_ATTEMPTS) {
                user.setAccountNonLocked(false);
                user.setLockTime(System.currentTimeMillis());
            }

            userRepository.save(user);
        }
    }

    public void resetFailedAttempts(String username) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        userOpt.ifPresent(user -> {
            user.setFailedAttempts(0);
            user.setAccountNonLocked(true);
            user.setLockTime(null);
            userRepository.save(user);
        });
    }

    public boolean isAccountLocked(String username) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();

            if (!user.getAccountNonLocked()) {
                long lockTime = user.getLockTime();
                long now = System.currentTimeMillis();

                if (now - lockTime >= LOCK_TIME_DURATION) {
                    user.setAccountNonLocked(true);
                    user.setFailedAttempts(0);
                    user.setLockTime(null);
                    userRepository.save(user);
                    return false;
                }
                return true;
            }
        }
        return false;
    }

}
