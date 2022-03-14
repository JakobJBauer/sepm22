-- insert initial test data
-- the IDs are hardcoded to enable references between further test data
-- negative IDs are used to not interfere with user-entered data and allow clean deletion of test data

DELETE FROM horse where id < 0;

INSERT INTO horse (name, description, birthdate, sex, owner)
VALUES ('Ramzan', 'Horsus Chonkus', CURRENT_DATE, 'MALE', 'Ashish Negi'),
       ('Ash', 'Minorus Taurus', '2020-09-20', 'MALE', 'Ashish Negis Mom')
;
