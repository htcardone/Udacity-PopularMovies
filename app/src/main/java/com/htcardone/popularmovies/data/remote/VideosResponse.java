package com.htcardone.popularmovies.data.remote;

import com.google.gson.annotations.SerializedName;
import com.htcardone.popularmovies.data.model.Video;

import java.util.List;

@SuppressWarnings("unused")
public class VideosResponse{

	@SerializedName("id")
	private int id;

	@SerializedName("results")
	private List<Video> results;

	public int getId(){
		return id;
	}

	public List<Video> getResults(){
		return results;
	}

	@Override
 	public String toString(){
		return 
			"VideosResponse{" + 
			"id = '" + id + '\'' + 
			",results = '" + results + '\'' + 
			"}";
		}
}