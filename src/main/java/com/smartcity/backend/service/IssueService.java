package com.smartcity.backend.service;

import com.smartcity.backend.dto.common.PageResponse;
import com.smartcity.backend.dto.issue.IssueRequest;
import com.smartcity.backend.dto.issue.IssueResponse;
import com.smartcity.backend.dto.issue.IssueStatusUpdateRequest;

public interface IssueService {

    IssueResponse createIssue(IssueRequest request, String userEmail);

    PageResponse<IssueResponse> getMyIssues(String userEmail, int page, int size);

    IssueResponse getIssueById(Long issueId, String userEmail, boolean adminAccess);

    PageResponse<IssueResponse> getAllIssues(int page, int size);

    PageResponse<IssueResponse> getIssuesByCity(String city, String adminEmail, int page, int size);

    IssueResponse updateIssueStatus(Long issueId, IssueStatusUpdateRequest request, String adminEmail);
}
