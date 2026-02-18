package com.example.Book_Fair_Project.service;

import com.example.Book_Fair_Project.dto.genre.SaveUserGenresRequest;
import com.example.Book_Fair_Project.dto.genre.UserGenresResponse;
import com.example.Book_Fair_Project.exception.BadRequestException;
import com.example.Book_Fair_Project.exception.NotFoundException;
import com.example.Book_Fair_Project.model.User;
import com.example.Book_Fair_Project.model.UserGenre;
import com.example.Book_Fair_Project.model.UserGenreId;
import com.example.Book_Fair_Project.repository.UserGenreRepository;
import com.example.Book_Fair_Project.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserGenreServiceImpl implements UserGenreService {

    private final UserGenreRepository userGenreRepository;
    private final UserRepository userRepository;

    public UserGenreServiceImpl(UserGenreRepository userGenreRepository,
                                UserRepository userRepository) {
        this.userGenreRepository = userGenreRepository;
        this.userRepository = userRepository;
    }

    @Override
    public UserGenresResponse replaceUserGenres(SaveUserGenresRequest request) {
        validateRequest(request);

        Long userId = request.getUserId();
        User user = getUserOrThrow(userId);

        // delete old
        userGenreRepository.deleteByUser_UserId(userId);

        // insert new
        List<String> normalized = normalizeGenres(request.getGenres());
        List<UserGenre> toSave = normalized.stream()
                .map(g -> new UserGenre(new UserGenreId(userId, g), user))
                .toList();

        userGenreRepository.saveAll(toSave);

        return new UserGenresResponse(userId, normalized);
    }

    @Override
    public void replaceGenres(Long userId, List<String> genres) {

    }

    @Override
    public UserGenresResponse addUserGenres(SaveUserGenresRequest request) {
        validateRequest(request);

        Long userId = request.getUserId();
        User user = getUserOrThrow(userId);

        List<String> normalized = normalizeGenres(request.getGenres());

        List<UserGenre> toSave = new ArrayList<>();
        for (String g : normalized) {
            UserGenreId id = new UserGenreId(userId, g);
            if (!userGenreRepository.existsById(id)) {
                toSave.add(new UserGenre(id, user));
            }
        }

        if (!toSave.isEmpty()) userGenreRepository.saveAll(toSave);

        // return latest list
        return getUserGenres(userId);
    }

    @Override
    public void removeUserGenre(Long userId, String genreName) {
        if (userId == null) throw new BadRequestException("userId is required");
        if (genreName == null || genreName.trim().isEmpty())
            throw new BadRequestException("genreName is required");

        String g = genreName.trim();
        UserGenreId id = new UserGenreId(userId, g);

        if (!userGenreRepository.existsById(id)) {
            throw new NotFoundException("Genre not found for user: " + g);
        }

        userGenreRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public UserGenresResponse getUserGenres(Long userId) {
        if (userId == null) throw new BadRequestException("userId is required");
        getUserOrThrow(userId); // ensure user exists

        List<String> genres = userGenreRepository.findByUser_UserId(userId).stream()
                .map(ug -> ug.getId().getGenreName())
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .toList();

        return new UserGenresResponse(userId, genres);
    }

    @Override
    public void clearUserGenres(Long userId) {
        if (userId == null) throw new BadRequestException("userId is required");
        getUserOrThrow(userId);
        userGenreRepository.deleteByUser_UserId(userId);
    }

    // -------------------------
    // Helpers
    // -------------------------
    private void validateRequest(SaveUserGenresRequest request) {
        if (request == null) throw new BadRequestException("Request body is missing");
        if (request.getUserId() == null) throw new BadRequestException("userId is required");
        if (request.getGenres() == null) throw new BadRequestException("genres list is required");
        if (request.getGenres().isEmpty()) throw new BadRequestException("genres list cannot be empty");
    }

    private User getUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found: " + userId));
    }

    private List<String> normalizeGenres(List<String> input) {
        // trim + remove blanks + unique (case-insensitive) but keep original casing of first occurrence
        Map<String, String> uniq = new LinkedHashMap<>();
        for (String g : input) {
            if (g == null) continue;
            String trimmed = g.trim();
            if (trimmed.isEmpty()) continue;
            uniq.putIfAbsent(trimmed.toLowerCase(Locale.ROOT), trimmed);
        }
        return new ArrayList<>(uniq.values());
    }
}

