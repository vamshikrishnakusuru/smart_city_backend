package com.smartcity.backend.dto.issue;

import com.smartcity.backend.dto.auth.UserSummaryResponse;
import com.smartcity.backend.enums.IssueStatus;
import java.time.LocalDateTime;
public class IssueResponse {

    private Long id;
    private String title;
    private String description;
    private String category;
    private IssueStatus status;
    private String city;
    private UserSummaryResponse user;
    private UserSummaryResponse assignedAdmin;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public IssueStatus getStatus() {
        return status;
    }

    public void setStatus(IssueStatus status) {
        this.status = status;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public UserSummaryResponse getUser() {
        return user;
    }

    public void setUser(UserSummaryResponse user) {
        this.user = user;
    }

    public UserSummaryResponse getAssignedAdmin() {
        return assignedAdmin;
    }

    public void setAssignedAdmin(UserSummaryResponse assignedAdmin) {
        this.assignedAdmin = assignedAdmin;
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
}
