package main.project_1.service;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.springframework.stereotype.Service;
import main.project_1.dto.AnalysisResult;

import java.awt.image.BufferedImage;

@Service
public class VisualizationService {

    public BufferedImage createLangChart(AnalysisResult analysisResult) {
        DefaultPieDataset dataset = new DefaultPieDataset();

        analysisResult.getLangDistribution().
                forEach((lang, count) -> {
                    dataset.setValue(lang, count);
                });
        JFreeChart chart = ChartFactory.createPieChart(
                "Распределение языков программирования",
                dataset,
                true, true, false
        );

        return chart.createBufferedImage(600, 400);
    }
}
