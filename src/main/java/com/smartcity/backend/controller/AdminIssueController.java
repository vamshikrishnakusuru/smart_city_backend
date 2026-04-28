package com.smartcity.backend.controller;

import com.smartcity.backend.dto.common.ApiResponse;
import com.smartcity.backend.dto.common.PageResponse;
import com.smartcity.backend.dto.issue.IssueResponse;
import com.smartcity.backend.dto.issue.IssueStatusUpdateRequest;
import com.smartcity.backend.service.IssueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/issues")
@Tag(name = "Admin Issues", description = "Admin issue management endpoints")
@SecurityRequirement(name = "bearerAuth")
public class AdminIssueController {

    private final IssueService issueService;

    public AdminIssueController(IssueService issueService) {
        this.issueService = issueService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all issues")
    public ResponseEntity<ApiResponse<PageResponse<IssueResponse>>> getAllIssues(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PageResponse<IssueResponse> response = issueService.getAllIssues(page, size);
        return ResponseEntity.ok(new ApiResponse<>("success", "All issues fetched successfully", response));
    }

    @GetMapping("/city/{city}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get issues for a specific city")
    public ResponseEntity<ApiResponse<PageResponse<IssueResponse>>> getIssuesByCity(
            @PathVariable String city,
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PageResponse<IssueResponse> response = issueService.getIssuesByCity(city, authentication.getName(), page, size);
        return ResponseEntity.ok(new ApiResponse<>("success", "City issues fetched successfully", response));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update issue status")
    public ResponseEntity<ApiResponse<IssueResponse>> updateIssueStatus(
            @PathVariable Long id,
            @Valid @RequestBody IssueStatusUpdateRequest request,
            Authentication authentication
    ) {
        IssueResponse response = issueService.updateIssueStatus(id, request, authentication.getName());
        return ResponseEntity.ok(new ApiResponse<>("success", "Issue status updated successfully", response));
    }
}
