INSERT INTO students (student_number, first_name, last_name) VALUES (20240001, 'Jesse', 'Pinkman');
INSERT INTO students (student_number, first_name, last_name) VALUES (20240002, 'Hector', 'Salamanca');
INSERT INTO students (student_number, first_name, last_name) VALUES (20240003, 'Kim', 'Wexler');

INSERT INTO faculty (faculty_number, first_name, last_name) VALUES (20160916, 'Walter', 'White');
INSERT INTO faculty (faculty_number, first_name, last_name) VALUES (20180354, 'Saul', 'Goodman');

INSERT INTO users (username, password_hash, role) VALUES ('ST-20240001', '{noop}JessePinkman', 'STUDENT');
INSERT INTO users (username, password_hash, role) VALUES ('ST-20240002', '{noop}HectorSalamanca', 'STUDENT');
INSERT INTO users (username, password_hash, role) VALUES ('ST-20240003', '{noop}KimWexler', 'STUDENT');
INSERT INTO users (username, password_hash, role) VALUES ('FC-20160916', '{noop}WalterWhite', 'FACULTY');
INSERT INTO users (username, password_hash, role) VALUES ('FC-20180354', '{noop}SaulGoodman', 'FACULTY');

INSERT INTO subjects VALUES ('ES 1');
INSERT INTO subjects VALUES ('PI 100');
INSERT INTO subjects VALUES ('COMM 2');
INSERT INTO subjects VALUES ('KAS 1');

INSERT INTO rooms (name, capacity) VALUES ('PH102', 30);
INSERT INTO rooms (name, capacity) VALUES ('MH204', 1);
INSERT INTO rooms (name, capacity) VALUES ('NEC310', 30);

INSERT INTO sections (section_id, subject_id, schedule, room_name, faculty_number) VALUES ('ES1TFXY', 'ES 1', 'TF 14:30-17:30', 'MH204', 20160916);
INSERT INTO sections (section_id, subject_id, schedule, room_name, faculty_number) VALUES ('ES1MHXY', 'ES 1', 'MTH 14:30-17:30', 'MH204', 20160916);
INSERT INTO sections (section_id, subject_id, schedule, room_name, faculty_number) VALUES ('KAS1MHR1', 'KAS 1', 'MTH 08:30-10:00', 'PH102', 20160916);
INSERT INTO sections (section_id, subject_id, schedule, room_name, faculty_number) VALUES ('PI100TFWX', 'PI 100', 'TF 13:00-16:00', 'PH102', 20160916);

INSERT INTO enlistments (student_number, section_id) VALUES (20240001, 'ES1TFXY');