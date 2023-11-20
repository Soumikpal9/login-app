package com.project.loginapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.loginapp.dto.LoginDto;
import com.project.loginapp.dto.UserDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class LoginAppApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@Order(1)
	void registerUserTest() throws Exception {
		UserDto userDto = new UserDto("User", "user@test.com", "password");

		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/api/v1/user/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(userDto)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();

		String responseBody = mvcResult.getResponse().getContentAsString();
		Assertions.assertThat(responseBody).isNotEmpty();
	}

	@Test
	@Order(2)
	void loginUserValidTest() throws Exception {
		LoginDto loginDto = new LoginDto("user@test.com", "password");

		mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/api/v1/user/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(loginDto)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Login successful"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(true));
	}

	@Test
	@Order(3)
	void loginUserInvalidEmailTest() throws Exception {
		LoginDto loginDto = new LoginDto("email@test.com", "password");

		mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/api/v1/user/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(loginDto)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Email does not exist"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(false));
	}

	@Test
	@Order(4)
	void loginUserIncorrectPasswordTest() throws Exception {
		LoginDto loginDto = new LoginDto("user@test.com", "incorrectpassword");

		mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/api/v1/user/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(loginDto)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Incorrect password"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(false));
	}

}
