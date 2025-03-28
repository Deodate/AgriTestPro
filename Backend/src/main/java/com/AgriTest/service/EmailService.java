// File: src/main/java/com/AgriTest/service/EmailService.java
package com.AgriTest.service;

public interface EmailService {
    /**
     * Send a simple email
     * 
     * @param to Recipient email address
     * @param subject Email subject
     * @param body Email body
     * @return True if sent successfully, false otherwise
     */
    boolean sendEmail(String to, String subject, String body);
    
    /**
     * Send an email with a report attachment
     * 
     * @param to recipient email address
     * @param subject email subject
     * @param body email body text
     * @param attachmentFilename filename for the attachment
     * @param attachmentData byte array of the attachment data
     * @param mimeType MIME type of the attachment
     */
    void sendReportEmail(String to, String subject, String body, 
                         String attachmentFilename, byte[] attachmentData, String mimeType);
}