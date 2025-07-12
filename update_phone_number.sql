-- SQL script to update phone number from 0788765320 to 0788530240 in the users table
UPDATE users 
SET phone_number = '0788530240' 
WHERE phone_number = '0788765320';
 
-- Verify the update
SELECT id, username, email, phone_number 
FROM users 
WHERE phone_number = '0788530240'; 