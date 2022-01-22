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

import com.example.dms.api.dtos.folder.NewFolderDTO;
import com.example.dms.domain.DmsFolder;
import com.example.dms.services.FolderService;
import com.example.dms.utils.exceptions.BadRequestException;

@RestController
@RequestMapping("/api/v1/folders")
public class FolderController {

	FolderService folderService;

	public FolderController(FolderService folderService) {
		super();
		this.folderService = folderService;
	};
	
	@GetMapping("/")
	public List<DmsFolder> getAllFoldersOrSubfolders(@RequestParam Optional<String> path) {
		if (path.isPresent())
			return folderService.findByPath(path.get()).getSubfolders();
		return folderService.findAll();
	}

	@GetMapping("/search")
	public DmsFolder getFolderBySearch(@RequestParam Optional<String> path) {
		if (path.isPresent())
			return folderService.findByPath(path.get());
		throw new BadRequestException("Request prameters for search are invalid.");
	}
	
	@GetMapping("/{id}")
	public DmsFolder getFolderById(@PathVariable UUID id) {
		return folderService.findById(id);
	}
	
	@PostMapping("/")
	@ResponseStatus(HttpStatus.CREATED)
	public DmsFolder createNewFolder(@RequestBody @Valid NewFolderDTO newFolderDTO) {
		return folderService.createNewFolder(newFolderDTO.getPath());
	}
	
	@PutMapping("/{id}")
	public DmsFolder updateFolder(@PathVariable UUID id, @RequestBody @Valid NewFolderDTO newFolderDTO) {
		return folderService.updateFolder(id, newFolderDTO.getPath()); 
	}
	
	@DeleteMapping("/{id}")
	public void deleteFolderById(@PathVariable UUID id) {
		folderService.deleteById(id);
	}
	
	//TODO: MOVE FILE TO FOLDER
	
}