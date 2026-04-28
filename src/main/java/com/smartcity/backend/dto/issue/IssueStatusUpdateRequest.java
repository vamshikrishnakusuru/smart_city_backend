package com.smartcity.backend.dto.issue;

import com.smartcity.backend.enums.IssueStatus;
import jakarta.validation.constraints.NotNull;
public class IssueStatusUpdateRequest {

    @NotNull(message = "Status is required")
    private IssueStatus status;

    public IssueStatus getStatus() {
        return status;
    }

    public void setStatus(IssueStatus status) {
        this.status = status;
    }
}
