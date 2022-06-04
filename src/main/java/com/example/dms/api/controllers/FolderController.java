package com.example.dms.api.controllers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.dms.api.dtos.folder.DmsFolderDTO;
import com.example.dms.api.dtos.folder.FolderTreeDTO;
import com.example.dms.api.dtos.folder.NewFolderDTO;
import com.example.dms.services.FolderService;
import com.example.dms.utils.exceptions.BadRequestException;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/folders")
@RequiredArgsConstructor
public class FolderController {

	private final FolderService folderService;

	@GetMapping
	public List<DmsFolderDTO> getAllFolders() {
		return folderService.findAll();
	}
	
	@GetMapping("/tree")
	public FolderTreeDTO getFolderTreeDTO(@RequestParam String path) {
		return folderService.getSubfolderTree(path);
	}

	@GetMapping("/search")
	public DmsFolderDTO getFolderBySearch(@RequestParam Optional<String> path) {
		if (path.isPresent())
			return folderService.findByPath(path.get());
		throw new BadRequestException("Request prameters for search are invalid.");
	}
	
	@GetMapping("/{id}")
	public DmsFolderDTO getFolderById(@PathVariable UUID id) {
		return folderService.findById(id);
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public DmsFolderDTO createNewFolder(@RequestBody @Valid NewFolderDTO newFolderDTO) {
		return folderService.createFolder(newFolderDTO.getPath());
	}
	
	@PutMapping("/{id}")
	public DmsFolderDTO updateFolder(@PathVariable UUID id, @RequestBody @Valid NewFolderDTO newFolderDTO) {
		return folderService.updateFolder(id, newFolderDTO.getPath()); 
	}
	
	@DeleteMapping("/{id}")
	public void deleteFolderById(@PathVariable UUID id) {
		folderService.deleteById(id);
	}
	
	@PostMapping("/move/{id}")
	public DmsFolderDTO moveFilesToFolder(@PathVariable UUID id, @RequestBody List<UUID> documentIdList) {
		return folderService.moveFilesToFolder(id, documentIdList);
	}
}
