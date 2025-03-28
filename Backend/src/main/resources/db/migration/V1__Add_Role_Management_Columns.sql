-- Add permissions column
ALTER TABLE users ADD COLUMN permissions VARCHAR(255);

-- Add updated_by column
ALTER TABLE users ADD COLUMN updated_by VARCHAR(255);

-- Update existing users to have default permissions
UPDATE users SET permissions = 'READ' WHERE permissions IS NULL; 