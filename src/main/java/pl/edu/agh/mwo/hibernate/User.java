package pl.edu.agh.mwo.hibernate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToMany(cascade = CascadeType.ALL) // ,orphanRemoval = true TODO test it
    @JoinColumn(name = "user_id")
    private Set<Album> albums = new HashSet<>();

//    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "user_photo_likes",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "photo_id")
    )
    private Set<Photo> likedPhotos = new HashSet<>();

    @Column(name = "name")
    private String name;

    @Column(name = "join_date")
    private String joinDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(LocalDateTime joinDate) {
        String formatted = joinDate.format(DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss"));
        this.joinDate = formatted;
    }

    public void addAlbum(Album album) {
        albums.add(album);
    }

    public void removeAlbum(Album album) {
        albums.remove(album);
    }

    public Set<Album> getAlbums() {
        return albums;
    }

    public void addLikedPhoto(Photo photo) {
        likedPhotos.add(photo);
    }

    public void removeLikedPhoto(Photo photo) {
        likedPhotos.remove(photo);
    }

    public Set<Photo> getLikedPhotos() {
        return likedPhotos;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", albums=" + albums +
                ", name='" + name + '\'' +
                ", joinDate=" + joinDate +
                '}';
    }
}