package com.example.demo.web;

import com.example.demo.ChartDataItem;
import com.example.demo.HomeService;
import com.example.demo.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.domain.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController {

	@Autowired
	private HomeService homeService;

	@Autowired
	private PdfService pdfService;

	@RequestMapping("/")
	public String home(Model model) {
		model.addAttribute("formData", new User());
		return "index";
	}

	@GetMapping("/export-pdf")
	public ResponseEntity<byte[]> exportPdf() {
		// Create data (same as in ChartController)
		Map<String, Number> pieData = new HashMap<>();
		pieData.put("Product A", 25);
		pieData.put("Product B", 40);
		pieData.put("Product C", 35);

		List<ChartDataItem> barData = new ArrayList<>();
		barData.add(new ChartDataItem("Q1", "2023", 120));
		barData.add(new ChartDataItem("Q2", "2023", 140));
		barData.add(new ChartDataItem("Q3", "2023", 135));
		barData.add(new ChartDataItem("Q4", "2023", 180));
		barData.add(new ChartDataItem("Q1", "2024", 125));
		barData.add(new ChartDataItem("Q2", "2024", 150));

		// Generate charts
		String pieChartBase64 = homeService.generatePieChartAsBase64("Product Distribution", pieData);
		String barChartBase64 = homeService.generateBarChartAsBase64(
				"Quarterly Sales Comparison", "Quarter", "Sales (thousands)", barData);

		// Prepare model for template
		Map<String, Object> model = new HashMap<>();
		model.put("pieChartImage", pieChartBase64);
		model.put("barChartImage", barChartBase64);
		model.put("reportTitle", "Sales Performance Report");
		model.put("timestamp", java.time.LocalDateTime.now().toString());

		// Generate PDF
		byte[] pdfBytes = pdfService.generatePdfFromTemplate("charts", model);

		// Set response headers
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_PDF);
		headers.setContentDispositionFormData("filename", "charts-report.pdf");

		return ResponseEntity
				.ok()
				.headers(headers)
				.body(pdfBytes);
	}


	@RequestMapping("/create-pdf")
	public String createPdf(Model model) throws IOException {
		this.homeService.createPdf();
		// Create pie chart data
		Map<String, Number> pieData = new HashMap<>();
		pieData.put("Product A", 25);
		pieData.put("Product B", 40);
		pieData.put("Product C", 35);

		// Create bar chart data
		List<ChartDataItem> barData = new ArrayList<>();
		barData.add(new ChartDataItem("Q1", "2023", 120));
		barData.add(new ChartDataItem("Q2", "2023", 140));
		barData.add(new ChartDataItem("Q3", "2023", 135));
		barData.add(new ChartDataItem("Q4", "2023", 180));
		barData.add(new ChartDataItem("Q1", "2024", 125));
		barData.add(new ChartDataItem("Q2", "2024", 150));

		// Generate charts as Base64 strings
		String pieChartBase64 = homeService.generatePieChartAsBase64("Product Distribution", pieData);
		String barChartBase64 = homeService.generateBarChartAsBase64(
				"Quarterly Sales Comparison", "Quarter", "Sales (thousands)", barData);

		// Add data to model
		model.addAttribute("pieChartImage", pieChartBase64);
		model.addAttribute("barChartImage", barChartBase64);
		model.addAttribute("reportTitle", "Sales Performance Report");
		model.addAttribute("timestamp", java.time.LocalDateTime.now().toString());

		return "charts";
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String processFormData(User user, RedirectAttributes attr) {
		attr.addFlashAttribute("user", user);
		return "redirect:/display";
	}
	
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	public String displayFormData(User user) {
		return "result";
	}
}
