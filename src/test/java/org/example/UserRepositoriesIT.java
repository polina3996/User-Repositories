package org.example;

import org.example.controller.UserRepositoriesController;
import org.example.controller.dto.GithubRepositoryDto;
import org.example.restClient.RepositoryDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import java.util.List;

import static org.example.utils.UtilityClass.REPOSITORIES_PATH;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserRepositoriesIT {

    @Autowired
    private RestClient restClient;

    @Autowired
    private UserRepositoriesController userRepositoriesController;

    @Test
    void givenExistingUser_whenGetUserRepositories_thenRetrieveResponseWithRepositories() {
        // Given
        String username = "polina3996";

        //When
        ResponseEntity<List<GithubRepositoryDto>> response = userRepositoriesController.getUserRepositories(username);
        List<GithubRepositoryDto> repos = response.getBody();
        List<RepositoryDto> expectedRepositories = restClient.get()
                .uri(REPOSITORIES_PATH, username)
                .retrieve()
                .body(new ParameterizedTypeReference<List<RepositoryDto>>() {})
                .stream()
                .filter(repo -> !repo.isFork())
                .toList();

        //Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(repos);
        assertFalse(repos.isEmpty(), "There must be at least one repository");
        assertEquals(expectedRepositories.size(), repos.size(), "Response must contain only non-fork repositories");

        for (GithubRepositoryDto repo : repos) {
            assertNotNull(repo.getRepositoryName());
            assertNotNull(repo.getOwnerLogin());
            assertNotNull(repo.getBranches());
            assertFalse(repo.getBranches().isEmpty(), "Repository must contain branches");
            repo.getBranches().forEach(branch -> {
                assertNotNull(branch.getBranchName());
                assertNotNull(branch.getLastCommitSha());
                assertFalse(branch.getLastCommitSha().isEmpty());
            });
        }
    }
}