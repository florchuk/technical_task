-- Створення таблиці зображень.
CREATE TABLE IF NOT EXISTS  fish_images (
	id INT AUTO_INCREMENT PRIMARY KEY,
	fish_id INT NOT NULL,
	image_file_name VARCHAR(255),
	FOREIGN KEY (fish_id) REFERENCES fish(id) ON DELETE CASCADE
);

-- Міграція усіх існуючих даних.
INSERT INTO fish_images (fish_id, image_file_name)
SELECT id, image_file_name FROM fish;

-- Видалення поля, яке більше не використовується.
ALTER TABLE fish DROP COLUMN image_file_name;

-- Створення таблиці користувачів.
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    is_account_non_expired BOOLEAN NOT NULL,
    is_account_non_locked BOOLEAN NOT NULL,
    is_credentials_non_expired BOOLEAN NOT NULL,
    is_enabled BOOLEAN NOT NULL,
    created_at TIMESTAMP(6) NOT NULL,
    updated_at TIMESTAMP(6) NOT NULL
);

-- Створення таблиці ролей.
CREATE TABLE IF NOT EXISTS authorities (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    created_at TIMESTAMP(6) NOT NULL,
    updated_at TIMESTAMP(6) NOT NULL
);

-- Створення таблиці, яка використовується для зв'язки користувачів і їх ролей.
CREATE TABLE IF NOT EXISTS user_authorities (
    user_id INT NOT NULL,
    authority_id INT NOT NULL,
    created_at TIMESTAMP(6) NOT NULL,
    updated_at TIMESTAMP(6) NOT NULL,
    PRIMARY KEY (user_id, authority_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (authority_id) REFERENCES authorities(id) ON DELETE CASCADE
);

-- Створення базових ролей.
INSERT INTO authorities (name, created_at, updated_at)
VALUES ('ADMIN', CURRENT_TIMESTAMP(6), CURRENT_TIMESTAMP(6));
INSERT INTO authorities (name, created_at, updated_at)
VALUES ('USER', CURRENT_TIMESTAMP(6), CURRENT_TIMESTAMP(6));

-- Створення базових користувачів.
INSERT INTO users (
    username,
    password,
    is_account_non_expired,
    is_account_non_locked,
    is_credentials_non_expired,
    is_enabled,
    created_at,
    updated_at
)
VALUES (
    'admin',
    '$2a$10$OQh4Eg0Ku66/18S6dB.Yce3sp2kgG9S2JF9Y7pXICNAt0mn7GLXDq', -- admin
    TRUE,
    TRUE,
    TRUE,
    TRUE,
    CURRENT_TIMESTAMP(6),
    CURRENT_TIMESTAMP(6)
);
INSERT INTO users (
    username,
    password,
    is_account_non_expired,
    is_account_non_locked,
    is_credentials_non_expired,
    is_enabled,
    created_at,
    updated_at
)
VALUES (
    'user',
    '$2a$10$fLHWzPoI6YD.29fh9s6jue8w/WU.TshYOYR8TUCvcBkbYnVFgNZnO', -- user
    TRUE,
    TRUE,
    TRUE,
    TRUE,
    CURRENT_TIMESTAMP(6),
    CURRENT_TIMESTAMP(6)
);

-- Прив'язка ролей, до створених користувачів.
INSERT INTO user_authorities (
    user_id,
    authority_id,
    created_at,
    updated_at
)
VALUES (
    (SELECT id FROM users WHERE username = 'admin'),
    (SELECT id FROM authorities WHERE name = 'ADMIN'),
    CURRENT_TIMESTAMP(6),
    CURRENT_TIMESTAMP(6)
);

INSERT INTO user_authorities (
    user_id,
    authority_id,
    created_at,
    updated_at
)
VALUES (
    (SELECT id FROM users WHERE username = 'user'),
    (SELECT id FROM authorities WHERE name = 'USER'),
    CURRENT_TIMESTAMP(6),
    CURRENT_TIMESTAMP(6)
);