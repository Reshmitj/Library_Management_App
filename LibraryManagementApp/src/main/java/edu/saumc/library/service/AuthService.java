package edu.saumc.library.service;

import edu.saumc.library.entity.Admin;
import edu.saumc.library.entity.User;
import edu.saumc.library.repository.AdminRepository;
import edu.saumc.library.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final AdminRepository adminRepository;

    public AuthService(UserRepository userRepository, AdminRepository adminRepository) {
        this.userRepository = userRepository;
        this.adminRepository = adminRepository;
    }

    public boolean authenticateUser(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        return userOpt.isPresent() && userOpt.get().getPassword().equals(password);
    }

    public boolean authenticateAdmin(String email, String password) {
        Optional<Admin> adminOpt = adminRepository.findByEmail(email);
        return adminOpt.isPresent() && adminOpt.get().getPassword().equals(password);
    }
}
