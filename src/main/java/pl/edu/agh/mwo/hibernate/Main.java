package pl.edu.agh.mwo.hibernate;

import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Set;

public class Main {

    Session session;

    public static void main(String[] args) {
        Main main = new Main();
        // tu wstaw kod aplikacji
        main.printEntitiesForCheck();

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

    public Main() {
        session = HibernateUtil.getSessionFactory().openSession();
    }

    public void close() {
        session.close();
        HibernateUtil.shutdown();
    }
}
