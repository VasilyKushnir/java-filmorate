package ru.yandex.practicum.filmorate.dal;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.storage.LikesStorage;

@Primary
@Repository
@RequiredArgsConstructor
public class LikesRepository implements LikesStorage {
    private final JdbcTemplate jdbc;

    private static final String ADD_LIKE_QUERY = """
            INSERT INTO users_ids_likes (
                user_id,
                film_id
            )
            VALUES (?, ?)
            """;

    private static final String REMOVE_LIKE_QUERY = """
            DELETE FROM users_ids_likes
            WHERE user_id = ?
              AND film_id = ?
            """;

    public void addLike(Long userId, Long filmId) {
        jdbc.update(ADD_LIKE_QUERY, userId, filmId);
    }

    public void removeLike(Long userId, Long filmId) {
        jdbc.update(REMOVE_LIKE_QUERY, userId, filmId);
    }
}