package com.htcardone.popularmovies.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@SuppressWarnings("unused")
public class Movie {

	@SerializedName("overview")
	private String overview;

	@SerializedName("original_title")
	private String originalTitle;

	@SerializedName("title")
	private String title;

	@SerializedName("poster_path")
	private String posterPath;

	@SerializedName("backdrop_path")
	private String backdropPath;

	@SerializedName("release_date")
	private String releaseDate;

	@SerializedName("vote_average")
	private float voteAverage;

	@SerializedName("id")
	private int id;

	public Movie(String overview, String originalTitle, String title, String posterPath,
				 String backdropPath, String releaseDate, float voteAverage, int id) {
		this.overview = overview;
		this.originalTitle = originalTitle;
		this.title = title;
		this.posterPath = posterPath;
		this.backdropPath = backdropPath;
		this.releaseDate = releaseDate;
		this.voteAverage = voteAverage;
		this.id = id;
	}

	public String getOverview(){
		return overview;
	}

	public String getOriginalTitle(){
		return originalTitle;
	}

	public String getTitle(){
		return title;
	}

	public String getPosterPath(){
		return posterPath;
	}

	public String getBackdropPath(){
		return backdropPath;
	}

	public String getReleaseDate(){
		return releaseDate;
	}

	public float getVoteAverage(){
		return voteAverage;
	}

	public int getId(){
		return id;
	}

	@Override
	public String toString() {
		return "Movie{" +
				"overview='" + overview + '\'' +
				", originalTitle='" + originalTitle + '\'' +
				", title='" + title + '\'' +
				", posterPath='" + posterPath + '\'' +
				", backdropPath='" + backdropPath + '\'' +
				", releaseDate='" + releaseDate + '\'' +
				", voteAverage=" + voteAverage +
				", id=" + id +
				'}';
	}
}