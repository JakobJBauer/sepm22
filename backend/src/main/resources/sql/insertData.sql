-- insert initial test data
-- the IDs are hardcoded to enable references between further test data
-- negative IDs are used to not interfere with user-entered data and allow clean deletion of test data

DELETE FROM horse where id < 0;

INSERT INTO owner (firstName, lastName, email)
VALUES ('Jack', 'Jackson', 'jack@jackson.com');

INSERT INTO horse (name, description, birthdate, sex, ownerId)
VALUES ('Ramzan', 'Horsus Chonkus', CURRENT_DATE, 'MALE', 1),
       ('Ash', 'Minorus Taurus', '2020-09-20', 'MALE', 1)
;

-- INSERT INTO child_of(child, parent)
-- VALUES (1, 2);
