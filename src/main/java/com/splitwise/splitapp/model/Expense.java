package com.splitwise.splitapp.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
public class Expense {
	
	@Enumerated(EnumType.STRING)
	private ExpenseCategory category;

	@Column(nullable = false)
	private boolean recurring = false;

	private String recurrenceType; // e.g., DAILY, WEEKLY, MONTHLY

	private LocalDate recurrenceEndDate; // when the recurring stops

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    private double amount;

    private String paidBy;

    private String splitType; // EQUAL, EXACT, PERCENTAGE

    @Column(nullable = false)
    private LocalDate date = LocalDate.now();

    @ElementCollection
    @CollectionTable(name = "expense_split_details", joinColumns = @JoinColumn(name = "expense_id"))
    private List<SplitDetail> splitDetails;

    // Getters and Setters

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public double getAmount() { return amount; }

    public void setAmount(double amount) { this.amount = amount; }

    public String getPaidBy() { return paidBy; }

    public void setPaidBy(String paidBy) { this.paidBy = paidBy; }

    public String getSplitType() { return splitType; }

    public void setSplitType(String splitType) { this.splitType = splitType; }

    public LocalDate getDate() { return date; }

    public void setDate(LocalDate date) { this.date = date; }

    public List<SplitDetail> getSplitDetails() { return splitDetails; }

    public void setSplitDetails(List<SplitDetail> splitDetails) { this.splitDetails = splitDetails; }
    
    public boolean isRecurring() {
        return recurring;
    }

    public void setRecurring(boolean recurring) {
        this.recurring = recurring;
    }

    public String getRecurrenceType() {
        return recurrenceType;
    }

    public void setRecurrenceType(String recurrenceType) {
        this.recurrenceType = recurrenceType;
    }

    public LocalDate getRecurrenceEndDate() {
        return recurrenceEndDate;
    }

    public void setRecurrenceEndDate(LocalDate recurrenceEndDate) {
        this.recurrenceEndDate = recurrenceEndDate;
    }
    
 // getters and setters...
    public ExpenseCategory getCategory() {
        return category;
    }

    public void setCategory(ExpenseCategory category) {
        this.category = category;
    }

}
