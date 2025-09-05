package com.example.PSP.services;

import com.example.PSP.dtos.CredentialDto;
import com.example.PSP.dtos.RegistrationDto;
import com.example.PSP.exceptions.ResourceNotFoundException;
import com.example.PSP.models.User;
import com.example.PSP.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.PSP.dtos.UserInfoDto;

import java.time.Instant;
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

    private static final int MAX_FAILED_ATTEMPTS = 5;
    private static final long LOCK_TIME_DURATION = 2 * 60 * 1000; //2 min zakljucano

    public User getUserById(Long id) {
        logger.info("Retrieving user by userId " + id);
        Optional<User> user=userRepository.findById(id);
        return user.orElse(null);
    }

    public String registerUser(RegistrationDto registrationDto) {
        logger.info("Starting registration for " + registrationDto.getUsername() + "..");
        if (!registrationDto.isMacthingPassword()) {
            logger.error("Passwords do not match");
            return "Passwords do not match.";
        }

        if (userRepository.existsByUsername(registrationDto.getUsername())) {
            logger.error("Username " + registrationDto.getUsername() + " already exists");
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
                registrationDto.getCompanyName(),
                registrationDto.getWebsiteUrl(),
                registrationDto.getCompanyAddress(),
                registrationDto.getUsername(),
                encoder.encode(registrationDto.getPassword()),
                false
        );

        logger.info("User " + newUser.getUsername() + " registered successfully");
        userRepository.save(newUser);
        return "Registration successful!";
    }

    private boolean isValidPassword(String password) {
        String regex = "^(?=.*[A-Z])(?=.*\\d)(?=.*[#!%?&])[A-Za-z\\d#!%?&]{12,}$";
        return password.matches(regex);
    }

    public Optional<UserInfoDto> getUserInfoById(Long id) {
        logger.info("Retrieving user info by userId " + id);
        Optional<User> userOptional = userRepository.findById(id);
        return userOptional.map(this::mapToUserInfoDto);
    }

    public List<UserInfoDto> getAllUsers() {
        logger.info("Retrieving all users");

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

    public User getUserByUsername(String username){
        return userRepository.findByUsername(username)
                .orElseThrow(()->new ResourceNotFoundException("User not found"));
    }

    public void updateUserCode(String code, User user){
        long utcMillis = Instant.now().toEpochMilli();
        String hashed = encoder.encode(code);
        user.setTempCode(hashed);
        user.setCodeTimestamp(utcMillis);
        userRepository.save(user);
    }

    @Transactional
    public User validateCode(CredentialDto dto){
        var user = this.getUserByUsername(dto.getUsername());

        long utcMillis = Instant.now().toEpochMilli();
        Long timestamp = user.getCodeTimestamp();
        if(timestamp == null || utcMillis - timestamp > 20*60*1000){//20 mins
            throw new AccountExpiredException("Token has expired");
        }

        var code = dto.getPassword();
        if(encoder.matches(code,user.getTempCode())){
            user.setTempCode(null);
            user.setCodeTimestamp(null);
            userRepository.save(user);
            return user;
        }
        return null;
    }

    public void updateUserRefreshToken(User user,String token){
        var hashed = encoder.encode(token);
        user.setRefreshToken(hashed);
        userRepository.save(user);
    }

    public boolean validateRefresh(String token, User user){
        return encoder.matches(token, user.getRefreshToken());
    }

}
