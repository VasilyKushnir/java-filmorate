package ru.yandex.practicum.filmorate.dal;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Primary
@Repository
public class FilmRepository extends BaseRepository<Film> implements FilmStorage {
    private static final String FIND_ALL_QUERY =
            "SELECT f.*, m.name AS mpa_name " +
                    "FROM films AS f " +
                    "JOIN mpa AS m ON f.mpa_id = m.id";

    private static final String FIND_BY_ID_QUERY =
            "SELECT f.*, m.name AS mpa_name " +
                    "FROM films AS f " +
                    "JOIN mpa AS m ON f.mpa_id = m.id " +
                    "WHERE f.id = ?";

    private static final String INSERT_QUERY =
            "INSERT INTO films (name, description, release_date, duration, mpa_id)" +
                    "VALUES (?, ?, ?, ?, ?)";

    private static final String INSERT_FILM_GENRE_QUERY =
            "INSERT INTO films_genres (film_id, genre_id) " +
                    "VALUES (?, ?)";

    private static final String DELETE_FILM_GENRES_QUERY =
            "DELETE FROM films_genres " +
                    "WHERE film_id = ?";

    private static final String UPDATE_QUERY =
            "UPDATE films SET name = ?, description = ?, release_date = ?, duration = ?, mpa_id = ? " +
                    "WHERE id = ?";

    private static final String FIND_MOST_POPULAR_QUERY =
            "SELECT COUNT(uil.film_id) AS count, f.*, m.name AS mpa_name " +
                    "FROM users_ids_likes AS uil " +
                    "JOIN films AS f ON uil.film_id = f.id " +
                    "JOIN mpa AS m ON f.mpa_id = m.id " +
                    "GROUP BY film_id " +
                    "ORDER BY count DESC " +
                    "LIMIT ?";


    public FilmRepository(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper);
    }

    public List<Film> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    public Optional<Film> getFilm(Long filmId) {
        return findOne(FIND_BY_ID_QUERY, filmId);
    }

    public Film add(Film film) {
        Mpa mpa = film.getMpa();
        Integer mpaId = null;

        if (mpa != null) {
            mpaId = mpa.getId();
        }

        long id = insert(
                INSERT_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate().format(DateTimeFormatter.ISO_LOCAL_DATE),
                film.getDuration(),
                mpaId
        );
        film.setId(id);

        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                update(INSERT_FILM_GENRE_QUERY, film.getId(), genre.getId());
            }
        }

        return film;
    }

    public Film update(Film film) {
        Mpa mpa = film.getMpa();
        Integer mpaId = null;

        if (mpa != null) {
            mpaId = mpa.getId();
        }

        update(
                UPDATE_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate().format(DateTimeFormatter.ISO_LOCAL_DATE),
                film.getDuration(),
                mpaId,
                film.getId()
        );

        if (film.getGenres() != null) {
            update(DELETE_FILM_GENRES_QUERY, film.getId());
            for (Genre genre : film.getGenres()) {
                update(INSERT_FILM_GENRE_QUERY, film.getId(), genre.getId());
            }
        }

        return film;
    }

    public List<Film> fetchMostPopular(Integer count) {
        return findMany(FIND_MOST_POPULAR_QUERY, count);
    }
}