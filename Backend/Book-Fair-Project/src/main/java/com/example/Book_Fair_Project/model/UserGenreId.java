package com.example.Book_Fair_Project.model;



import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class UserGenreId implements Serializable {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "genre_name", length = 50)
    private String genreName;

    public UserGenreId() {}

    public UserGenreId(Long userId, String genreName) {
        this.userId = userId;
        this.genreName = genreName;
    }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getGenreName() { return genreName; }
    public void setGenreName(String genreName) { this.genreName = genreName; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserGenreId that = (UserGenreId) o;
        return Objects.equals(userId, that.userId) &&
                Objects.equals(genreName, that.genreName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, genreName);
    }
}
