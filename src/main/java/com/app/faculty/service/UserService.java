package com.app.faculty.service;

import com.app.faculty.dto.UserDTO;
import com.app.faculty.model.Role;
import com.app.faculty.model.User;
import com.app.faculty.repository.UserRepository;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findUserByUserName(@NonNull String username) {
        return userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("Could not find user: " + username));
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User saveUser(UserDTO userDTO, Role roleType) {
        try {
            return userRepository.save(User.builder()
                    .firstName(userDTO.getFirstName())
                    .lastName(userDTO.getLastName())
                    .email(userDTO.getEmail())
                    .username(userDTO.getUsername())
                    .role(roleType)
                    .password(new BCryptPasswordEncoder().encode(userDTO.getPassword()))
                    .isActive(true).build());
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed operation to save new user");
        }
    }

    public List<User> findUsersByRoleType(Role role) {
        return userRepository.findAllByRole(role);
    }

    public Page<User> findPaginated(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return userRepository.findAllByRole(pageable, Role.ROLE_USER);
    }

    @Transactional
    public User disableUserById(Long id){
        User user = userRepository.findById(id).orElseThrow(
                () -> new UsernameNotFoundException("Could not find user with id: " + id));
        user.setActive(!user.isActive());
        return userRepository.save(user);
    }

}