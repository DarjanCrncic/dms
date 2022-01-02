package com.example.dms.api.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.example.dms.api.dtos.document.DocumentDTO;
import com.example.dms.domain.Document;

@Mapper
public interface DocumentMapper {

	DocumentMapper INSTANCE = Mappers.getMapper(DocumentMapper.class);
	
	Document documentDTOToDocument(DocumentDTO documentDTO);
	
	DocumentDTO documentToDocumentDTO(Document document);
	
	List<DocumentDTO> documentListToDocumentDTOList(List<Document> list);
}
