package pl.edu.agh.mwo.hibernate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "photo")
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToMany(
            mappedBy = "likedPhotos",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE}
    )
    private Set<User> usersWhoLikedPhoto = new HashSet<>();

    @Column(name = "name")
    private String name;

    @Column(name = "date")
    private String date;

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

    public String getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        String formatted = date.format(DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss"));
        this.date = formatted;
    }

    public void addUserWhoLikedPhoto(User user) {
        usersWhoLikedPhoto.add(user);
    }

    public void removeUserWhoLikedPhoto(User user) {
        usersWhoLikedPhoto.remove(user);
    }

    public Set<User> getUsersWhoLikedPhoto() {
        return usersWhoLikedPhoto;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", date=" + date +
                '}';
    }
}
