package pl.edu.agh.mwo.hibernate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private Set<Album> albums = new HashSet<>();

    @Column(name = "name")
    private String name;

    @Column(name = "join_date")
    private LocalDateTime joinDate;

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

    public LocalDateTime getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(LocalDateTime joinDate) {
        this.joinDate = joinDate;
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