package com.example.Book_Fair_Project.controller;



import com.example.Book_Fair_Project.dto.genre.SaveUserGenresRequest;
import com.example.Book_Fair_Project.dto.genre.UserGenresResponse;
import com.example.Book_Fair_Project.service.UserGenreService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user-genres")

public class UserGenreController {

    private final UserGenreService userGenreService;

    public UserGenreController(UserGenreService userGenreService) {
        this.userGenreService = userGenreService;
    }

    // ✅ Replace all genres (recommended for your checkbox UI)
    // POST /api/user-genres/replace
    @PostMapping("/replace")
    public ResponseEntity<UserGenresResponse> replace(@RequestBody SaveUserGenresRequest request) {
        return ResponseEntity.ok(userGenreService.replaceUserGenres(request));
    }

    // ✅ Add new genres (merge)
    // POST /api/user-genres/add
    @PostMapping("/add")
    public ResponseEntity<UserGenresResponse> add(@RequestBody SaveUserGenresRequest request) {
        return ResponseEntity.ok(userGenreService.addUserGenres(request));
    }

    // ✅ Get user genres
    // GET /api/user-genres/{userId}
    @GetMapping("/{userId}")
    public ResponseEntity<UserGenresResponse> get(@PathVariable Long userId) {
        return ResponseEntity.ok(userGenreService.getUserGenres(userId));
    }

    // ✅ Remove one genre
    // DELETE /api/user-genres/{userId}/{genreName}
    @DeleteMapping("/{userId}/{genreName}")
    public ResponseEntity<Void> removeOne(@PathVariable Long userId, @PathVariable String genreName) {
        userGenreService.removeUserGenre(userId, genreName);
        return ResponseEntity.noContent().build();
    }

    // ✅ Clear all genres
    // DELETE /api/user-genres/{userId}
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> clear(@PathVariable Long userId) {
        userGenreService.clearUserGenres(userId);
        return ResponseEntity.noContent().build();
    }
}

