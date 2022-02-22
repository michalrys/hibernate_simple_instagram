package pl.edu.agh.mwo.hibernate;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import java.util.stream.Collectors;

public class Main {
    Session session;
    private SimpleLogger logger = new SimpleLogger();

    public static void main(String[] args) {
        //TODO: later merge to branch FEATURE-POINT-3-...
        Main main = new Main();
        main.logger.clearFile();

        new DataBaseReseter().rebuildDatabase();  // USE THIS TO REBUILD DATABASE
        main.logger.write("! Database was rebuilt to original state.\n");
        // STEP 1: print db initial data - in order to have it, run database_setup.sql in SQLite
//        main.printEntitiesForCheck();

        // STEP 2: point 4.1. Adding new data to db
        main.previewDataBase();
        main.addSomeNewData();
        main.previewDataBase();

        // STEP 3: point 4.2.1. Removing like -> db shall be consistent
        main.previewDataBase();
        main.deleteLikes();
        main.previewDataBase();

        // STEP 4: point 4.2.2. Removing photo will delete likes
        main.previewDataBase();
        main.deletePhoto();
        main.previewDataBase();

        // STEP 5: point 4.2.3. Removing album will remove photos
        main.previewDataBase();
        main.deleteAlbum();
        main.previewDataBase();

        // STEP 6: point 4.2.4. Removing user will remove all albums, photos, likes.
        main.previewDataBase();
        main.deleteUser();
        main.previewDataBase();


        main.close();
    }

    private void printEntitiesForCheck() {
        Query<User> query = session.createQuery("from User", User.class);
        List<User> users = query.list();
        System.out.println(users);
        for (User user : users) {
            Set<Album> albums = user.getAlbums();
            System.out.println(user.getName());
            System.out.println("\tAlbums:");
            for (Album album : albums) {
                System.out.println("\t\t" + album.getName());
            }
            System.out.println("\tLiked photos:");
            for (Photo photo : user.getLikedPhotos()) {
                System.out.println("\t\t" + photo.getName());
            }
            System.out.println("----------");
        }

        System.out.println("== photo ==");
        Query<Photo> from_photo = session.createQuery("from Photo", Photo.class);
        List<Photo> photos = from_photo.list();
        System.out.println(photos);

        System.out.println("== Albums ==");
        Query<Album> from_album = session.createQuery("from Album", Album.class);
        List<Album> albums = from_album.list();
        System.out.println(albums);
        for (Album album : albums) {
            System.out.println("\tAlbum: ");
            System.out.println("\t\t" + album);
            System.out.println("\tPhotos:");
            for (Photo photo : album.getPhotos()) {
                System.out.println("\t\t" + photo.getName());
                Set<User> likesByUsers = photo.getUsersWhoLikedPhoto();
                if (likesByUsers.size() != 0) {
                    System.out.println("\t\t\tlikes by:");
                    for (User user : likesByUsers) {
                        System.out.println("\t\t\t\t" + user.getName());
                    }
                } else {
                    System.out.println("\t\t\t\tnobody likes it.");
                }
            }
        }
    }

    private void previewDataBase() {
        logger.append("call: previewDataBase\n");
        Query<User> from_user = session.createQuery("from User", User.class);
        Query<Photo> from_photo = session.createQuery("from Photo", Photo.class);
        Query<Album> from_album = session.createQuery("from Album", Album.class);

        List<User> users = from_user.list();
        List<Photo> photos = from_photo.list();
        List<Album> albums = from_album.list();

//        System.out.println(users);
        System.out.print("Users:\t");
        logger.append("Users:\t");
        int i = 3;
        for (User user : users) {
            List<String> albumsNames = user.getAlbums().stream().map(s -> s.getName()).collect(Collectors.toList());
            System.out.printf("%s(%s), ", user.getName(), albumsNames);
            if (i++ % 3 == 0) {
                logger.append(String.format("\n\t\t%s(%s)", user.getName(), albumsNames));
            } else {
                logger.append(String.format(", %s(%s)", user.getName(), albumsNames));
            }
        }
        System.out.println();

//        System.out.println(photos);
        System.out.print("Photos:\t");
        logger.append("\nPhotos:\t");
        i = 3;
        for (Photo photo : photos) {
            System.out.printf("%s, ", photo.getName());
            if (i++ % 3 == 0) {
                logger.append(String.format("\n\t\t%s", photo.getName()));
            } else {
                logger.append(String.format(", %s", photo.getName()));
            }
        }
        System.out.println();

//        System.out.println(albums);
        System.out.print("Albums:\t");
        logger.append("\nAlbums:\t");
        i = 3;
        for (Album album : albums) {
            System.out.printf("%s, ", album.getName());
            if (i++ % 3 == 0) {
                logger.append(String.format("\n\t\t%s", album.getName()));
            } else {
                logger.append(String.format(", %s", album.getName()));
            }
        }
        System.out.println();
        System.out.println("Likes:\t ");
        logger.append("\nLikes:\t");
        i = 2;
        for (User user : users) {
            List<String> photoNames = user.getLikedPhotos().stream()
                    .map(s -> s.getName())
                    .collect(Collectors.toList());
            System.out.printf("\t%s likes %s\n", user.getName(), photoNames);
            if (i++ % 2 == 0) {
                logger.append(String.format("\n\t\t%s likes %s", user.getName(), photoNames));
            } else {
                logger.append(String.format(", %s likes %s", user.getName(), photoNames));
            }
        }
        logger.append("\n");
        logger.writeStoredMassages();
    }

    private void addSomeNewData() {
        logger.append("call: addSomeNewData\n");
        User userA = new User();
        userA.setName("Anita");
        userA.setJoinDate(LocalDateTime.of(2022, 01, 24, 12, 03, 01));

        Album albumAa = new Album();
        albumAa.setName("Anita's party");
        albumAa.setDescription("Moje imprezowe");

        Album albumAb = new Album();
        albumAb.setName("Art");
        albumAb.setDescription("Sztuka");

        Photo photoAa = new Photo();
        photoAa.setName("Imprezka1.png");
        photoAa.setDate(LocalDateTime.of(2022, 02, 13, 14, 54, 24));
        Photo photoAb = new Photo();
        photoAb.setName("Imprezka2.png");
        photoAb.setDate(LocalDateTime.of(2022, 02, 13, 14, 56, 24));
        Photo photoAc = new Photo();
        photoAc.setName("Impresja1.png");
        photoAc.setDate(LocalDateTime.of(2022, 02, 15, 13, 01, 12));

        albumAa.addPhotos(photoAa);
        albumAa.addPhotos(photoAb);
        albumAb.addPhotos(photoAc);
        logger.append("Create user Anita with\n");
        logger.append("\tAlbum: Anita's party (Moje imprezowe) -> Imprezka1.png, Imprezka2.png\n");
        logger.append("\tAlbum: Art (Sztuka) -> Impresja1.png\n");

        User userB = new User();
        userB.setName("Halina");
        userB.setJoinDate(LocalDateTime.of(2019, 02, 13, 14, 54, 24));

        Album albumBa = new Album();
        albumBa.setName("Halinas fotoski");
        albumBa.setDescription("All in one");

        Photo photoBa = new Photo();
        photoBa.setName("MyFace.png");
        photoBa.setDate(LocalDateTime.of(2020, 01, 24, 10, 14, 00));

        logger.append("Create user Halina with\n");
        logger.append("\tAlbum: Halinas fotoski (All in one) -> MyFace.png\n");

        albumBa.addPhotos(photoBa);

        userA.addLikedPhoto(photoBa);
        userB.addLikedPhoto(photoAa);
        userB.addLikedPhoto(photoAb);

        Transaction transaction = session.beginTransaction();
        session.save(userA);
        session.save(userB);
        session.save(photoAa);
        session.save(photoAb);
        session.save(photoAc);
        session.save(photoBa);
        session.save(albumAa);
        session.save(albumAb);
        session.save(albumBa);
        transaction.commit();
        logger.append("Transaction commited.\n");
        logger.writeStoredMassages();
    }

    private void deleteLikes() {
        logger.append("call: deleteLikes:\n");
        logger.append("\tHalina --> does not like 'Imprezka1.png'\n");
        String queryA = "from Photo where name = 'Imprezka1.png'";
        Query<Photo> query = session.createQuery(queryA, Photo.class);
        Photo photo = query.uniqueResult();

        String queryB = "from User where name = 'Halina'";
        Query<User> query2 = session.createQuery(queryB, User.class);
        User user = query2.uniqueResult();

        user.removeLikedPhoto(photo);

        Transaction transaction = session.beginTransaction();
        session.update(user);
        transaction.commit();
        logger.append("Transcation commited.\n");
        logger.writeStoredMassages();
    }

    private void deletePhoto() {
        logger.append("call: deletePhoto\n");
        logger.append("\tdel: 'Imprezka2.png'  from album: Anita's party (Moje imprezowe)  user: Anita\n");
        Query<Photo> query = session.createQuery("from Photo where name = 'Imprezka2.png'", Photo.class);
        Photo photoToDelete = query.uniqueResult();

        Query<Album> query2 = session.createQuery("from Album where description = 'Moje imprezowe'", Album.class);
        Album album = query2.uniqueResult();
        System.out.println(album);
        album.removePhoto(photoToDelete);

        Query<User> query3 = session.createQuery("from User", User.class);
        List<User> users = query3.list();
        for (User user : users) {
            user.removeLikedPhoto(photoToDelete);
        }

        Transaction transaction = session.beginTransaction();
        session.update(album);
        session.delete(photoToDelete);
        for (User user : users) {
            session.update(user);
        }
        transaction.commit();
        logger.append("Transaction commited.\n");
        logger.writeStoredMassages();
    }

    private void deleteAlbum() {
        logger.append("call: deleteAlbum\n");
        logger.append("\tdel: 'Praca' from user: Romek\n");
        Query<Album> query = session.createQuery("from Album where name = 'Praca'", Album.class);
        Album album = query.uniqueResult();

        //Here I remove albums and photos from user -> I had to remove Cascade.ALL from photo.
        Query<User> from_user = session.createQuery("from User", User.class);
        List<User> users = from_user.list();
        for (User user : users) {
            Set<Album> albums = user.getAlbums();
            albums.remove(album);
            Set<Photo> photos = album.getPhotos();
            for (Photo photo : photos) {
                user.removeLikedPhoto(photo);
            }
        }

        Transaction transaction = session.beginTransaction();
        for (User user : users) {
            session.update(user);
        }

        session.delete(album);
        transaction.commit();
        logger.append("Transaction commited.\n");
        logger.writeStoredMassages();
    }

    private void deleteUser() {
        logger.append("call: deleteUser\n");
        logger.append("\tdel: Janek -> albums: Wakacje2020, Wakacje2019 -> likes: Domowka2.jpg, Domowka1.jpg\n");

        Query<User> queryUser = session.createQuery("from User where name = 'Janek'", User.class);
        User userToDelete = queryUser.uniqueResult();
        Set<Album> albumsOfUserToDelete = userToDelete.getAlbums();

        Query<User> queryUsers = session.createQuery("from User", User.class);
        List<User> users = queryUsers.list();

        for (User user : users) {
            for (Album album : albumsOfUserToDelete) {
                for (Photo photo : album.getPhotos()) {
                    user.removeLikedPhoto(photo);
                }
            }
        }

        Transaction transaction = session.beginTransaction();
        for (User user : users) {
            session.update(user);
        }
        session.delete(userToDelete);
        transaction.commit();
        logger.append("Transaction commited.\n");
        logger.writeStoredMassages();
    }

    public Main() {
        session = HibernateUtil.getSessionFactory().openSession();
    }

    public void close() {
        session.close();
        HibernateUtil.shutdown();
    }
}
