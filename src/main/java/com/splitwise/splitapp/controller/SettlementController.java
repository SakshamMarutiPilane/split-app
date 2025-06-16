package com.splitwise.splitapp.controller;

import com.splitwise.splitapp.service.SettlementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;
import java.util.List;

@RestController
public class SettlementController {

    @Autowired
    private SettlementService service;

    @GetMapping("/balances")
    public Map<String, Double> getBalances() {
        return service.getBalances();
    }

    @GetMapping("/settlements")
    public List<String> getSettlementSummary() {
        return service.getSettlementSummary();
    }

    @GetMapping("/people")
    public Set<String> getPeople() {
        return service.getAllPeople();
    }
}
