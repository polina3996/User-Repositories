# GitHub User Repositories Service

This Spring Boot project provides a REST API to fetch **non-forked GitHub repositories** and their **branches** for a given GitHub username.  
It integrates with GitHub's REST API and maps the response into custom DTOs.

---

## Technologies Used

- Java 17+
- Spring Boot
- Spring Web
- RestClient (Spring 6+ HTTP client)
- Lombok
- JUnit 5
- Maven

---

## Features

- Fetch all **non-forked** public repositories of a given GitHub user
- For each repository, fetch all **branches** with the **latest commit SHA**
- Custom error handling for absent ot non-existing users
- Integration test coverage

---

## API Endpoint

### `GET api/github/{username}/repositories`

**Path Variable:**
- `username` — GitHub username to fetch repositories for

**Response:**
Returns a list of repositories with their names, owner login, branches and last commit SHA.

```json
[
  {
    "repositoryName": "my-repo",
    "ownerLogin": "username",
    "branches": [
      {
        "branchName": "main",
        "lastCommitSha": "abc123..."
      }
    ]
  }
]
```

For non-existing user or if username is absent:

```json
{
    "status": 404,
    "message": "GitHub user not found"
}
```

## Tests

**mvn verify** or **mvn failsafe:integration-test failsafe:verify**