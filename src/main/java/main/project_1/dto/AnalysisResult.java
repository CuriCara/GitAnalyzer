package main.project_1.dto;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AnalysisResult {
    private String repoName;
    private int totalFiles;
    private int totalLines;
    private Map<String, Integer> langDistribution;

    public AnalysisResult() {
        langDistribution = new HashMap<>();
    }

    public String getRepoName() {
        return repoName;
    }

    public void setRepoName(String repoName) {
        this.repoName = repoName;
    }

    public int getTotalFiles() {
        return totalFiles;
    }

    public void setTotalFiles(int totalFiles) {
        this.totalFiles = totalFiles;
    }

    public int getTotalLines() {
        return totalLines;
    }

    public void setTotalLines(int totalLines) {
        this.totalLines = totalLines;
    }


    public Map<String, Integer> getLangDistribution() {
        return langDistribution;
    }

    public void setLangDistribution(Map<String, Integer> langDistribution) {
        this.langDistribution = langDistribution;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnalysisResult that = (AnalysisResult) o;
        return totalFiles == that.totalFiles &&
                totalLines == that.totalLines &&
                Objects.equals(repoName, that.repoName) &&
                Objects.equals(langDistribution, that.langDistribution);
    }

    @Override
    public int hashCode() {
        return Objects.hash(repoName, totalFiles, totalLines, langDistribution);
    }

    @Override
    public String toString() {
        return "AnalyzeResult{" +
                "repoName = '" + repoName +'\'' +
                ", totalFiles = " + totalFiles +
                ", totalLanes = " + totalLines +
                ", langDistribution = " + langDistribution +
                '}';
    }
}

