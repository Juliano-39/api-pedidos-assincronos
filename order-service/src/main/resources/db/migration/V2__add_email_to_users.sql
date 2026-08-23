ALTER TABLE users
    ADD COLUMN email VARCHAR(255);

UPDATE users
SET users.email = CONCAT(username, '@test.com')
WHERE users.email IS NULL;

ALTER TABLE users
    MODIFY COLUMN email VARCHAR (255) NOT NULL;

ALTER TABLE users
    ADD CONSTRAINT uk_users_email UNIQUE (email);