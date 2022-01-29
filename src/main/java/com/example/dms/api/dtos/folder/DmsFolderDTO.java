package com.example.dms.api.dtos.folder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.example.dms.api.dtos.document.DmsDocumentDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DmsFolderDTO {

	private UUID id;
	private LocalDateTime creationDate;
	private LocalDateTime modifyDate;
	
	private String path;
	private DmsFolderDTO parentFolder;
	@Default
	private List<DmsFolderPathDTO> subfolders = new ArrayList<>();
	@Default
	private List<DmsDocumentDTO> documents  = new ArrayList<>();
}
