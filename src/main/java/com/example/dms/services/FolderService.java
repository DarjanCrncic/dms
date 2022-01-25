package com.example.dms.services;

import java.util.UUID;

import com.example.dms.api.dtos.folder.DmsFolderDTO;
import com.example.dms.domain.DmsFolder;

public interface FolderService extends CrudService<DmsFolder, DmsFolderDTO, UUID>{
	
	DmsFolderDTO findByPath(String path);

	DmsFolderDTO createNewFolder(String path);

	DmsFolderDTO updateFolder(UUID id, String path);
	
}
