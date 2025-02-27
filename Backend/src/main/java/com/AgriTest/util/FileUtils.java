// File: src/main/java/com/AgriTest/util/FileUtils.java
package com.AgriTest.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * Utility class for file operations.
 */
public class FileUtils {

    /**
     * Generate a unique file name to avoid name conflicts.
     *
     * @param originalFileName the original file name
     * @return the unique file name
     */
    public static String generateUniqueFileName(String originalFileName) {
        String fileExtension = getFileExtension(originalFileName);
        return UUID.randomUUID().toString() + "." + fileExtension;
    }
    
    /**
     * Get the file extension from a file name.
     *
     * @param fileName the file name
     * @return the file extension
     */
    public static String getFileExtension(String fileName) {
        if (fileName == null || fileName.lastIndexOf(".") == -1) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
    
    /**
     * Check if the given file is an image.
     *
     * @param fileName the file name
     * @return true if the file is an image, false otherwise
     */
    public static boolean isImageFile(String fileName) {
        String extension = getFileExtension(fileName).toLowerCase();
        return extension.equals("jpg") || extension.equals("jpeg") || 
               extension.equals("png") || extension.equals("gif") || 
               extension.equals("bmp");
    }
    
    /**
     * Check if the given file is a document.
     *
     * @param fileName the file name
     * @return true if the file is a document, false otherwise
     */
    public static boolean isDocumentFile(String fileName) {
        String extension = getFileExtension(fileName).toLowerCase();
        return extension.equals("pdf") || extension.equals("doc") || 
               extension.equals("docx") || extension.equals("xls") || 
               extension.equals("xlsx") || extension.equals("txt");
    }
    
    /**
     * Save a MultipartFile to the specified directory.
     *
     * @param file the file to save
     * @param directory the directory to save the file
     * @return the saved file path
     * @throws IOException if an I/O error occurs
     */
    public static String saveFile(MultipartFile file, String directory) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("Failed to store empty file");
        }
        
        Path directoryPath = Paths.get(directory);
        Files.createDirectories(directoryPath);
        
        String originalFileName = file.getOriginalFilename();
        String uniqueFileName = generateUniqueFileName(originalFileName);
        Path filePath = directoryPath.resolve(uniqueFileName);
        
        file.transferTo(filePath.toFile());
        
        return uniqueFileName;
    }
    
    /**
     * Delete a file from the specified directory.
     *
     * @param fileName the file name to delete
     * @param directory the directory containing the file
     * @return true if the file was deleted successfully, false otherwise
     */
    public static boolean deleteFile(String fileName, String directory) {
        File file = new File(directory + File.separator + fileName);
        return file.exists() && file.delete();
    }
    
    /**
     * Get the size of a file in readable format (e.g., KB, MB).
     *
     * @param sizeInBytes the size in bytes
     * @return the readable file size
     */
    public static String getReadableFileSize(long sizeInBytes) {
        if (sizeInBytes <= 0) {
            return "0 B";
        }
        
        final String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };
        int digitGroups = (int) (Math.log10(sizeInBytes) / Math.log10(1024));
        
        return String.format("%.1f %s", sizeInBytes / Math.pow(1024, digitGroups), units[digitGroups]);
    }
}