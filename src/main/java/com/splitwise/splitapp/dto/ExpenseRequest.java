package com.splitwise.splitapp.dto;

import com.splitwise.splitapp.model.SplitDetail;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.List;

public class ExpenseRequest {

    @NotBlank(message = "Description cannot be empty")
    private String description;

    @Positive(message = "Amount must be greater than 0")
    private double amount;

    @NotBlank(message = "PaidBy cannot be empty")
    private String paidBy;

    @NotBlank(message = "Split type is required")
    private String splitType;

    @NotEmpty(message = "Split details are required")
    private List<@Valid SplitDetail> splitDetails;

    @NotBlank(message = "Category is required")
    private String category;

    @NotNull(message = "Date is required")
    private LocalDate date;

    private boolean recurring;

    private String recurrenceType;

    private LocalDate recurrenceEndDate;

    // Getters and Setters

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getPaidBy() { return paidBy; }
    public void setPaidBy(String paidBy) { this.paidBy = paidBy; }

    public String getSplitType() { return splitType; }
    public void setSplitType(String splitType) { this.splitType = splitType; }

    public List<SplitDetail> getSplitDetails() { return splitDetails; }
    public void setSplitDetails(List<SplitDetail> splitDetails) { this.splitDetails = splitDetails; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public boolean isRecurring() { return recurring; }
    public void setRecurring(boolean recurring) { this.recurring = recurring; }

    public String getRecurrenceType() { return recurrenceType; }
    public void setRecurrenceType(String recurrenceType) { this.recurrenceType = recurrenceType; }

    public LocalDate getRecurrenceEndDate() { return recurrenceEndDate; }
    public void setRecurrenceEndDate(LocalDate recurrenceEndDate) { this.recurrenceEndDate = recurrenceEndDate; }
}
