-- RESET DATABASE
DROP TABLE user_photo_likes;
DROP TABLE photo;
DROP TABLE album;
DROP TABLE user;

-- CREATE TABLES
CREATE TABLE user (
    id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    name VARCHAR(100) NOT NULL DEFAULT "Unknown name",
    join_date DATETIME NOT NULL DEFAULT (datetime('now','localtime'))
);

CREATE TABLE album (
    id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER REFERENCES user (id),
    name VARCHAR(100) NOT NULL DEFAULT "My photos",
    description VARCHAR(300) NOT NULL DEFAULT "No description."
);

CREATE TABLE photo (
    id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    album_id INTEGER REFERENCES album (id),
    name VARCHAR(100) NOT NULL DEFAULT "My photos",
    date DATETIME NOT NULL DEFAULT (datetime('now','localtime'))
);

CREATE TABLE user_photo_likes (
    id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER REFERENCES user (id),
    photo_id INTEGER REFERENCES photo (id)
);

-- ADD SOME DATA FOR TEST
INSERT INTO user (name, join_date) VALUES ('Janek', '2001-02-14 12:04:50');
INSERT INTO user (name, join_date) VALUES ('Tomek', '2014-08-07 23:04:50');
INSERT INTO user (name, join_date) VALUES ('Romek', '2020-03-22 08:04:50');
INSERT INTO user (name, join_date) VALUES ('Atomek', '2019-05-02 17:04:50');
SELECT * FROM user;

INSERT INTO album (name, description, user_id) VALUES ('Wakacje2020', 'Fotki z wakacji', 1);
INSERT INTO album (name, description, user_id) VALUES ('Wakacje2019', 'Chorwacja', 1);
INSERT INTO album (name, description, user_id) VALUES ('Urodziny', 'Z urodzin', 2);
INSERT INTO album (name, description, user_id) VALUES ('Praca', 'Z imprez firmowych', 3);
INSERT INTO album (name, description, user_id) VALUES ('Dom', 'Z imprez domowych', 3);
SELECT * FROM album;

INSERT INTO photo (name, date, album_id) VALUES ('Domek1.jpg', '2020-07-14 12:04:50', 1);
INSERT INTO photo (name, date, album_id) VALUES ('Domek2.jpg', '2020-07-14 12:05:12', 1);
INSERT INTO photo (name, date, album_id) VALUES ('Domek3.jpg', '2020-07-14 12:12:54', 1);
INSERT INTO photo (name, date, album_id) VALUES ('Plaza1.jpg', '2019-08-21 10:12:54', 2);
INSERT INTO photo (name, date, album_id) VALUES ('Plaza2.jpg', '2019-08-22 18:12:54', 2);
INSERT INTO photo (name, date, album_id) VALUES ('UrodzinyKaska1.jpg', '2021-01-11 22:01:54', 3);
INSERT INTO photo (name, date, album_id) VALUES ('UrodzinyKaska2.jpg', '2021-01-11 22:05:22', 3);
INSERT INTO photo (name, date, album_id) VALUES ('UrodzinyKaska3.jpg', '2021-01-11 22:11:13', 3);
INSERT INTO photo (name, date, album_id) VALUES ('PracaFimowka1.jpg', '2020-12-22 12:11:19', 4);
INSERT INTO photo (name, date, album_id) VALUES ('PracaFimowka2.jpg', '2020-12-22 14:24:15', 4);
INSERT INTO photo (name, date, album_id) VALUES ('Domowka1.jpg', '2020-12-23 17:24:31', 5);
INSERT INTO photo (name, date, album_id) VALUES ('Domowka2.jpg', '2020-12-23 18:34:24', 5); --12
SELECT * FROM photo;

INSERT INTO user_photo_likes (user_id, photo_id) VALUES (1, 10);
INSERT INTO user_photo_likes (user_id, photo_id) VALUES (1, 11);
INSERT INTO user_photo_likes (user_id, photo_id) VALUES (1, 12);
INSERT INTO user_photo_likes (user_id, photo_id) VALUES (2, 1);
INSERT INTO user_photo_likes (user_id, photo_id) VALUES (2, 2);
INSERT INTO user_photo_likes (user_id, photo_id) VALUES (4, 4);
INSERT INTO user_photo_likes (user_id, photo_id) VALUES (4, 5);
SELECT * FROM user_photo_likes;


--------------- OTHER REVIEWS
SELECT u.name AS "User name", p.name AS "liked photo name", p.date AS "Photo date"
FROM user_photo_likes as upl
JOIN user AS u ON upl.user_id = u.id
JOIN photo AS p ON upl.photo_id = p.id;

SELECT * FROM photo AS p
JOIN album AS a ON p.album_id = a.id;

SELECT * FROM user AS u
LEFT OUTER JOIN album AS a ON a.user_id = u.id;

SELECT * FROM album AS a
INNER JOIN user AS u ON a.user_id = u.id;