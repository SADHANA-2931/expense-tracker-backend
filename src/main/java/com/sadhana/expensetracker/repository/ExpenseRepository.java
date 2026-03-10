package com.sadhana.expensetracker.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sadhana.expensetracker.model.Expense;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    @Query("SELECT SUM(e.amount) FROM Expense e")
    Double getTotalExpenses();

    @Query("SELECT e.category, SUM(e.amount) FROM Expense e GROUP BY e.category")
    List<Object[]> getCategorySummary();

    @Query("SELECT MONTH(e.date), SUM(e.amount) FROM Expense e GROUP BY MONTH(e.date)")
    List<Object[]> getMonthlySummary();

    @Query("SELECT e.category, SUM(e.amount) as total FROM Expense e GROUP BY e.category ORDER BY total DESC")
    List<Object[]> findTopCategory();

    List<Expense> findByCategory(String category);
    List<Expense> findByDateBetween(LocalDate start, LocalDate end);

    @Query("SELECT SUM(e.amount) FROM Expense e")
Double getTotalAmount();

@Query("SELECT AVG(e.amount) FROM Expense e")
Double getAverageAmount();

@Query("SELECT MAX(e.amount) FROM Expense e")
Double getHighestExpense();

@Query("SELECT MIN(e.amount) FROM Expense e")
Double getLowestExpense();

@Query("SELECT e FROM Expense e WHERE LOWER(e.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(e.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
List<Expense> searchExpenses(@Param("keyword") String keyword);

List<Expense> findTop5ByOrderByAmountDesc();

@Query("SELECT e.date, SUM(e.amount) FROM Expense e GROUP BY e.date ORDER BY e.date")
List<Object[]> getExpenseTrend();

@Query("SELECT e.category, SUM(e.amount) FROM Expense e GROUP BY e.category")
List<Object[]> getCategoryTotals();
}