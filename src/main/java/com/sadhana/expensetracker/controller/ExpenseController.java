package com.sadhana.expensetracker.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sadhana.expensetracker.dto.ApiResponse;
import com.sadhana.expensetracker.exception.ResourceNotFoundException;
import com.sadhana.expensetracker.model.Expense;
import com.sadhana.expensetracker.repository.ExpenseRepository;
@RestController
@RequestMapping("/expenses")
public class ExpenseController {
    private static final Logger logger = LoggerFactory.getLogger(ExpenseController.class);

    public static Logger getLogger() {
        return logger;
    }

    private final ExpenseRepository expenseRepository;

    public ExpenseController(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    @PostMapping
public ApiResponse<Expense> addExpense(@RequestBody Expense expense) {

    logger.info("Adding new expense: {}", expense.getTitle());

    Expense saved = expenseRepository.save(expense);

    logger.info("Expense saved with id: {}", saved.getId());

    return new ApiResponse<>("Expense added successfully", saved);
}
    @GetMapping
    public List<Expense> getAllExpenses() {
        return expenseRepository.findAll();
    }

    @DeleteMapping("/{id}")
    public void deleteExpense(@PathVariable Long id) {
        expenseRepository.deleteById(id);
    }
    @PutMapping("/{id}")
public Expense updateExpense(@PathVariable Long id, @RequestBody Expense updatedExpense) {

    Expense expense = expenseRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Expense not found with id: " + id));

    expense.setTitle(updatedExpense.getTitle());
    expense.setAmount(updatedExpense.getAmount());
    expense.setCategory(updatedExpense.getCategory());
    expense.setDate(updatedExpense.getDate());

    return expenseRepository.save(expense);
}
    @GetMapping("/total")
    public Double getTotalExpenses() {
        return expenseRepository.getTotalExpenses();
    }

    @GetMapping("/category-summary")
    public Map<String, Double> getCategorySummary() {

        List<Object[]> results = expenseRepository.getCategorySummary();

        Map<String, Double> summary = new HashMap<>();

        for (Object[] result : results) {
            summary.put((String) result[0], (Double) result[1]);
        }

        return summary;
    }

    @GetMapping("/monthly-summary")
    public Map<Integer, Double> getMonthlySummary() {

        List<Object[]> results = expenseRepository.getMonthlySummary();

        Map<Integer, Double> summary = new HashMap<>();

        for (Object[] result : results) {
            summary.put((Integer) result[0], (Double) result[1]);
        }

        return summary;
    }
    @GetMapping("/dashboard")
public Map<String, Object> getDashboard() {

    Map<String, Object> dashboard = new HashMap<>();

    Double totalExpenses = expenseRepository.getTotalExpenses();
    long count = expenseRepository.count();

    List<Object[]> topCategoryResult = expenseRepository.findTopCategory();

    String topCategory = "None";

    if (!topCategoryResult.isEmpty()) {
        topCategory = (String) topCategoryResult.get(0)[0];
    }

    dashboard.put("totalExpenses", totalExpenses);
    dashboard.put("totalTransactions", count);
    dashboard.put("topCategory", topCategory);

    return dashboard;
}
@GetMapping("/{id}")
public Expense getExpenseById(@PathVariable Long id) {

    return expenseRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Expense not found with id: " + id));
}
@GetMapping("/category/{category}")
public List<Expense> getExpensesByCategory(@PathVariable String category) {
    return expenseRepository.findByCategory(category);
}
@GetMapping("/date-range")
public List<Expense> getExpensesByDateRange(
        @RequestParam LocalDate start,
        @RequestParam LocalDate end) {

    return expenseRepository.findByDateBetween(start, end);
}
@GetMapping("/paged")
public Page<Expense> getExpensesPaged(
        @RequestParam int page,
        @RequestParam int size,
        @RequestParam(defaultValue = "id,asc") String[] sort) {

    Sort.Direction direction = sort[1].equalsIgnoreCase("desc")
            ? Sort.Direction.DESC
            : Sort.Direction.ASC;

    Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort[0]));

    return expenseRepository.findAll(pageable);
}
@GetMapping("/stats")
public Map<String, Double> getExpenseStats() {

    Map<String, Double> stats = new HashMap<>();

    stats.put("total", expenseRepository.getTotalAmount());
    stats.put("average", expenseRepository.getAverageAmount());
    stats.put("highest", expenseRepository.getHighestExpense());
    stats.put("lowest", expenseRepository.getLowestExpense());

    return stats;
}

@GetMapping("/export")
public ResponseEntity<String> exportExpenses() {

    List<Expense> expenses = expenseRepository.findAll();

    StringBuilder csv = new StringBuilder();
    csv.append("ID,Title,Amount,Category,Date\n");

    for (Expense e : expenses) {
        csv.append(e.getId()).append(",")
           .append(e.getTitle()).append(",")
           .append(e.getAmount()).append(",")
           .append(e.getCategory()).append(",")
           .append(e.getDate()).append("\n");
    }

    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Disposition", "attachment; filename=expenses.csv");

    return ResponseEntity.ok()
            .headers(headers)
            .contentType(MediaType.TEXT_PLAIN)
            .body(csv.toString());
}

@GetMapping("/search")
public ApiResponse<List<Expense>> searchExpenses(@RequestParam String keyword) {

    List<Expense> results = expenseRepository.searchExpenses(keyword);

    return new ApiResponse<>("Search results", results);
}

@GetMapping("/top")
public ApiResponse<List<Expense>> getTopExpenses() {

    List<Expense> topExpenses = expenseRepository.findTop5ByOrderByAmountDesc();

    return new ApiResponse<>("Top 5 highest expenses", topExpenses);
}
@GetMapping("/trend")
public ApiResponse<Map<String, Double>> getExpenseTrend() {

    List<Object[]> results = expenseRepository.getExpenseTrend();

    Map<String, Double> trend = new HashMap<>();

    for (Object[] row : results) {
        trend.put(row[0].toString(), (Double) row[1]);
    }

    return new ApiResponse<>("Expense trend data", trend);
}

@GetMapping("/category-percentage")
public ApiResponse<Map<String, Double>> getCategoryPercentage() {

    List<Object[]> results = expenseRepository.getCategoryTotals();

    Double totalExpenses = expenseRepository.getTotalExpenses();

    Map<String, Double> percentage = new HashMap<>();

    for (Object[] row : results) {

        String category = (String) row[0];
        Double amount = (Double) row[1];

        double percent = Math.round((amount / totalExpenses) * 100 * 100.0) / 100.0;

        percentage.put(category, percent);
    }

    return new ApiResponse<>("Category percentage", percentage);
}
}
