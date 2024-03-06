package com.my.column.service;

import com.my.column.entity.Film;

import java.util.List;

public interface IFilmService {
	Film getFilmById(String film_id);
	List<Film> getFilmUnLucene();
	void updateFilmLucene(String film_id);
	void getFilmLucene();
}
