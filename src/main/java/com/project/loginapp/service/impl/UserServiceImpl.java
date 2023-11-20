package com.project.loginapp.service.impl;

import com.project.loginapp.dto.LoginDto;
import com.project.loginapp.dto.UserDto;
import com.project.loginapp.entity.User;
import com.project.loginapp.response.LoginResponse;
import com.project.loginapp.service.UserService;
import com.project.loginapp.service.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public String registerUser(UserDto userDto) {
        User user = new User();
        modelMapper.map(userDto, user);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        userRepository.save(user);
        return user.getUserId();
    }

    @Override
    public LoginResponse loginUser(LoginDto loginDto) {
        User user = userRepository.findByEmail(loginDto.getEmail());

        LoginResponse loginResponse = new LoginResponse();
        if (user != null) {
            Boolean isPasswordCorrect = passwordEncoder.matches(loginDto.getPassword(), user.getPassword());
            if (isPasswordCorrect) {
                loginResponse.setMessage("Login successful");
                loginResponse.setStatus(true);
            } else {
                loginResponse.setMessage("Incorrect password");
                loginResponse.setStatus(false);
            }
        } else {
            loginResponse.setMessage("Email does not exist");
            loginResponse.setStatus(false);
        }
        return loginResponse;
    }

}
