
CREATE TABLE IF NOT EXISTS owner
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    firstName   VARCHAR(255) NOT NULL,
    lastName    VARCHAR(255) NOT NULL,
    email       VARCHAR(255)
);


CREATE TABLE IF NOT EXISTS horse
(
  id            BIGINT AUTO_INCREMENT PRIMARY KEY,
  name          VARCHAR(255) NOT NULL,
  description   VARCHAR(255),
  birthdate     DATE NOT NULL,
  sex           ENUM ('MALE', 'FEMALE') NOT NULL,
  ownerId       BIGINT,
   FOREIGN KEY (ownerId) REFERENCES owner (id)
);
