package com.splitwise.splitapp.controller;

import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.splitwise.splitapp.dto.BalanceResponse;
import com.splitwise.splitapp.dto.ExpenseRequest;
import com.splitwise.splitapp.model.Expense;
import com.splitwise.splitapp.model.ExpenseCategory;
import com.splitwise.splitapp.service.ExpenseService;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/expenses")
public class ExpenseController {

    @Autowired
    private ExpenseService service;

    // GET /expenses with an optional category filter
    @GetMapping
    public List<Expense> getAll(@RequestParam(required = false) ExpenseCategory category) {
        if (category != null) {
            return service.getExpensesByCategory(category);
        }
        return service.getAllExpenses();
    }

    // POST /expenses: Create Expense from DTO
    @PostMapping
    public ResponseEntity<?> create(@RequestBody ExpenseRequest request) {
        Expense expense = new Expense();
        expense.setDescription(request.getDescription());
        expense.setAmount(request.getAmount());
        expense.setPaidBy(request.getPaidBy());
        expense.setSplitType(request.getSplitType());
        expense.setSplitDetails(request.getSplitDetails());
        expense.setDate(request.getDate());
        expense.setRecurring(request.isRecurring());
        expense.setRecurrenceType(request.getRecurrenceType());
        expense.setRecurrenceEndDate(request.getRecurrenceEndDate());
        
        // Map category from request to ExpenseCategory enum, defaulting to OTHER on error
        ExpenseCategory category;
        try {
            category = ExpenseCategory.valueOf(request.getCategory().toUpperCase());
        } catch (Exception e) {
            category = ExpenseCategory.OTHER;
        }
        expense.setCategory(category);

        return ResponseEntity.ok(service.addExpense(expense));
    }

    // PUT /expenses/{id}: Update Expense
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Expense expense) {
        Expense updated = service.updateExpense(id, expense);
        return (updated != null) ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    // DELETE /expenses/{id}: Delete Expense
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return service.deleteExpense(id) ? ResponseEntity.ok("Deleted") : ResponseEntity.notFound().build();
    }

    // GET /expenses/balances: Get balances for all users
    @GetMapping("/balances")
    public ResponseEntity<BalanceResponse> getBalances() {
        Map<String, Double> balances = service.calculateBalances();
        return ResponseEntity.ok(new BalanceResponse(balances));
    }

    // GET /expenses/settlements: Calculate settlements to clear debts
    @GetMapping("/settlements")
    public ResponseEntity<List<Map<String, Object>>> getSettlements() {
        List<Map<String, Object>> settlements = service.calculateSettlements();
        return ResponseEntity.ok(settlements);
    }

    // GET /expenses/summary/categories: Get total amounts for each category
    @GetMapping("/summary/categories")
    public Map<ExpenseCategory, Double> getCategorySummary() {
        List<Expense> all = service.getAllExpenses();
        return all.stream()
                .filter(e -> e.getCategory() != null)
                .collect(Collectors.groupingBy(
                        Expense::getCategory,
                        Collectors.summingDouble(Expense::getAmount)
                ));
    }

    // Phase 3: Monthly Summary API
    // GET /expenses/summary/monthly?person=saksham&month=2025-06
    @GetMapping("/summary/monthly")
    public ResponseEntity<Map<String, Double>> getMonthlySummary(
            @RequestParam String person,
            @RequestParam String month  // format: "YYYY-MM"
    ) {
        return ResponseEntity.ok(service.getMonthlySummary(person, month));
    }

    // Phase 4: Expense History by Person
    // GET /expenses/history?person=saksham
    @GetMapping("/history")
    public ResponseEntity<List<Expense>> getExpenseHistory(@RequestParam String person) {
        return ResponseEntity.ok(service.getExpenseHistoryByPerson(person));
    }
    
    @GetMapping("/export")
    public void exportExpenses(@RequestParam String format, HttpServletResponse response) throws IOException {
        List<Expense> expenses = service.getAllExpenses();

        if (format.equalsIgnoreCase("csv")) {
            response.setContentType("text/csv");
            response.setHeader("Content-Disposition", "attachment; filename=\"expenses.csv\"");

            PrintWriter writer = response.getWriter();
            writer.println("ID,Description,Amount,PaidBy,SplitType,Date,Recurring,RecurrenceType,RecurrenceEndDate,Category");

            for (Expense expense : expenses) {
                writer.printf("%d,%s,%.2f,%s,%s,%s,%b,%s,%s,%s\n",
                        expense.getId(),
                        expense.getDescription(),
                        expense.getAmount(),
                        expense.getPaidBy(),
                        expense.getSplitType(),
                        expense.getDate(),
                        expense.isRecurring(),
                        expense.getRecurrenceType(),
                        expense.getRecurrenceEndDate(),
                        expense.getCategory()
                );
            }

            writer.flush();

        } else if (format.equalsIgnoreCase("pdf")) {
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=\"expenses.pdf\"");

            PdfWriter pdfWriter = new PdfWriter(response.getOutputStream());
            PdfDocument pdfDoc = new PdfDocument(pdfWriter);
            Document document = new Document(pdfDoc);

            document.add(new Paragraph("Expense Report"));

            float[] columnWidths = {30F, 100F, 50F, 60F, 50F, 70F, 40F, 60F, 70F, 70F};
            Table table = new Table(columnWidths);
            table.addCell("ID");
            table.addCell("Description");
            table.addCell("Amount");
            table.addCell("PaidBy");
            table.addCell("SplitType");
            table.addCell("Date");
            table.addCell("Recurring");
            table.addCell("RecurrenceType");
            table.addCell("RecurrenceEndDate");
            table.addCell("Category");

            for (Expense expense : expenses) {
                table.addCell(String.valueOf(expense.getId()));
                table.addCell(expense.getDescription());
                table.addCell(String.format("%.2f", expense.getAmount()));
                table.addCell(expense.getPaidBy());
                table.addCell(expense.getSplitType());
                table.addCell(String.valueOf(expense.getDate()));
                table.addCell(String.valueOf(expense.isRecurring()));
                table.addCell(String.valueOf(expense.getRecurrenceType()));
                table.addCell(String.valueOf(expense.getRecurrenceEndDate()));
                table.addCell(String.valueOf(expense.getCategory()));
            }

            document.add(table);
            document.close();

        } else {
            throw new IllegalArgumentException("Unsupported export format: " + format);
        }
    }
}
