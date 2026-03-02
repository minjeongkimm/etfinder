package com.etfinder.mvc.comment.dto;

import java.time.LocalDateTime;

public class CommentResponse {

	private Long commentId;				// 댓글 id
    private String nickname;           // 작성자 닉네임
    private String content;            // 댓글 내용
    private String sentiment;          // 감정 분석 (선택)
    private LocalDateTime createdAt;   // 최초 작성 시간 (수정 안 했을 때만 사용)
    private LocalDateTime updatedAt;   // 수정 시간 (수정했을 때만 사용)
    private boolean edited;            // 수정 여부 (true면 "수정됨" 표시용)

    public CommentResponse() {}
    
    

    public CommentResponse(Long commentId, String nickname, String content, String sentiment, LocalDateTime createdAt,
			LocalDateTime updatedAt, boolean edited) {
		this.commentId = commentId;
		this.nickname = nickname;
		this.content = content;
		this.sentiment = sentiment;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.edited = edited;
	}



	public Long getCommentId() {
		return commentId;
	}



	public void setCommentId(Long commentId) {
		this.commentId = commentId;
	}



	public String getNickname() {
		return nickname;
	}



	public void setNickname(String nickname) {
		this.nickname = nickname;
	}



	public String getContent() {
		return content;
	}



	public void setContent(String content) {
		this.content = content;
	}



	public String getSentiment() {
		return sentiment;
	}



	public void setSentiment(String sentiment) {
		this.sentiment = sentiment;
	}



	public LocalDateTime getCreatedAt() {
		return createdAt;
	}



	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}



	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}



	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}



	public boolean isEdited() {
		return edited;
	}



	public void setEdited(boolean edited) {
		this.edited = edited;
	}



	@Override
	public String toString() {
		return "CommentResponse [commentId=" + commentId + ", nickname=" + nickname + ", content=" + content
				+ ", sentiment=" + sentiment + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + ", edited="
				+ edited + "]";
	}

    
    

    
    
}
