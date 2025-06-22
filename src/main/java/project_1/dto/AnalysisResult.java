package project_1.dto;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class AnalysisResult {
    private String repoName;
    private int totalFiles;
    private int totalLines;
    private Map<String, Integer> langDistribution;
    private List<FileDetail> filesDetails;

    public AnalysisResult() {
        this.langDistribution = new HashMap<>();
        this.filesDetails = new ArrayList<>();
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

    public List<FileDetail> getFilesDetails() {
        return filesDetails;
    }

    public void setFilesDetails(List<FileDetail> filesDetails) {
        this.filesDetails = filesDetails;
    }

    @Override
    public String toString() {
        return "AnalysisResult{" +
                "repoName='" + repoName + '\'' +
                ", totalFiles=" + totalFiles +
                ", totalLines=" + totalLines +
                ", langDistribution=" + langDistribution +
                ", filesDetails=" + filesDetails +
                '}';
    }
}