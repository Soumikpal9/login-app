package com.project.loginapp.service;

import com.project.loginapp.dto.LoginDto;
import com.project.loginapp.dto.UserDto;
import com.project.loginapp.response.LoginResponse;

public interface UserService {

    String registerUser(UserDto userDto);

    LoginResponse loginUser(LoginDto loginDto);
}
