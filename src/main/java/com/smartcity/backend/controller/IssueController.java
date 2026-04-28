package com.smartcity.backend.controller;

import com.smartcity.backend.dto.common.ApiResponse;
import com.smartcity.backend.dto.common.PageResponse;
import com.smartcity.backend.dto.issue.IssueRequest;
import com.smartcity.backend.dto.issue.IssueResponse;
import com.smartcity.backend.service.IssueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/issues")
@Tag(name = "Issues", description = "Issue operations for citizens")
@SecurityRequirement(name = "bearerAuth")
public class IssueController {

    private final IssueService issueService;

    public IssueController(IssueService issueService) {
        this.issueService = issueService;
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Create a new issue")
    public ResponseEntity<ApiResponse<IssueResponse>> createIssue(
            @Valid @RequestBody IssueRequest request,
            Authentication authentication
    ) {
        IssueResponse response = issueService.createIssue(request, authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("success", "Issue reported successfully", response));
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get issues reported by the logged-in user")
    public ResponseEntity<ApiResponse<PageResponse<IssueResponse>>> getMyIssues(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PageResponse<IssueResponse> response = issueService.getMyIssues(authentication.getName(), page, size);
        return ResponseEntity.ok(new ApiResponse<>("success", "User issues fetched successfully", response));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Get a single issue by ID")
    public ResponseEntity<ApiResponse<IssueResponse>> getIssueById(
            @PathVariable Long id,
            Authentication authentication
    ) {
        boolean adminAccess = authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
        IssueResponse response = issueService.getIssueById(id, authentication.getName(), adminAccess);
        return ResponseEntity.ok(new ApiResponse<>("success", "Issue fetched successfully", response));
    }
}
