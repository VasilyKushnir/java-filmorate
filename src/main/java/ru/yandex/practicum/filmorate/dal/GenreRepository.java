package ru.yandex.practicum.filmorate.dal;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Primary
@Repository
public class GenreRepository extends BaseRepository<Genre> implements GenreStorage {
    private static final String FIND_ALL_QUERY = """
            SELECT *
            FROM genres
            """;

    private static final String FIND_BY_ID_QUERY = """
            SELECT *
            FROM genres
            WHERE id = ?
            """;

    private static final String FIND_GENRES_FOR_FILM_QUERY = """
            SELECT g.*
            FROM genres AS g
            JOIN films_genres AS fg
              ON g.id = fg.genre_id
            WHERE fg.film_id = ?
            """;

    public GenreRepository(JdbcTemplate jdbc, RowMapper<Genre> mapper) {
        super(jdbc, mapper);
    }

    public List<Genre> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    public Optional<Genre> findGenre(Integer genreId) {
        return findOne(FIND_BY_ID_QUERY, genreId);
    }

    public Set<Genre> findGenresForFilm(Long filmId) {
        return new HashSet<>(findMany(FIND_GENRES_FOR_FILM_QUERY, filmId));
    }
}