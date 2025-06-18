package main.project_1.service;


import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class GitService {
    @Value("${github.token:}")
    private String githubToken;

    public File cloneRepository(String gitUrl) throws IOException, GitAPIException {
        Path tempDir = Files.createTempDirectory("git-clone-");

        CredentialsProvider credentials = new UsernamePasswordCredentialsProvider(
                githubToken.isEmpty() ? "" : githubToken,
                ""
        );

        try (Git git = Git.cloneRepository()
                .setURI(gitUrl)
                .setCredentialsProvider(credentials)
                .setDirectory(tempDir.toFile())
                .call()) {
            // Используем try-with-resources, чтобы гарантировать закрытие
        }

        return tempDir.toFile();
    }

}
