package com.sadhana.expensetracker.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sadhana.expensetracker.model.Budget;
import com.sadhana.expensetracker.repository.BudgetRepository;
import com.sadhana.expensetracker.repository.ExpenseRepository;

@RestController
@RequestMapping("/budget")
public class BudgetController {

    private final BudgetRepository budgetRepository;
    private final ExpenseRepository expenseRepository;

    public BudgetController(BudgetRepository budgetRepository, ExpenseRepository expenseRepository) {
        this.budgetRepository = budgetRepository;
        this.expenseRepository = expenseRepository;
    }

    @PostMapping
    public Budget setBudget(@RequestBody Budget budget) {
        return budgetRepository.save(budget);
    }

    @GetMapping
    public Budget getBudget() {
        return budgetRepository.findAll().stream().findFirst().orElse(null);
    }

    @GetMapping("/status")
    public String checkBudgetStatus() {

        Double total = expenseRepository.getTotalExpenses();

        Budget budget = budgetRepository.findAll().stream().findFirst().orElse(null);

        if (budget == null) {
            return "No budget set";
        }

        if (total != null && total > budget.getMonthlyBudget()) {
            return "⚠ Budget exceeded!";
        }

        return "Budget within limit";
    }
}