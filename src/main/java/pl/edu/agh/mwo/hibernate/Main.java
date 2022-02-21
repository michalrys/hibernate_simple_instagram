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

    public static void main(String[] args) {
        Main main = new Main();
        // STEP 1: print db initial data - in order to have it, run database_setup.sql in SQLite
//        main.printEntitiesForCheck();

        // STEP 2: point 4.1. Adding new data to db
//        main.printLikesBeforeModification();
//        main.addSomeNewData();
//        main.printLikesBeforeModification();

        // STEP 3: point 4.2.1. Removing like -> db shall be consistent
//        main.printLikesBeforeModification();
//        main.deleteLikes();
//        main.printLikesBeforeModification();

        // STEP 4: point 4.2.2. Removing photo will delete likes
        main.printLikesBeforeModification();
        main.deletePhoto();
        main.printLikesBeforeModification();



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
            for (Photo photo : user.getPhotos()) {
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
                Set<User> likesByUsers = photo.getUsers();
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

    private void printLikesBeforeModification() {
        Query<User> from_user = session.createQuery("from User", User.class);
        Query<Photo> from_photo = session.createQuery("from Photo", Photo.class);
        Query<Album> from_album = session.createQuery("from Album", Album.class);

        List<User> users = from_user.list();
        List<Photo> photos = from_photo.list();
        List<Album> albums = from_album.list();

        System.out.println(users);
        System.out.println(photos);
        System.out.println(albums);
        for (User user : users) {
            List<String> photoNames = user.getPhotos().stream()
                    .map(s -> s.getName())
                    .collect(Collectors.toList());
            System.out.printf("\t%s likes %s\n", user.getName(), photoNames);
        }
    }

    private void addSomeNewData() {
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

        User userB = new User();
        userB.setName("Halina");
        userB.setJoinDate(LocalDateTime.of(2019, 02, 13, 14, 54, 24));

        Album albumBa = new Album();
        albumBa.setName("Halinas fotoski");
        albumBa.setDescription("All in one");

        Photo photoBa = new Photo();
        photoBa.setName("MyFace.png");
        photoBa.setDate(LocalDateTime.of(2020, 01, 24, 10, 14, 00));

        albumBa.addPhotos(photoBa);

        userA.addPhoto(photoBa);
        userB.addPhoto(photoAa);
        userB.addPhoto(photoAb);

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
    }

    private void deleteLikes() {
        String queryA = "from Photo where name = 'Imprezka1.png'";
        Query<Photo> query = session.createQuery(queryA, Photo.class);
        Photo photo = query.uniqueResult();

        String queryB = "from User where name = 'Halina'";
        Query<User> query2 = session.createQuery(queryB, User.class);
        User user = query2.uniqueResult();

        user.removePhoto(photo);

        Transaction transaction = session.beginTransaction();
        session.update(user);
        transaction.commit();

    }

    private void deletePhoto() {
        Query<Photo> query = session.createQuery("from Photo where name = 'Imprezka2.png'", Photo.class);
        Photo photoToDelete = query.uniqueResult();

        Transaction transaction = session.beginTransaction();
        session.delete(photoToDelete);
        transaction.commit();
    }

    public Main() {
        session = HibernateUtil.getSessionFactory().openSession();
    }

    public void close() {
        session.close();
        HibernateUtil.shutdown();
    }
}
