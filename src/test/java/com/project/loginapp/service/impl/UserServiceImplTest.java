package com.project.loginapp.service.impl;

import com.project.loginapp.dto.LoginDto;
import com.project.loginapp.dto.UserDto;
import com.project.loginapp.entity.User;
import com.project.loginapp.response.LoginResponse;
import com.project.loginapp.service.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest(properties = "spring.main.lazy-initialization=true", classes = {UserServiceImplTest.class})
@DisplayName("User Service -")
public class UserServiceImplTest {

    @Mock
    ModelMapper modelMapper;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserServiceImpl userService;

    @Nested
    @DisplayName("register user")
    class RegisterUser {

        @Test
        @DisplayName("register new user")
        void registerNewUser() {
            when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
                User savedUser = invocation.getArgument(0);
                savedUser.setUserId(UUID.randomUUID().toString());
                return savedUser;
            });

            UserDto userDto = new UserDto("User", "user@test.com", "password");
            String userId = userService.registerUser(userDto);
            assertThat(userId).isNotNull();

            verify(userRepository, times(1)).save(any(User.class));
        }

    }

    @Nested
    @DisplayName("login user")
    class LoginUser {

        @Test
        @DisplayName("login with correct credentials")
        void loginUserWithCorrectCredentials() {
            LoginDto loginDto = new LoginDto("user@test.com", "password");
            User user = new User();
            user.setEmail("user@test.com");
            user.setPassword("password");

            when(userRepository.findByEmail("user@test.com")).thenReturn(user);
            when(passwordEncoder.matches("password", user.getPassword())).thenReturn(true);

            LoginResponse response = userService.loginUser(loginDto);

            assertThat(response.getMessage()).isEqualTo("Login successful");
            assertThat(response.getStatus()).isTrue();

            verify(userRepository, times(1)).findByEmail(user.getEmail());
            verify(passwordEncoder, times(1)).matches(anyString(), anyString());
        }

        @Test
        @DisplayName("login with incorrect password")
        void loginUserWithIncorrectPassword() {
            LoginDto loginDto = new LoginDto("user@test.com", "wrongpassword");
            User user = new User();
            user.setEmail("user@test.com");
            user.setPassword("correctpassword");

            when(userRepository.findByEmail("user@test.com")).thenReturn(user);
            when(passwordEncoder.matches("wrongpassword", user.getPassword())).thenReturn(false);

            LoginResponse response = userService.loginUser(loginDto);

            assertThat(response.getMessage()).isEqualTo("Incorrect password");
            assertThat(response.getStatus()).isFalse();

            verify(userRepository, times(1)).findByEmail(user.getEmail());
            verify(passwordEncoder, times(1)).matches(anyString(), anyString());
        }

        @Test
        @DisplayName("login with non-existing email")
        void loginUserWithNonExistingEmail() {
            LoginDto loginDto = new LoginDto("nonexistent@test.com", "password");

            when(userRepository.findByEmail("nonexistent@test.com")).thenReturn(null);

            LoginResponse response = userService.loginUser(loginDto);

            assertThat(response.getMessage()).isEqualTo("Email does not exist");
            assertThat(response.getStatus()).isFalse();

            verify(userRepository, times(1)).findByEmail(anyString());
        }

    }

}
