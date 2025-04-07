package edu.saumc.library.service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.saumc.library.entity.User;
import edu.saumc.library.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private static final String PASSWORD_REGEX = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public User registerUser(String name, String email, String password) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already exists!");
        }

        if (!Pattern.matches(PASSWORD_REGEX, password)) {
            throw new IllegalArgumentException("Password does not meet security requirements!");
        }

        User user = new User(name, email, password);
        return userRepository.save(user); // ✅ FIX: Use repository save method
    }
    
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("User ID not found!");
        }
        userRepository.deleteById(id);
    }
    
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + id));
    }

    @Transactional
    public User updateUser(Long id, String name, String email, String password) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + id));

        if (!user.getEmail().equals(email) && userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already exists!");
        }

        if (!Pattern.matches(PASSWORD_REGEX, password)) {
            throw new IllegalArgumentException("Password does not meet security requirements!");
        }

        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        return userRepository.save(user); 
    }
    
    @Transactional
    public User saveUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already exists!");
        }

        if (!Pattern.matches(PASSWORD_REGEX, user.getPassword())) {
            throw new IllegalArgumentException("Password does not meet security requirements!");
        }

        return userRepository.save(user); 
    }

}
