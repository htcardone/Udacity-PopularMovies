package com.htcardone.popularmovies.data.remote;

import com.google.gson.annotations.SerializedName;
import com.htcardone.popularmovies.data.model.Review;

import java.util.List;

@SuppressWarnings("unused")
public class ReviewsResponse {

	@SerializedName("id")
	private int id;

	@SerializedName("page")
	private int page;

	@SerializedName("total_pages")
	private int totalPages;

	@SerializedName("results")
	private List<Review> results;

	@SerializedName("total_results")
	private int totalResults;

	public int getId(){
		return id;
	}

	public int getPage(){
		return page;
	}

	public int getTotalPages(){
		return totalPages;
	}

	public List<Review> getResults(){
		return results;
	}

	public int getTotalResults(){
		return totalResults;
	}

	@Override
 	public String toString(){
		return 
			"ReviewsResponse{" +
			"id = '" + id + '\'' + 
			",page = '" + page + '\'' + 
			",total_pages = '" + totalPages + '\'' + 
			",results = '" + results + '\'' + 
			",total_results = '" + totalResults + '\'' + 
			"}";
		}
}