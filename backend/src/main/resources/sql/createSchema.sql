CREATE TABLE IF NOT EXISTS horse
(
  id            BIGINT AUTO_INCREMENT PRIMARY KEY,
  name          VARCHAR(255) NOT NULL,
  description   VARCHAR(255),
  birthdate     DATE NOT NULL,
  sex           ENUM ('MALE', 'FEMALE') NOT NULL,
  owner         VARCHAR(255)
);