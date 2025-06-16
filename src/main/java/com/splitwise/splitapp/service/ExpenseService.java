package com.splitwise.splitapp.service;

import com.splitwise.splitapp.exception.ResourceNotFoundException;
import com.splitwise.splitapp.model.Expense;
import com.splitwise.splitapp.model.ExpenseCategory;
import com.splitwise.splitapp.model.SplitDetail;
import com.splitwise.splitapp.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    public List<Expense> getAllExpenses() {
        return expenseRepository.findAll();
    }

    public Expense addExpense(Expense expense) {
        calculateSplitAmounts(expense);
        return expenseRepository.save(expense);
    }

    public Expense updateExpense(Long id, Expense updatedExpense) {
        Expense existingExpense = expenseRepository.findById(id)
        		.orElseThrow(() -> new ResourceNotFoundException("Expense not found with id: " + id));

        existingExpense.setAmount(updatedExpense.getAmount());
        existingExpense.setDescription(updatedExpense.getDescription());
        existingExpense.setPaidBy(updatedExpense.getPaidBy());
        existingExpense.setSplitType(updatedExpense.getSplitType());
        existingExpense.setSplitDetails(updatedExpense.getSplitDetails());
        existingExpense.setDate(updatedExpense.getDate());
        existingExpense.setRecurring(updatedExpense.isRecurring());
        existingExpense.setRecurrenceType(updatedExpense.getRecurrenceType());
        existingExpense.setRecurrenceEndDate(updatedExpense.getRecurrenceEndDate());

        // ✅ Add this line
        existingExpense.setCategory(updatedExpense.getCategory());

        calculateSplitAmounts(existingExpense);
        return expenseRepository.save(existingExpense);
    }


    private void calculateSplitAmounts(Expense expense) {
        List<SplitDetail> splits = expense.getSplitDetails();
        double totalAmount = expense.getAmount();

        switch (expense.getSplitType().toUpperCase()) {
            case "EQUAL":
                double equalShare = totalAmount / splits.size();
                for (SplitDetail split : splits) {
                    split.setValue(equalShare);
                }
                break;

            case "EXACT":
                double exactSum = 0;
                for (SplitDetail split : splits) {
                    exactSum += split.getValue();
                }
                if (Math.abs(exactSum - totalAmount) > 0.01) {
                    throw new IllegalArgumentException("Sum of exact split values doesn't match total amount.");
                }
                break;

            case "PERCENTAGE":
                double percentSum = 0;
                for (SplitDetail split : splits) {
                    percentSum += split.getValue();
                }
                if (Math.abs(percentSum - 100.0) > 0.01) {
                    throw new IllegalArgumentException("Sum of percentage values must be 100.");
                }
                for (SplitDetail split : splits) {
                    double calculatedAmount = (split.getValue() / 100.0) * totalAmount;
                    split.setValue(calculatedAmount);
                }
                break;

            default:
                throw new IllegalArgumentException("Invalid split type: " + expense.getSplitType());
        }

        expense.setSplitDetails(splits);
    }
    
    public Map<String, Double> calculateBalances() {
        List<Expense> expenses = expenseRepository.findAll();
        Map<String, Double> balanceMap = new HashMap<>();

        for (Expense expense : expenses) {
            String payer = expense.getPaidBy();
            double totalAmount = expense.getAmount();

            // Payer paid the full amount
            balanceMap.put(payer, balanceMap.getOrDefault(payer, 0.0) + totalAmount);

            // Each person owes part of the amount
            for (SplitDetail split : expense.getSplitDetails()) {
                String person = split.getPerson();
                double owed = split.getValue();
                balanceMap.put(person, balanceMap.getOrDefault(person, 0.0) - owed);
            }
        }

        return balanceMap;
    }

    public List<Map<String, Object>> calculateSettlements() {
        Map<String, Double> balances = calculateBalances();

        PriorityQueue<Map.Entry<String, Double>> debtors = new PriorityQueue<>(
            (a, b) -> Double.compare(a.getValue(), b.getValue()));
        PriorityQueue<Map.Entry<String, Double>> creditors = new PriorityQueue<>(
            (a, b) -> Double.compare(b.getValue(), a.getValue()));

        for (Map.Entry<String, Double> entry : balances.entrySet()) {
            if (entry.getValue() < 0) {
                debtors.add(entry);
            } else if (entry.getValue() > 0) {
                creditors.add(entry);
            }
        }

        List<Map<String, Object>> settlements = new ArrayList<>();

        while (!debtors.isEmpty() && !creditors.isEmpty()) {
            Map.Entry<String, Double> debtor = debtors.poll();
            Map.Entry<String, Double> creditor = creditors.poll();

            double amount = Math.min(-debtor.getValue(), creditor.getValue());

            Map<String, Object> transaction = new HashMap<>();
            transaction.put("from", debtor.getKey());
            transaction.put("to", creditor.getKey());
            transaction.put("amount", Math.round(amount * 100.0) / 100.0);
            settlements.add(transaction);

            double debtorRemaining = debtor.getValue() + amount;
            double creditorRemaining = creditor.getValue() - amount;

            if (debtorRemaining != 0) {
                debtors.add(Map.entry(debtor.getKey(), debtorRemaining));
            }
            if (creditorRemaining != 0) {
                creditors.add(Map.entry(creditor.getKey(), creditorRemaining));
            }
        }

        return settlements;
    }
    
    public List<Expense> getExpensesByCategory(ExpenseCategory category) {
        return expenseRepository.findByCategory(category);
    }
    
    public boolean deleteExpense(Long id) {
        if (expenseRepository.existsById(id)) {
            expenseRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    public Map<String, Double> getMonthlySummary(String person, String month) {
        List<Expense> allExpenses = expenseRepository.findAll();

        double totalPaid = 0.0;
        double totalOwed = 0.0;

        for (Expense expense : allExpenses) {
            if (!expense.getDate().toString().startsWith(month)) continue;

            if (expense.getPaidBy().equalsIgnoreCase(person)) {
                totalPaid += expense.getAmount();
            }

            for (SplitDetail split : expense.getSplitDetails()) {
                if (split.getPerson().equalsIgnoreCase(person)) {
                    totalOwed += split.getValue();
                }
            }
        }

        Map<String, Double> summary = new HashMap<>();
        summary.put("totalPaid", totalPaid);
        summary.put("totalOwed", totalOwed);
        summary.put("netBalance", Math.round((totalPaid - totalOwed) * 100.0) / 100.0);
        return summary;
    }
    
    public List<Expense> getExpenseHistoryByPerson(String person) {
        List<Expense> allExpenses = expenseRepository.findAll();

        return allExpenses.stream()
                .filter(expense -> expense.getSplitDetails().stream()
                        .anyMatch(split -> split.getPerson().equalsIgnoreCase(person)))
                .collect(Collectors.toList());
    }
}
