package com.example.Book_Fair_Project.model;


import jakarta.persistence.*;

@Entity
@Table(name = "user_genres")
public class UserGenre {

    @EmbeddedId
    private UserGenreId id;

    @ManyToOne(optional = false)
    @MapsId("userId")
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public UserGenre() {}

    public UserGenre(UserGenreId id, User user) {
        this.id = id;
        this.user = user;
    }

    public UserGenreId getId() { return id; }
    public void setId(UserGenreId id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
