package com.AgriTest.mapper;

import com.AgriTest.dto.FieldActivityRequest;
import com.AgriTest.dto.FieldActivityResponse;
import com.AgriTest.model.FieldActivity;
import com.AgriTest.model.FieldActivityAttachment;
import com.AgriTest.model.User;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class FieldActivityMapper {

    public FieldActivity toEntity(FieldActivityRequest request, User responsiblePerson) {
        FieldActivity fieldActivity = new FieldActivity();
        fieldActivity.setActivityName(request.getActivityName());
        fieldActivity.setLocation(request.getLocation());
        fieldActivity.setResponsiblePerson(responsiblePerson);
        fieldActivity.setActivityDateTime(request.getActivityDateTime());
        fieldActivity.setObservations(request.getObservations());
        fieldActivity.setStatus(request.getStatus() != null 
            ? request.getStatus() 
            : FieldActivity.FieldActivityStatus.PLANNED);
        return fieldActivity;
    }

    public FieldActivityResponse toResponse(FieldActivity fieldActivity) {
        FieldActivityResponse response = new FieldActivityResponse();
        response.setId(fieldActivity.getId());
        response.setActivityName(fieldActivity.getActivityName());
        response.setLocation(fieldActivity.getLocation());
        
        // Map Responsible Person
        if (fieldActivity.getResponsiblePerson() != null) {
            FieldActivityResponse.UserDto userDto = new FieldActivityResponse.UserDto();
            userDto.setId(fieldActivity.getResponsiblePerson().getId());
            userDto.setUsername(fieldActivity.getResponsiblePerson().getUsername());
            userDto.setFullName(fieldActivity.getResponsiblePerson().getFullName());
            response.setResponsiblePerson(userDto);
        }
        
        response.setActivityDateTime(fieldActivity.getActivityDateTime());
        response.setObservations(fieldActivity.getObservations());
        response.setStatus(fieldActivity.getStatus());
        response.setCreatedAt(fieldActivity.getCreatedAt());
        response.setUpdatedAt(fieldActivity.getUpdatedAt());
        
        // Map Attachments
        if (fieldActivity.getAttachments() != null) {
            response.setAttachments(fieldActivity.getAttachments().stream()
                .map(this::toAttachmentDto)
                .collect(Collectors.toList()));
        }
        
        return response;
    }

    private FieldActivityResponse.AttachmentDto toAttachmentDto(FieldActivityAttachment attachment) {
        FieldActivityResponse.AttachmentDto dto = new FieldActivityResponse.AttachmentDto();
        dto.setId(attachment.getId());
        dto.setFileName(attachment.getFileName());
        dto.setFilePath(attachment.getFilePath());
        dto.setFileType(attachment.getFileType());
        dto.setUploadedAt(attachment.getUploadedAt());
        return dto;
    }
}