package com.example.dms.api.controllers;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
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

import com.example.dms.domain.DmsFolder;
import com.example.dms.domain.DmsUser;
import com.example.dms.services.FolderService;

@WebMvcTest(FolderController.class)
public class FolderControllerTest {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	FolderService folderService;

	DmsUser validUser;
	DmsFolder rootFolder;
	DmsFolder validFolder;
	List<DmsFolder> folderList;

	@BeforeEach
	void setUp() {
		validUser = new DmsUser("dcrncic", "12345", "Darjan", "Crnčić", "darjan.crncic@gmail.com");
		validUser.setId(UUID.randomUUID());

		rootFolder = DmsFolder.builder().path("/").build();
		validFolder = DmsFolder.builder().path("/test").parentFolder(rootFolder).build();
		rootFolder.getSubfolders().add(validFolder);

		folderList = new ArrayList<DmsFolder>();
		folderList.add(rootFolder);
		folderList.add(validFolder);
	}

	@Test
	void testGetAllFolders() throws Exception {
		BDDMockito.given(folderService.findAll()).willReturn(folderList);

		mockMvc.perform(get("/api/v1/folders/")).andExpect(status().isOk()).andExpect(jsonPath("$").isArray());
	}

	@Test
	void testFindById() throws Exception {
		BDDMockito.given(folderService.findById(Mockito.any(UUID.class))).willReturn(rootFolder);

		mockMvc.perform(get("/api/v1/folders/{id}", UUID.randomUUID())).andExpect(status().isOk())
				.andExpect(jsonPath("$.path", is(rootFolder.getPath())))
				.andExpect(jsonPath("$.subfolders[0].path", is(validFolder.getPath())));

	}

	@Test
	void testFindSubfoldersByPath() throws Exception {
		BDDMockito.given(folderService.findByPath(Mockito.anyString())).willReturn(rootFolder);

		mockMvc.perform(get("/api/v1/folders/").param("path", Mockito.anyString())).andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray());

	}

	@Test
	void testSaveNewFolder() throws Exception {
		BDDMockito.given(folderService.createNewFolder(Mockito.anyString())).willReturn(validFolder);

		mockMvc.perform(post("/api/v1/folders/").content("\"path\":\"/test\"").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated()).andExpect(jsonPath("$.path", is(validFolder.getPath())));

	}
	
	@Test
	void testUpdateFolder() throws Exception {
		BDDMockito.given(folderService.updateFolder(Mockito.any(UUID.class), Mockito.anyString())).willReturn(validFolder);

		mockMvc.perform(put("/api/v1/folders/{id}", UUID.randomUUID()).content("\"path\":\"/test\"").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.path", is(validFolder.getPath())));

	}

	@Test
	void deleteById() throws Exception {
		doNothing().when(folderService).deleteById(Mockito.any(UUID.class));

		mockMvc.perform(delete("/api/v1/folders/{id}", UUID.randomUUID())).andExpect(status().isOk());
	}
}