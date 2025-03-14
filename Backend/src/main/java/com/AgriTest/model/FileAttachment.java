// File: src/main/java/com/AgriTest/model/FileAttachment.java
package com.AgriTest.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "file_attachments")
public class FileAttachment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "file_type", nullable = false)
    private String fileType;

    @Lob
    @Column(name = "file_data", nullable = false)
    private byte[] fileData;
}
