package com.smartcity.backend.service.impl;

import com.smartcity.backend.dto.auth.UserSummaryResponse;
import com.smartcity.backend.dto.common.PageResponse;
import com.smartcity.backend.dto.issue.IssueRequest;
import com.smartcity.backend.dto.issue.IssueResponse;
import com.smartcity.backend.dto.issue.IssueStatusUpdateRequest;
import com.smartcity.backend.entity.Issue;
import com.smartcity.backend.entity.User;
import com.smartcity.backend.enums.IssueStatus;
import com.smartcity.backend.enums.Role;
import com.smartcity.backend.exception.ResourceNotFoundException;
import com.smartcity.backend.repository.IssueRepository;
import com.smartcity.backend.repository.UserRepository;
import com.smartcity.backend.service.IssueService;
import java.util.Comparator;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class IssueServiceImpl implements IssueService {

    private final IssueRepository issueRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public IssueServiceImpl(IssueRepository issueRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.issueRepository = issueRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public IssueResponse createIssue(IssueRequest request, String userEmail) {
        User user = getUserByEmail(userEmail);

        Issue issue = modelMapper.map(request, Issue.class);
        issue.setStatus(IssueStatus.PENDING);
        issue.setCity(request.getCity().trim());
        issue.setUser(user);
        issue.setAssignedAdmin(resolveAdminForCity(issue.getCity()));

        return toIssueResponse(issueRepository.save(issue));
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<IssueResponse> getMyIssues(String userEmail, int page, int size) {
        User user = getUserByEmail(userEmail);
        Page<IssueResponse> result = issueRepository.findAllByUser(user, buildPageable(page, size)).map(this::toIssueResponse);
        return toPageResponse(result);
    }

    @Override
    @Transactional(readOnly = true)
    public IssueResponse getIssueById(Long issueId, String userEmail, boolean adminAccess) {
        User requester = getUserByEmail(userEmail);
        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new ResourceNotFoundException("Issue not found with id: " + issueId));

        if (!adminAccess && !issue.getUser().getId().equals(requester.getId())) {
            throw new AccessDeniedException("You are not allowed to view this issue");
        }

        if (adminAccess && requester.getRole() == Role.ADMIN && !requester.getCity().equalsIgnoreCase(issue.getCity())) {
            throw new AccessDeniedException("You are not allowed to view issues outside your city");
        }

        return toIssueResponse(issue);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<IssueResponse> getAllIssues(int page, int size) {
        Page<IssueResponse> result = issueRepository.findAll(buildPageable(page, size)).map(this::toIssueResponse);
        return toPageResponse(result);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<IssueResponse> getIssuesByCity(String city, String adminEmail, int page, int size) {
        User admin = getUserByEmail(adminEmail);
        validateAdminCityAccess(admin, city);
        Page<IssueResponse> result = issueRepository.findAllByCityIgnoreCase(city.trim(), buildPageable(page, size))
                .map(this::toIssueResponse);
        return toPageResponse(result);
    }

    @Override
    @Transactional
    public IssueResponse updateIssueStatus(Long issueId, IssueStatusUpdateRequest request, String adminEmail) {
        User admin = getUserByEmail(adminEmail);
        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new ResourceNotFoundException("Issue not found with id: " + issueId));

        validateAdminCityAccess(admin, issue.getCity());
        issue.setStatus(request.getStatus());
        if (issue.getAssignedAdmin() == null) {
            issue.setAssignedAdmin(admin);
        }

        return toIssueResponse(issueRepository.save(issue));
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }

    private void validateAdminCityAccess(User admin, String city) {
        if (admin.getRole() != Role.ADMIN) {
            throw new AccessDeniedException("Admin access required");
        }
        if (!admin.getCity().equalsIgnoreCase(city.trim())) {
            throw new AccessDeniedException("You are not allowed to manage issues outside your city");
        }
    }

    private User resolveAdminForCity(String city) {
        List<User> admins = userRepository.findByRoleAndCityIgnoreCase(Role.ADMIN, city.trim());
        return admins.stream().min(Comparator.comparing(User::getId)).orElse(null);
    }

    private Pageable buildPageable(int page, int size) {
        return PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    private PageResponse<IssueResponse> toPageResponse(Page<IssueResponse> page) {
        return new PageResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }

    private IssueResponse toIssueResponse(Issue issue) {
        IssueResponse response = modelMapper.map(issue, IssueResponse.class);
        response.setUser(modelMapper.map(issue.getUser(), UserSummaryResponse.class));
        if (issue.getAssignedAdmin() != null) {
            response.setAssignedAdmin(modelMapper.map(issue.getAssignedAdmin(), UserSummaryResponse.class));
        }
        return response;
    }
}
