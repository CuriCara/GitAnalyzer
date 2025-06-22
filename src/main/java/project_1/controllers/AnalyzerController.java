package project_1.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import project_1.dto.AnalysisResult;
import project_1.service.CodeAnalysisService;

import java.util.HashMap;
import java.util.Map;

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

    @PostMapping("/api/analyze")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> analyzeRepositoryApi(@RequestParam String gitUrl) {
        Map<String, Object> response = new HashMap<>();
        try {
            AnalysisResult result = codeAnalysisService.analyze(gitUrl);
            if (result == null) {
                response.put("error", "Не удалось проанализировать репозиторий");
                return ResponseEntity.badRequest().body(response);
            }
            response.put("result", result);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}