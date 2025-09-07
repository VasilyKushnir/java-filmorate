package ru.yandex.practicum.filmorate.dal.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class FilmRowMapper implements RowMapper<Film> {
    private final GenreService genreService;

    @Override
    public Film mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(resultSet.getLong("id"));
        film.setName(resultSet.getString("name"));
        film.setDescription(resultSet.getString("description"));

        LocalDate releaseDate = resultSet.getDate("release_date").toLocalDate();
        film.setReleaseDate(releaseDate);

        film.setDuration(resultSet.getInt("duration"));

        film.setGenres(genreService.getGenresForFilm(film.getId()));

        Mpa mpa = new Mpa();
        mpa.setId(resultSet.getInt("mpa_id"));
        mpa.setName(resultSet.getString("mpa_name"));
        film.setMpa(mpa);

        return film;
    }
}