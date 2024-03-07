package com.my.column.controller.rest;

import com.my.column.entity.*;
import com.my.column.service.IFilmService;
import com.my.column.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/film")
public class FilmController {

	@Autowired
	private IFilmService filmService;

	/**
	 * @RequestMapping(value="/showFilm") public String showFilm(Model model) {
	 *                                    List<Film>
	 *                                    fs=filmService.getFilmUnLucene();
	 *                                    model.addAttribute("films",fs); return
	 *                                    "index"; }
	 **/
	@GetMapping({"/search", "/search.html"})
	public String loginPage() {
		return "search";
	}
//	@RequestMapping(value = "/searchFilm", produces = "text/html;charset=UTF-8")
	@RequestMapping(value = "/searchFilm")
	@ResponseBody
	public SearchResult<Film> searchFilm(Model model, String search_str, int start, int limits) {
		SearchResult<Film> fs = SearchService.searchFilm(search_str, start, limits);
		return fs;
	}

	@RequestMapping(value = "/searchActor")
	public @ResponseBody SearchResult<Actor> searchActor(Model model, String search_str, int start, int limits) {
		SearchResult<Actor> as = SearchService.searchActor(search_str, start, limits);
		return as;
	}

	@GetMapping("/updateFilm")
	public String articleDetailPage(HttpServletRequest request) {
		filmService.getFilmLucene();
		return "success";
	}
}
