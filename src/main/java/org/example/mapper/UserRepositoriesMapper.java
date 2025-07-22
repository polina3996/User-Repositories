package org.example.mapper;

import org.example.controller.dto.GithubBranchDto;
import org.example.controller.dto.GithubRepositoryDto;
import org.example.restClient.OwnerDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserRepositoriesMapper {

    @Mapping(source = "repositoryName", target = "repositoryName")
    @Mapping(source = "ownerDto.login", target = "ownerLogin")
    @Mapping(source = "branches", target = "branches")
    GithubRepositoryDto toRepositoriesDto(String repositoryName, OwnerDto ownerDto, List<GithubBranchDto> branches);
}