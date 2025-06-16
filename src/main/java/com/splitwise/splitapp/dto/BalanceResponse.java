package com.splitwise.splitapp.dto;

import java.util.Map;

public class BalanceResponse {
    private Map<String, Double> balances;

    public BalanceResponse() {}

    public BalanceResponse(Map<String, Double> balances) {
        this.balances = balances;
    }

    public Map<String, Double> getBalances() { return balances; }
    public void setBalances(Map<String, Double> balances) { this.balances = balances; }
}