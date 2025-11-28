package com.etfinder.mvc.comment.dto;

import java.time.LocalDateTime;

public class CommentResponse {

    private String nickname;           // 작성자 닉네임
    private String content;            // 댓글 내용
    private String sentiment;          // 감정 분석 (선택)
    private LocalDateTime createdAt;   // 최초 작성 시간 (수정 안 했을 때만 사용)
    private LocalDateTime updatedAt;   // 수정 시간 (수정했을 때만 사용)
    private boolean edited;            // 수정 여부 (true면 "수정됨" 표시용)

    public CommentResponse() {}

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
}
