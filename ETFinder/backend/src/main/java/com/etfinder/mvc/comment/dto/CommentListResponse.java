package com.etfinder.mvc.comment.dto;

import java.util.List;

public class CommentListResponse {

	private List<CommentResponse> comments;
	private int positivePercent;
	private int negativePercent;
	private long totalCnt;
	private String moodMessage;
	
	public CommentListResponse() {
	}
	
	public CommentListResponse(List<CommentResponse> comments, int positivePercent, int negativePercent, long totalCnt,
			String moodMessage) {
		this.comments = comments;
		this.positivePercent = positivePercent;
		this.negativePercent = negativePercent;
		this.totalCnt = totalCnt;
		this.moodMessage = moodMessage;
	}

	public List<CommentResponse> getComments() {
		return comments;
	}

	public void setComments(List<CommentResponse> comments) {
		this.comments = comments;
	}

	public int getPositivePercent() {
		return positivePercent;
	}

	public void setPositivePercent(int positivePercent) {
		this.positivePercent = positivePercent;
	}

	public int getNegativePercent() {
		return negativePercent;
	}

	public void setNegativePercent(int negativePercent) {
		this.negativePercent = negativePercent;
	}

	public long getTotalCnt() {
		return totalCnt;
	}

	public void setTotalCnt(long totalCnt) {
		this.totalCnt = totalCnt;
	}

	public String getMoodMessage() {
		return moodMessage;
	}

	public void setMoodMessage(String moodMessage) {
		this.moodMessage = moodMessage;
	}

	@Override
	public String toString() {
		return "CommentListResponse [comments=" + comments + ", positivePercent=" + positivePercent
				+ ", negativePercent=" + negativePercent + ", totalCnt=" + totalCnt + ", moodMessage=" + moodMessage
				+ "]";
	}
	
	
}
