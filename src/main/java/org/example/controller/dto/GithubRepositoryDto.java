package org.example.controller.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class GithubRepositoryDto {
    private String repositoryName;
    private String ownerLogin;
    private List<GithubBranchDto> branches;
}
