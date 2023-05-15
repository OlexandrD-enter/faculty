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

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> findUserByUserName(@NonNull String userName) {
        return userRepository.findByUsername(userName);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User saveUser(UserDTO userDTO, Role roleType) {
        try {
            return userRepository.save(User.builder()
                    .id(userDTO.getId())
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

    public User updateUser(User user) {
        try {
            return userRepository.save(user);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed operation to update user");
        }
    }

    public List<User> findUsersByRoleType(Role role) {
        return userRepository.findAllByRole(role);
    }

    public Page<User> findPaginated(int pageNo, int pageSize, Role role) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return userRepository.findAllByRole(pageable, role);
    }

    public User disableUserById(Long id){
        User user = userRepository.findById(id).orElseThrow(
                () -> new UsernameNotFoundException("Could not find user with id: " + id));
        user.setActive(!user.isActive());
        return userRepository.save(user);
    }

    public void deleteUserById(Long id){
        userRepository.deleteById(id);
    }

    public User findUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
}