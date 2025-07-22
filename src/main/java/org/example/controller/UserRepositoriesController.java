package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.controller.dto.GithubRepositoryDto;
import org.example.service.UserRepositoriesService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/api/github")
public class UserRepositoriesController {

    private final UserRepositoriesService userRepositoriesService;

    @GetMapping("/{username}/repositories")
    public ResponseEntity<List<GithubRepositoryDto>> getUserRepositories(@PathVariable String username) {
        return ResponseEntity.ok(userRepositoriesService.getNonForkedRepositories(username));
    }
}