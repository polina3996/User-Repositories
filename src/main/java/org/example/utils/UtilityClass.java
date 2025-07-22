package org.example.utils;

@lombok.experimental.UtilityClass
public class UtilityClass {
    public static final String REPOSITORIES_PATH = "https://api.github.com/users/{username}/repos";
    public static final String BRANCHES_PATH = "https://api.github.com//repos/{username}/{repository}/branches";
    public static final String EXCEPTION_MESSAGE = "GitHub user %s not found";
}