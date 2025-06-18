package project_1.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import project_1.dto.AnalysisResult;
import project_1.service.CodeAnalysisService;

@Controller
public class AnalyzerController {

    private final CodeAnalysisService codeAnalysisService;

    @Autowired
    public AnalyzerController(final CodeAnalysisService codeAnalysisService) {
        this.codeAnalysisService = codeAnalysisService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/analyze")
    public String analyzeRepository(@RequestParam String gitUrl, Model model) {
        try {
            AnalysisResult result = codeAnalysisService.analyze(gitUrl);
            if (result == null)
                throw new RuntimeException("Could not analyze git");
            model.addAttribute("result", result);
            return "results";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }

}
