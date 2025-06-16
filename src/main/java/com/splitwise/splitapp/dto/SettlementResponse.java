package com.splitwise.splitapp.dto;

import java.util.List;

public class SettlementResponse {
    private List<String> settlements;

    public SettlementResponse() {}

    public SettlementResponse(List<String> settlements) {
        this.settlements = settlements;
    }

    public List<String> getSettlements() { return settlements; }
    public void setSettlements(List<String> settlements) { this.settlements = settlements; }
}
