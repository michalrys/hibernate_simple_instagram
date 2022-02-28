package pl.edu.agh.mwo.hibernate;

import java.sql.*;

public class DataBaseReseter {

    public void rebuildDatabase() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:database.db", "", "");
            rebuild(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void rebuild(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();

        statement.addBatch("DROP TABLE user_photo_likes;");
        statement.addBatch("DROP TABLE photo;");
        statement.addBatch("DROP TABLE album;");
        statement.addBatch("DROP TABLE user;");
        statement.addBatch("DROP TABLE user_friend_of_user;");

        statement.addBatch("CREATE TABLE user (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,name VARCHAR(100) NOT NULL DEFAULT \"Unknown name\",join_date DATETIME NOT NULL DEFAULT (datetime('now','localtime')));");
        statement.addBatch("CREATE TABLE album (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,user_id INTEGER REFERENCES user (id),name VARCHAR(100) NOT NULL DEFAULT \"My photos\",description VARCHAR(300) NOT NULL DEFAULT \"No description.\");");
        statement.addBatch("CREATE TABLE photo (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,album_id INTEGER REFERENCES album (id),name VARCHAR(100) NOT NULL DEFAULT \"My photos\",date DATETIME NOT NULL DEFAULT (datetime('now','localtime')));");
        statement.addBatch("CREATE TABLE user_photo_likes (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,user_id INTEGER REFERENCES user (id),photo_id INTEGER REFERENCES photo (id));");
        statement.addBatch("CREATE TABLE user_friend_of_user(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,user_id INTEGER REFERENCES user(id),user_friend_id INTEGER REFERENCES user(id));");

        statement.addBatch("INSERT INTO user (name, join_date) VALUES ('Janek', '2001-02-14 12:04:50');");
        statement.addBatch("INSERT INTO user (name, join_date) VALUES ('Tomek', '2014-08-07 23:04:50');");
        statement.addBatch("INSERT INTO user (name, join_date) VALUES ('Romek', '2020-03-22 08:04:50');");
        statement.addBatch("INSERT INTO user (name, join_date) VALUES ('Atomek', '2019-05-02 17:04:50');");

        statement.addBatch("INSERT INTO album (name, description, user_id) VALUES ('Wakacje2020', 'Fotki z wakacji', 1);");
        statement.addBatch("INSERT INTO album (name, description, user_id) VALUES ('Wakacje2019', 'Chorwacja', 1);");
        statement.addBatch("INSERT INTO album (name, description, user_id) VALUES ('Urodziny', 'Z urodzin', 2);");
        statement.addBatch("INSERT INTO album (name, description, user_id) VALUES ('Praca', 'Z imprez firmowych', 3);");
        statement.addBatch("INSERT INTO album (name, description, user_id) VALUES ('Dom', 'Z imprez domowych', 3);");

        statement.addBatch("INSERT INTO photo (name, date, album_id) VALUES ('Domek1.jpg', '2020-07-14 12:04:50', 1);");
        statement.addBatch("INSERT INTO photo (name, date, album_id) VALUES ('Domek2.jpg', '2020-07-14 12:05:12', 1);");
        statement.addBatch("INSERT INTO photo (name, date, album_id) VALUES ('Domek3.jpg', '2020-07-14 12:12:54', 1);");
        statement.addBatch("INSERT INTO photo (name, date, album_id) VALUES ('Plaza1.jpg', '2019-08-21 10:12:54', 2);");
        statement.addBatch("INSERT INTO photo (name, date, album_id) VALUES ('Plaza2.jpg', '2019-08-22 18:12:54', 2);");
        statement.addBatch("INSERT INTO photo (name, date, album_id) VALUES ('UrodzinyKaska1.jpg', '2021-01-11 22:01:54', 3);");
        statement.addBatch("INSERT INTO photo (name, date, album_id) VALUES ('UrodzinyKaska2.jpg', '2021-01-11 22:05:22', 3);");
        statement.addBatch("INSERT INTO photo (name, date, album_id) VALUES ('UrodzinyKaska3.jpg', '2021-01-11 22:11:13', 3);");
        statement.addBatch("INSERT INTO photo (name, date, album_id) VALUES ('PracaFimowka1.jpg', '2020-12-22 12:11:19', 4);");
        statement.addBatch("INSERT INTO photo (name, date, album_id) VALUES ('PracaFimowka2.jpg', '2020-12-22 14:24:15', 4);");
        statement.addBatch("INSERT INTO photo (name, date, album_id) VALUES ('Domowka1.jpg', '2020-12-23 17:24:31', 5);");
        statement.addBatch("INSERT INTO photo (name, date, album_id) VALUES ('Domowka2.jpg', '2020-12-23 18:34:24', 5);");

        statement.addBatch("INSERT INTO user_photo_likes (user_id, photo_id) VALUES (1, 10);");
        statement.addBatch("INSERT INTO user_photo_likes (user_id, photo_id) VALUES (1, 11);");
        statement.addBatch("INSERT INTO user_photo_likes (user_id, photo_id) VALUES (1, 12);");
        statement.addBatch("INSERT INTO user_photo_likes (user_id, photo_id) VALUES (2, 1);");
        statement.addBatch("INSERT INTO user_photo_likes (user_id, photo_id) VALUES (2, 2);");
        statement.addBatch("INSERT INTO user_photo_likes (user_id, photo_id) VALUES (4, 4);");
        statement.addBatch("INSERT INTO user_photo_likes (user_id, photo_id) VALUES (4, 5);");

        statement.addBatch("INSERT INTO user_friend_of_user (user_id, user_friend_id) VALUES (1, 2);");
        statement.addBatch("INSERT INTO user_friend_of_user (user_id, user_friend_id) VALUES (1, 3);");
        statement.addBatch("INSERT INTO user_friend_of_user (user_id, user_friend_id) VALUES (1, 4);");

        statement.executeBatch();
        statement.close();
        connection.close();
    }
}