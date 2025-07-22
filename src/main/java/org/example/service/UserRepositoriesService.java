package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.controller.dto.GithubBranchDto;
import org.example.controller.dto.GithubRepositoryDto;
import org.example.exception.handler.UserNotFoundException;
import org.example.restClient.BranchDto;
import org.example.restClient.RepositoryDto;
import org.example.mapper.UserRepositoriesMapper;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;

import static org.example.utils.UtilityClass.BRANCHES_PATH;
import static org.example.utils.UtilityClass.EXCEPTION_MESSAGE;
import static org.example.utils.UtilityClass.REPOSITORIES_PATH;

@Service
@RequiredArgsConstructor
public class UserRepositoriesService {

    private final RestClient restClient;
    private final UserRepositoriesMapper userRepositoriesMapper;

    public List<GithubRepositoryDto> getNonForkedRepositories(String username) {
        checkUsernamePresence(username);
        List<GithubRepositoryDto> result = new ArrayList<>();
        List<RepositoryDto> notForkedRepositories = retrieveNotForkedRepositories(username);
        for (RepositoryDto repositoryDto: notForkedRepositories){
            List<GithubBranchDto> branchesOfRepository = retrieveBranchesOfOneRepository(username, repositoryDto);
            GithubRepositoryDto mappedRepository = userRepositoriesMapper.toRepositoriesDto(repositoryDto.getName(), repositoryDto.getOwner(), branchesOfRepository);
            result.add(mappedRepository);
        }
        return result;
    }

    private void checkUsernamePresence(String username){
        if (username == null || username.isBlank()) {
            throw new UserNotFoundException("Username must be provided");
        }
    }

    private List<RepositoryDto> retrieveNotForkedRepositories(String username){
        return restClient.get()
                .uri(REPOSITORIES_PATH, username)
                .retrieve()
                .onStatus(HttpStatus.NOT_FOUND::equals, (request, response) -> {
                    throw new UserNotFoundException(String.format(EXCEPTION_MESSAGE, username));
                })
                .body(new ParameterizedTypeReference<List<RepositoryDto>>() {})
                .stream()
                .filter(repo -> !repo.isFork())
                .toList();
    }

    private List<GithubBranchDto> retrieveBranchesOfOneRepository(String username, RepositoryDto repositoryDto){
        return restClient.get()
                .uri(BRANCHES_PATH, username, repositoryDto.getName())
                .retrieve()
                .onStatus(HttpStatus.NOT_FOUND::equals, (request, response) -> {
                    throw new UserNotFoundException(String.format(EXCEPTION_MESSAGE, username));
                })
                .body(new ParameterizedTypeReference<List<BranchDto>>() {})
                .stream()
                .map(branch -> GithubBranchDto.builder()
                        .branchName(branch.getName())
                        .lastCommitSha(branch.getCommit() != null ? branch.getCommit().getSha() : null)
                        .build()
                )
                .toList();
    }
}