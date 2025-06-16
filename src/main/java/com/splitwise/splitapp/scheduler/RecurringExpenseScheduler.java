package com.splitwise.splitapp.scheduler;

import com.splitwise.splitapp.model.Expense;
import com.splitwise.splitapp.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class RecurringExpenseScheduler {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Scheduled(fixedRate = 10000)
    public void generateRecurringExpenses() {
        List<Expense> recurringExpenses = expenseRepository.findAll()
                .stream()
                .filter(exp -> exp.isRecurring() && exp.getDate().isBefore(LocalDate.now())
                        && exp.getRecurrenceEndDate() != null
                        && exp.getRecurrenceEndDate().isAfter(LocalDate.now()))
                .toList();

        for (Expense original : recurringExpenses) {
            LocalDate nextDate = getNextDate(original.getDate(), original.getRecurrenceType());

            if (nextDate != null && nextDate.equals(LocalDate.now())) {
                Expense newExpense = new Expense();
                newExpense.setDescription(original.getDescription());
                newExpense.setAmount(original.getAmount());
                newExpense.setPaidBy(original.getPaidBy());
                newExpense.setSplitType(original.getSplitType());
                newExpense.setSplitDetails(original.getSplitDetails());
                newExpense.setDate(LocalDate.now());

                // Copy recurring info
                newExpense.setRecurring(true);
                newExpense.setRecurrenceType(original.getRecurrenceType());
                newExpense.setRecurrenceEndDate(original.getRecurrenceEndDate());

                expenseRepository.save(newExpense);
            }
        }
    }

    private LocalDate getNextDate(LocalDate date, String recurrenceType) {
        return switch (recurrenceType.toUpperCase()) {
            case "DAILY" -> date.plusDays(1);
            case "WEEKLY" -> date.plusWeeks(1);
            case "MONTHLY" -> date.plusMonths(1);
            default -> null;
        };
    }
}
