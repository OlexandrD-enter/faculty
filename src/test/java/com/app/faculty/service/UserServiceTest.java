package com.app.faculty.service;

import com.app.faculty.dto.UserDTO;
import com.app.faculty.model.Role;
import com.app.faculty.model.User;
import com.app.faculty.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void testFindUserByUserName() {
        // Arrange
        String userName = "johndoe";
        User expectedUser = new User();
        expectedUser.setUsername(userName);

        // Mock the repository method
        when(userRepository.findByUsername(userName))
                .thenReturn(Optional.of(expectedUser));

        // Act
        Optional<User> result = userService.findUserByUserName(userName);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(expectedUser, result.get());

        // Verify that the repository method was called
        verify(userRepository, Mockito.times(1)).findByUsername(userName);
    }

    @Test
    public void testSaveUser() {
        // Arrange
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setFirstName("John");
        userDTO.setLastName("Doe");
        userDTO.setEmail("johndoe@example.com");
        userDTO.setUsername("johndoe");
        userDTO.setPassword("password");
        userDTO.setPasswordConfirm("password");

        Role roleType = Role.ROLE_USER;

        // Mock the userRepository.save method
        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0)); // Return the argument passed to save method

        // Act
        User savedUser = userService.saveUser(userDTO, roleType);

        // Assert
        assertNotNull(savedUser);
        assertEquals(userDTO.getId(), savedUser.getId());
        assertEquals(userDTO.getFirstName(), savedUser.getFirstName());
        assertEquals(userDTO.getLastName(), savedUser.getLastName());
        assertEquals(userDTO.getEmail(), savedUser.getEmail());
        assertEquals(userDTO.getUsername(), savedUser.getUsername());
        assertEquals(roleType, savedUser.getRole());
        assertTrue(savedUser.isActive());

        // Verify that the userRepository.save method was called
        verify(userRepository, Mockito.times(1)).save(any(User.class));
    }

    @Test
    public void testDeleteUserById() {
        // Arrange
        Long userId = 1L;

        // Act
        userService.deleteUserById(userId);

        // Assert
        // Verify that the userRepository.deleteById method was called with the correct userId
        verify(userRepository).deleteById(userId);
    }

    @Test
    public void testDisableUserById() {
        // Arrange
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setActive(true);

        // Mock the userRepository.findById method to return the user
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Mock the userRepository.save method to return the modified user
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        User result = userService.disableUserById(userId);

        // Assert
        assertFalse(result.isActive());

        // Verify that the userRepository.findById method was called
        verify(userRepository, Mockito.times(1)).findById(userId);

        // Verify that the userRepository.save method was called
        verify(userRepository, Mockito.times(1)).save(user);
    }

    @Test
    public void testDisableUserById_ThrowsUsernameNotFoundException() {
        // Arrange
        Long userId = 1L;

        // Mock the userRepository.findById method to return an empty optional
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(UsernameNotFoundException.class, () -> userService.disableUserById(userId));

        // Verify that the userRepository.findById method was called
        verify(userRepository, Mockito.times(1)).findById(userId);
    }

    @Test
    public void testSaveUser_ThrowsIllegalArgumentException() {
        // Arrange
        UserDTO userDTO = new UserDTO();

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> userService.saveUser(userDTO, Role.ROLE_USER));
    }
}