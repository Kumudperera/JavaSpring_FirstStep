package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

@Service
public class HomeService {

    @Autowired
    private SpringTemplateEngine templateEngine;

    public void createPdf() throws IOException {
        this.generatePdfFromHtml(parseThymeleafTemplate());
    }

    private String parseThymeleafTemplate() {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);

        /*TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);*/

        Context context = new Context();
        context.setVariable("graphPageTitle", "Baeldung");

        return templateEngine.process("chart", context);
    }

    public void generatePdfFromHtml(String html) throws IOException {
        String outputFolder = System.getProperty("user.home") + File.separator + "thymeleaf.pdf";
        System.out.println(outputFolder);
        OutputStream outputStream = Files.newOutputStream(Paths.get(outputFolder));

        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(html);
        renderer.layout();
        renderer.createPDF(outputStream);

        outputStream.close();
    }

    /**
     * Generates a pie chart and returns it as a Base64 encoded string
     *
     * @param title Chart title
     * @param data Map of labels and values
     * @return Base64 encoded image string
     */
    public String generatePieChartAsBase64(String title, Map<String, Number> data) {
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();

        // Add data to dataset
        for (Map.Entry<String, Number> entry : data.entrySet()) {
            dataset.setValue(entry.getKey(), entry.getValue());
        }

        // Create the chart
        JFreeChart chart = ChartFactory.createPieChart(
                title,      // chart title
                dataset,    // data
                true,       // include legend
                true,       // tooltips
                false       // URLs
        );

        // Customize chart
        PiePlot<String> plot = (PiePlot<String>) chart.getPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setOutlineVisible(false);
        chart.setBackgroundPaint(Color.WHITE);

        // Convert to Base64
        return convertChartToBase64(chart, 600, 400);
    }

    /**
     * Generates a bar chart and returns it as a Base64 encoded string
     *
     * @param title Chart title
     * @param categoryAxisLabel Category axis label
     * @param valueAxisLabel Value axis label
     * @param data List of category/series/value mappings
     * @return Base64 encoded image string
     */
    public String generateBarChartAsBase64(String title, String categoryAxisLabel,
                                           String valueAxisLabel, List<ChartDataItem> data) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Add data to dataset
        for (ChartDataItem item : data) {
            dataset.addValue(item.getValue(), item.getSeries(), item.getCategory());
        }

        // Create chart
        JFreeChart chart = ChartFactory.createBarChart(
                title,              // chart title
                categoryAxisLabel,  // domain axis label
                valueAxisLabel,     // range axis label
                dataset,            // data
                PlotOrientation.VERTICAL, // orientation
                true,               // include legend
                true,               // tooltips
                false               // URLs
        );

        // Customize chart
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
        plot.setOutlineVisible(false);

        // Customize axes
        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setLowerMargin(0.02);
        domainAxis.setUpperMargin(0.02);

        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        // Customize renderer
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDefaultItemLabelGenerator(new org.jfree.chart.labels.StandardCategoryItemLabelGenerator());
        renderer.setDefaultItemLabelsVisible(true);

        chart.setBackgroundPaint(Color.WHITE);

        // Convert to Base64
        return convertChartToBase64(chart, 600, 400);
    }

    /**
     * Converts JFreeChart to Base64 encoded string
     *
     * @param chart The JFreeChart object
     * @param width Width of the chart image
     * @param height Height of the chart image
     * @return Base64 encoded string
     */
    private String convertChartToBase64(JFreeChart chart, int width, int height) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ChartUtils.writeChartAsPNG(outputStream, chart, width, height);
            return Base64.getEncoder().encodeToString(outputStream.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Error generating chart", e);
        }
    }

}
