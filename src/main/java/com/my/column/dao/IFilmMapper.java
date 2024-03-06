package com.my.column.dao;

import com.my.column.entity.Film;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface IFilmMapper {
	List<Film> getFilmById(String film_id);
	List<Film> getFilmUnLucene();
	void updateFilmLucene(String film_id);
}
