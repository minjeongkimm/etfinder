package com.etfinder.mvc.comment.dto;

public class CommentAiResponse {

	private String sentiment;
	
	public CommentAiResponse() {
	}

	public CommentAiResponse(String sentiment) {
		this.sentiment = sentiment;
	}

	public String getSentiment() {
		return sentiment;
	}

	public void setSentiment(String sentiment) {
		this.sentiment = sentiment;
	}

	@Override
	public String toString() {
		return "CommentAiResponse [sentiment=" + sentiment + "]";
	}
	
}
