package main.project_1.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import main.project_1.dto.AnalysisResult;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnmappableCharacterException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CodeAnalysisService {

    private static final Charset[] CHARSETS = {StandardCharsets.UTF_8, StandardCharsets.ISO_8859_1, StandardCharsets.US_ASCII, Charset.forName("Windows-1251")};

    private static final Logger log = LoggerFactory.getLogger(CodeAnalysisService.class);

    private final GitService gitService;

    @Autowired
    public CodeAnalysisService(GitService gitService) {
        this.gitService = gitService;
    }

    private void validateGitUrl(String gitUrl) {
        if (!gitUrl.matches("^https?://.+\\..+/.+\\.git$")) {
            throw new IllegalArgumentException("Invalid git url: " + gitUrl);
        }
    }

    public AnalysisResult analyze(String gitUrl)
    {
        try {
            log.info("Начинаем анализ репозитория: {}", gitUrl);

            validateGitUrl(gitUrl);
            File repoDir = gitService.cloneRepository(gitUrl);
            log.debug("Репо клонирован в: {}", repoDir.getAbsolutePath());

            AnalysisResult result = new AnalysisResult();
            result.setRepoName(extractRepoName(gitUrl));
            try {
                analyzeFiles(repoDir, result);
            } finally {
                cleanupRepository(repoDir);
            }


            log.info("Анализ завершен успешно");
            return result;
        } catch (Exception e)
        {
            log.error("Analysis failed for URL: " + gitUrl, e);
            throw new RuntimeException("Failed to analyze repository",e);
        }
    }

    private String extractRepoName(String gitUrl) {
        return gitUrl.replaceAll("^https?://.+?/(.+)\\.git$", "$1");
    }


    private void analyzeFiles(File directory, AnalysisResult result) throws IOException {
        Map<String, Integer> languages = new HashMap<>();
        int totalFiles = 0;
        int totalLines = 0;

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory.toPath())) {
            for (Path path : stream) {
                if (Files.isDirectory(path)) {
                    analyzeFiles(path.toFile(), result);
                    continue;
                }

                if (Files.isRegularFile(path)) {
                    try {
                        String fileName = path.getFileName().toString();
                        String extension = getFileExtension(fileName);

                        if (extension != null && !isExcludedFile(fileName)) {
                            languages.merge(extension, 1, Integer::sum);
                            totalFiles++;

                            totalLines += countLinesInFile(path);
                        }
                    } catch (IOException e) {
                        log.error("Ошибка анализа файла: " + path, e);
                    }
                }
            }
        }

        result.setTotalFiles(result.getTotalFiles() + totalFiles);
        result.setTotalLines(result.getTotalLines() + totalLines);
        result.setLangDistribution(Stream.of(result.getLangDistribution(), languages)
                .flatMap(map -> map.entrySet().stream())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        Integer::sum
                )));
    }

    private int countLinesInFile(Path path) throws IOException {
        int lines = 0;

        for (Charset charset : CHARSETS) {
            try (BufferedReader reader = Files.newBufferedReader(path, charset)) {
                while (reader.readLine() != null) {
                    lines++;
                }
                return lines;
            } catch (MalformedInputException | UnmappableCharacterException e) {
                lines = 0;
                continue;
            }
        }

        log.warn("Не удалось прочитать файл (неподдерживаемая кодировка): {}", path);
        return 0;
    }

    private boolean isExcludedFile(String fileName) {
        return fileName.endsWith(".gitignore") ||
                fileName.endsWith(".md") ||
                fileName.startsWith(".");
    }

    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? null : fileName.substring(dotIndex + 1).toLowerCase();
    }

    private void cleanupRepository(File directory) {
        try {
            Files.walk(directory.toPath())
                    .sorted(Comparator.reverseOrder())
                    .forEach(path -> {
                        try {
                            Files.delete(path);
                        } catch (IOException e) {
                            log.warn("Не удалось удалить файл: {}", path, e);
                        }
                    });
            log.debug("Временные файлы репозитория удалены");
        } catch (IOException e) {
            log.error("Ошибка при очистке временных файлов", e);
        }
    }
}
