-- insert initial test data
-- the IDs are hardcoded to enable references between further test data
-- negative IDs are used to not interfere with user-entered data and allow clean deletion of test data

DELETE FROM CHILD_OF where PARENT < 0 OR CHILD < 0;
DELETE FROM horse where id < 0;
DELETE FROM owner where id < 0;

INSERT INTO owner (id, firstName, lastName, email)
VALUES (-1, 'Jack1', 'Jackson1', 'jack1@jackson.com'),
       (-2, 'Jack2', 'Jackson2', null),
       (-3, 'Jack3', 'Jackson3', 'jack3@jackson.com'),
       (-4, 'Jack4', 'Jackson4', 'jack@jack'),
       (-5, 'Jack5', 'Jackson5', 'jack5@jackson.com'),
       (-6, 'Jack6', 'Jackson6', null),
       (-7, 'Jack7', 'Jackson7', null),
       (-8, 'Jack8', 'Jackson8', null),
       (-9, 'Jack9', 'Jackson9', 'jack9@jackson.com'),
       (-10, 'Jack10', 'Jackson10', null);

INSERT INTO horse (id, name, description, birthdate, sex, ownerId)
VALUES (-1, 'Ramzan1', 'Horsus Chonkus', CURRENT_DATE, 'FEMALE', -1),
       (-2, 'Ash2', 'Minorus Taurus', '2020-09-01', 'MALE', -2),
       (-3, 'Ash3', null, '2020-09-02', 'FEMALE', -3),
       (-4, 'Ash4', null, '2020-09-03', 'MALE', -4),
       (-5, 'Ash5', null, '2020-09-04', 'FEMALE', -5),
       (-6, 'Ash6', 'Minorus Taurus', '2020-09-05', 'FEMALE', -6),
       (-7, 'Ash7', 'Nice description', '2020-09-06', 'FEMALE', -7),
       (-8, 'Ash8', 'description 8', '2020-09-07', 'FEMALE', -8),
       (-9, 'Ash9', 'i am reaaaaaaaaaaaaaaaaaaaaaaaaaaaaly looooooooooooooooooooooooooooooooooong', '2020-09-08', 'MALE', -9),
       (-10, 'Jackson', null, '2020-09-09', 'MALE', null),
       (-11, 'Jack', null, '2020-09-10', 'MALE', null),
       (-12, 'Chad', null, '2020-09-11', 'MALE', null),
       (-13, 'SigmaMale', 'Minorus Taurus', '2020-09-12', 'FEMALE', null),
       (-14, 'Ultrachad', 'the real chad', '2020-09-13', 'FEMALE', -1),
       (-15, 'Gimme A', 'pls good grade UwU', '2020-09-14', 'MALE', -1)
;

INSERT INTO CHILD_OF
    (CHILD, PARENT)
    VALUES (-1, -2),
           (-2, -3),
           (-3, -4),
           (-3, -5),
           (-4, -5),
           (-4, -6),
           (-5, -6),
           (-5, -9),
           (-7, -8),
           (-7, -9),
           (-9, -10),
           (-10, -12);
