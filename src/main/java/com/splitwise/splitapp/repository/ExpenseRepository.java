package com.splitwise.splitapp.repository;

import com.splitwise.splitapp.model.Expense;
import com.splitwise.splitapp.model.ExpenseCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByCategory(ExpenseCategory category);
}
