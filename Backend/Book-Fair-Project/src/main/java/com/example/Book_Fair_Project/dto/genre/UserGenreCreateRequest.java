package com.example.Book_Fair_Project.dto.genre;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UserGenreCreateRequest {

    @NotNull
    private Long userId;

    @NotBlank
    @Size(max = 50)
    private String genreName;

    public UserGenreCreateRequest() {}

    public UserGenreCreateRequest(Long userId, String genreName) {
        this.userId = userId;
        this.genreName = genreName;
    }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getGenreName() { return genreName; }
    public void setGenreName(String genreName) { this.genreName = genreName; }
}

