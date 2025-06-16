package com.splitwise.splitapp.service;

import com.splitwise.splitapp.model.Expense;
import com.splitwise.splitapp.model.SplitDetail;
import com.splitwise.splitapp.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SettlementService {

    @Autowired
    private ExpenseRepository repository;

    public Map<String, Double> getBalances() {
        Map<String, Double> balances = new HashMap<>();

        List<Expense> expenses = repository.findAll();

        for (Expense e : expenses) {
            balances.put(e.getPaidBy(), balances.getOrDefault(e.getPaidBy(), 0.0) + e.getAmount());

            if (e.getSplitType().equalsIgnoreCase("EQUAL")) {
                double share = e.getAmount() / e.getSplitDetails().size();
                for (SplitDetail s : e.getSplitDetails()) {
                    balances.put(s.getPerson(), balances.getOrDefault(s.getPerson(), 0.0) - share);
                }
            } else if (e.getSplitType().equalsIgnoreCase("EXACT")) {
                for (SplitDetail s : e.getSplitDetails()) {
                    balances.put(s.getPerson(), balances.getOrDefault(s.getPerson(), 0.0) - s.getValue());
                }
            } else if (e.getSplitType().equalsIgnoreCase("PERCENTAGE")) {
                for (SplitDetail s : e.getSplitDetails()) {
                    double share = e.getAmount() * s.getValue() / 100;
                    balances.put(s.getPerson(), balances.getOrDefault(s.getPerson(), 0.0) - share);
                }
            }
        }

        return balances;
    }

    public List<String> getSettlementSummary() {
        Map<String, Double> balances = getBalances();

        // Separate into debtors and creditors
        PriorityQueue<Map.Entry<String, Double>> debtors = new PriorityQueue<>(Map.Entry.comparingByValue());
        PriorityQueue<Map.Entry<String, Double>> creditors = new PriorityQueue<>((a, b) -> Double.compare(b.getValue(), a.getValue()));

        for (Map.Entry<String, Double> entry : balances.entrySet()) {
            double amt = Math.round(entry.getValue() * 100.0) / 100.0;
            if (amt < 0) debtors.add(Map.entry(entry.getKey(), -amt));
            else if (amt > 0) creditors.add(Map.entry(entry.getKey(), amt));
        }

        List<String> settlements = new ArrayList<>();

        while (!debtors.isEmpty() && !creditors.isEmpty()) {
            Map.Entry<String, Double> debtor = debtors.poll();
            Map.Entry<String, Double> creditor = creditors.poll();

            double amount = Math.min(debtor.getValue(), creditor.getValue());
            amount = Math.round(amount * 100.0) / 100.0;

            settlements.add(debtor.getKey() + " pays " + amount + " to " + creditor.getKey());

            if (debtor.getValue() > amount)
                debtors.add(Map.entry(debtor.getKey(), debtor.getValue() - amount));

            if (creditor.getValue() > amount)
                creditors.add(Map.entry(creditor.getKey(), creditor.getValue() - amount));
        }

        return settlements;
    }

    public Set<String> getAllPeople() {
        Set<String> people = new HashSet<>();

        for (Expense e : repository.findAll()) {
            people.add(e.getPaidBy());
            for (SplitDetail s : e.getSplitDetails()) {
                people.add(s.getPerson());
            }
        }

        return people;
    }
}
