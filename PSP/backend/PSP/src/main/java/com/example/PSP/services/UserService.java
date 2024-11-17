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

}
