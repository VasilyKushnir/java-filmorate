package ru.yandex.practicum.filmorate.dal;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Primary
@Repository
public class FilmRepository extends BaseRepository<Film> implements FilmStorage {
    private static final String FIND_ALL_QUERY = """
            SELECT f.*, m.name AS mpa_name
            FROM films AS f
            JOIN mpa AS m
              ON f.mpa_id = m.id
            """;

    private static final String FIND_BY_ID_QUERY = """
            SELECT f.*, m.name AS mpa_name
            FROM films AS f
            JOIN mpa AS m
              ON f.mpa_id = m.id
            WHERE f.id = ?
            """;

    private static final String INSERT_QUERY = """
            INSERT INTO films (
                name,
                description,
                release_date,
                duration,
                mpa_id
            )
            VALUES (?, ?, ?, ?, ?)
            """;

    private static final String INSERT_FILM_GENRE_QUERY = """
            INSERT INTO films_genres (
                film_id,
                genre_id
            )
            VALUES (?, ?)
            """;

    private static final String DELETE_FILM_GENRES_QUERY = """
            DELETE FROM films_genres
            WHERE film_id = ?
            """;

    private static final String UPDATE_QUERY = """
            UPDATE films
            SET name = ?,
                description = ?,
                release_date = ?,
                duration = ?,
                mpa_id = ?
            WHERE id = ?
            """;

    private static final String FIND_MOST_POPULAR_QUERY = """
            SELECT COUNT(uil.film_id) AS count, f.*, m.name AS mpa_name
            FROM users_ids_likes AS uil
            JOIN films AS f ON uil.film_id = f.id
            JOIN mpa AS m
              ON f.mpa_id = m.id
            GROUP BY film_id
            ORDER BY count DESC
            LIMIT ?
            """;

    private static final String FIND_FILMS_GENRES_TEMPLATE = """
            SELECT g.id, g.name, fg.film_id
            FROM films_genres AS fg
            JOIN genres AS g ON g.id = fg.genre_id
            WHERE fg.film_id IN (SELECT f.id FROM (%s) f)
            """;

    JdbcTemplate jdbc;
    RowMapper<Genre> genreMapper;

    public FilmRepository(JdbcTemplate jdbc, RowMapper<Film> mapper, RowMapper<Genre> genreMapper) {
        super(jdbc, mapper);
        this.jdbc = jdbc;
        this.genreMapper = genreMapper;
    }

    public List<Film> findAll() {
        List<Film> films = findMany(FIND_ALL_QUERY);
        this.setGenres(films, this.findFilmGenres(FIND_ALL_QUERY));

        return films;
    }

    public Optional<Film> findFilm(Long filmId) {
        Optional<Film> film = findOne(FIND_BY_ID_QUERY, filmId);
        film.ifPresent(value ->
                this.setGenres(List.of(value), this.findFilmGenres(FIND_BY_ID_QUERY, filmId)));

        return film;
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

    public List<Film> findMostPopular(Integer count) {
        List<Film> films = findMany(FIND_MOST_POPULAR_QUERY, count);
        this.setGenres(films, this.findFilmGenres(FIND_MOST_POPULAR_QUERY, count));

        return films;
    }

    private List<FilmGenre> findFilmGenres(String query, Object... params) {
        RowMapper<FilmGenre> mapper = (rs, rowNum) -> new FilmGenre(
                rs.getLong("film_id"),
                this.genreMapper.mapRow(rs, rowNum));

        return jdbc.query(FIND_FILMS_GENRES_TEMPLATE.formatted(query), mapper, params);
    }

    private void setGenres(List<Film> films, List<FilmGenre> genres) {
        films.forEach(film -> genres.stream()
                .filter(g -> Objects.equals(g.getFilmId(), film.getId()))
                .forEach(g -> film.getGenres().add(g.getGenre()))
        );
    }
}