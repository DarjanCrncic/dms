package com.example.dms.api.controllers;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.dms.api.dtos.user.UserDTO;
import com.example.dms.api.mappers.UserMapper;
import com.example.dms.domain.User;
import com.example.dms.services.UserService;
import com.example.dms.utils.exceptions.UniqueConstraintViolatedException;
import com.example.dms.utils.exceptions.UserNotFoundException;

@WebMvcTest(UserController.class)
class UserControllerTest {

	@MockBean
	UserService userService;

	@MockBean
	UserMapper userMapper;

	@Autowired
	MockMvc mockMvc;

	User validUser;
	UserDTO validUserDTO;
	String validUserJSON;

	@BeforeEach
	void setUp() {
		validUser = new User("dcrncic", "12345", "Darjan", "Crnčić", "darjan.crncic@gmail.com");
		validUser.setId(UUID.randomUUID());

		validUserDTO = new UserDTO(validUser.getId(), "dcrncic", "Darjan", "Crnčić", "darjan.crncic@gmail.com");

		validUserJSON = "{\n    \"password\": \"12345\",\n    \"username\": \"dcrncic\",\n    \"first_name\": \"Darjan\",\n    \"last_name\": \"Crn\u010di\u0107\",\n    \"email\": \"darjan.crncic@gmail.com\"\n}";
	}

	@Test
	void testGetUserById() throws Exception {

		BDDMockito.given(userService.findById(Mockito.any())).willReturn(validUser);
		BDDMockito.given(userMapper.userToUserDTO(Mockito.any(User.class))).willReturn(validUserDTO);

		mockMvc.perform(get("/api/v1/users/{id}", validUser.getId())).andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(validUser.getId().toString())))
				.andExpect(jsonPath("$.username", is(validUser.getUsername())))
				.andExpect(jsonPath("$.email", is(validUser.getEmail()))).andReturn();
	}

	@Test
	void testGetUserByIdNotFound() throws Exception {
		BDDMockito.given(userService.findById(Mockito.any())).willThrow(UserNotFoundException.class);

		mockMvc.perform(get("/api/v1/users/{id}", UUID.randomUUID())).andExpect(status().isNotFound()).andReturn();
	}

	@Test
	void testGetUserByUsername() throws Exception {
		BDDMockito.given(userService.findByUsername(Mockito.any(String.class))).willReturn(validUser);
		BDDMockito.given(userMapper.userToUserDTO(Mockito.any(User.class))).willReturn(validUserDTO);

		mockMvc.perform(get("/api/v1/users").param("username", validUser.getUsername())).andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(validUser.getId().toString())))
				.andExpect(jsonPath("$.username", is(validUser.getUsername())))
				.andExpect(jsonPath("$.email", is(validUser.getEmail()))).andReturn();
	}

	@Test
	void testGetUserWithInvalidParam() throws Exception {
		mockMvc.perform(get("/api/v1/users").param("username2", validUser.getUsername()))
				.andExpect(status().isBadRequest()).andReturn();
	}

	@Test
	void testCreateNewUser() throws Exception {
		BDDMockito.given(userService.save(Mockito.any(User.class))).willReturn(validUser);
		BDDMockito.given(userMapper.userToUserDTO(Mockito.any(User.class))).willReturn(validUserDTO);

		mockMvc.perform(post("/api/v1/users").content(validUserJSON).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated()).andReturn();
	}
	
	@Test
	void testCreateNewUserUnique() throws Exception {
		BDDMockito.given(userService.save(Mockito.any(User.class))).willThrow(UniqueConstraintViolatedException.class);

		mockMvc.perform(post("/api/v1/users").content(validUserJSON).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()).andReturn();
	}

}
