package com.etfinder.mvc.comment.dto;

//응답용 dto
public class CommentResponse {
    
	private String nickname;
    private String content;
    private String sentiment;
    private String createdAt;   // 문자열로 보내기
    private String updatedAt;   // 문자열
    private boolean edited;     // 수정 여부
    
    public CommentResponse() {
	}

	public CommentResponse(String nickname, String content, String sentiment, String createdAt, String updatedAt,
			boolean edited) {
		this.nickname = nickname;
		this.content = content;
		this.sentiment = sentiment;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.edited = edited;
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

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(String updatedAt) {
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
		return "CommentResponse [nickname=" + nickname + ", content=" + content + ", sentiment=" + sentiment
				+ ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + ", edited=" + edited + "]";
	}
    
    
}
