// File: src/main/java/com/AgriTest/dto/ChecklistItemDTO.java
package com.AgriTest.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChecklistItemDTO {
    private Long id;
    private String itemDescription;
    private Boolean passed;
    private String comments;
}
