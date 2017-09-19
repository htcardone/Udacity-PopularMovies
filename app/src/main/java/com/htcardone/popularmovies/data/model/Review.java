package com.htcardone.popularmovies.data.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Review {

	@SerializedName("author")
	private String author;

	@SerializedName("id")
	private String id;

	@SerializedName("content")
	private String content;

	@SerializedName("url")
	private String url;

	public String getAuthor(){
		return author;
	}

	public String getId(){
		return id;
	}

	public String getContent(){
		return content;
	}

	public String getUrl(){
		return url;
	}

	@Override
 	public String toString(){
		return 
			"Review{" +
			"author = '" + author + '\'' + 
			",id = '" + id + '\'' + 
			",content = '" + content + '\'' + 
			",url = '" + url + '\'' + 
			"}";
		}
}