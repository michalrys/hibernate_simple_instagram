package pl.edu.agh.mwo.hibernate;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "album")
public class Album {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "album_id")
    private Set<Photo> photos = new HashSet<>();

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void addPhotos(Photo photo) {
        photos.add(photo);
    }

    public Set<Photo> getPhotos() {
        return photos;
    }

    public void removePhoto(Photo photo) {
        photos.remove(photo);
    }

    @Override
    public String toString() {
        return "Album{" +
                "id=" + id +
                ", photos=" + photos +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
