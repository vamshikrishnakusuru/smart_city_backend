package com.smartcity.backend.repository;

import com.smartcity.backend.entity.Issue;
import com.smartcity.backend.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IssueRepository extends JpaRepository<Issue, Long> {

    @EntityGraph(attributePaths = {"user", "assignedAdmin"})
    Page<Issue> findAllByUser(User user, Pageable pageable);

    @EntityGraph(attributePaths = {"user", "assignedAdmin"})
    Page<Issue> findAllByCityIgnoreCase(String city, Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"user", "assignedAdmin"})
    Page<Issue> findAll(Pageable pageable);
}
