package com.sadhana.expensetracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sadhana.expensetracker.model.Budget;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
}