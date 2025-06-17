# 💸 Split App - Backend Assignment

## 📌 Problem Statement

Build a backend system that helps groups of people split expenses fairly and calculate who owes money to whom — similar to Splitwise or Google Pay Bill Split.  
Scenarios include roommates sharing rent, friends splitting meals, or trip expenses.

This system enables:
- Adding expenses with who paid and how it's split.
- Automatic balance and settlement calculations.
- Optionally: categories, recurring expenses, analytics, and a frontend.

---

## Core Features

### 1. Expense Management
- Add new expenses (amount, description, paid by, etc.)
- Automatically adds people based on expenses.
- View all expenses with full details.
- Edit and delete any expense.
- Choose split type: equal, exact, or percentage.

### 2. Settlement Calculations
- Calculate each person’s fair share and actual spending.
- Automatically compute who owes whom.
- Show optimized transactions to minimize payments.

### 3. Data Validation & Error Handling
- Input validation: required fields, positive amounts, etc.
- Graceful handling of empty/invalid inputs.
- Descriptive error messages and proper HTTP status codes.

---

## API Endpoints

### 📁 1. Expense Management

#### ✅ 1.1 Add Equal Expense
Endpoint: POST /expenses
Description: Add an expense where the amount is split equally among all participants.
Example Request:
```
{
    "category": "FOOD",
    "recurring": false,
    "recurrenceType": null,
    "recurrenceEndDate": null,
    "id": 1,
    "description": "Dinner at restaurant",
    "amount": 600.0,
    "paidBy": "Shantanu",
    "splitType": "EQUAL",
    "date": "2025-06-16",
    "splitDetails": [
        {
            "person": "Shantanu",
            "value": 200.0
        },
        {
            "person": "Sanket",
            "value": 200.0
        },
        {
            "person": "Om",
            "value": 200.0
        }
    ]
}
```
Expected Response:
```
{
    "category": "FOOD",
    "recurring": false,
    "recurrenceType": null,
    "recurrenceEndDate": null,
    "id": 1,
    "description": "Dinner at restaurant",
    "amount": 600.0,
    "paidBy": "Shantanu",
    "splitType": "EQUAL",
    "date": "2025-06-16",
    "splitDetails": [
        {
            "person": "Shantanu",
            "value": 200.0
        },
        {
            "person": "Sanket",
            "value": 200.0
        },
        {
            "person": "Om",
            "value": 200.0
        }
    ]
}
```

#### ✅ 1.2 Add Exact Expense
Endpoint: POST /expenses
Description: Add an expense where specific amounts are assigned to each participant.
Example Request:
```
{
  "amount": 300.00,
  "description": "Movie Tickets",
  "paidBy": "Sanket",
  "splitType": "EXACT",
  "splitDetails": [
    { "person": "Sanket", "value": 100 },
    { "person": "Om", "value": 100 },
    { "person": "Shantanu", "value": 100 }
  ],
  "date": "2025-06-16",
  "recurring": false,
  "recurrenceType": null,
  "recurrenceEndDate": null,
  "category": "ENTERTAINMENT"
}
```
Expected Response:
```
{
    "category": "ENTERTAINMENT",
    "recurring": false,
    "recurrenceType": null,
    "recurrenceEndDate": null,
    "id": 2,
    "description": "Movie Tickets",
    "amount": 300.0,
    "paidBy": "Sanket",
    "splitType": "EXACT",
    "date": "2025-06-16",
    "splitDetails": [
        {
            "person": "Sanket",
            "value": 100.0
        },
        {
            "person": "Om",
            "value": 100.0
        },
        {
            "person": "Shantanu",
            "value": 100.0
        }
    ]
}
```

#### ✅ 1.3 Add Percentage Expense
Endpoint: POST /expenses
Description: Add an expense where each participant pays a percentage of the total amount.
Example Request:
```
{
  "amount": 1000.00,
  "description": "Weekend Trip",
  "paidBy": "Om",
  "splitType": "PERCENTAGE",
  "splitDetails": [
    { "person": "Om", "value": 50 },
    { "person": "Shantanu", "value": 30 },
    { "person": "Sanket", "value": 20 }
  ],
  "date": "2025-06-16",
  "recurring": false,
  "recurrenceType": null,
  "recurrenceEndDate": null,
  "category": "TRAVEL"
}
```
Expected Response:
```
{
    "category": "TRAVEL",
    "recurring": false,
    "recurrenceType": null,
    "recurrenceEndDate": null,
    "id": 3,
    "description": "Weekend Trip",
    "amount": 1000.0,
    "paidBy": "Om",
    "splitType": "PERCENTAGE",
    "date": "2025-06-16",
    "splitDetails": [
        {
            "person": "Om",
            "value": 500.0
        },
        {
            "person": "Shantanu",
            "value": 300.0
        },
        {
            "person": "Sanket",
            "value": 200.0
        }
    ]
}
```

#### ✅ 1.4 List All Expenses
Endpoint: GET /expenses
Description: Retrieve a list of all recorded expenses with full details including split information.
Expected Response:
```
[
    {
        "category": "FOOD",
        "recurring": false,
        "recurrenceType": null,
        "recurrenceEndDate": null,
        "id": 1,
        "description": "Dinner at restaurant",
        "amount": 600.0,
        "paidBy": "Shantanu",
        "splitType": "EQUAL",
        "date": "2025-06-16",
        "splitDetails": [
            {
                "person": "Shantanu",
                "value": 200.0
            },
            {
                "person": "Sanket",
                "value": 200.0
            },
            {
                "person": "Om",
                "value": 200.0
            }
        ]
    },
    {
        "category": "ENTERTAINMENT",
        "recurring": false,
        "recurrenceType": null,
        "recurrenceEndDate": null,
        "id": 2,
        "description": "Movie Tickets",
        "amount": 300.0,
        "paidBy": "Sanket",
        "splitType": "EXACT",
        "date": "2025-06-16",
        "splitDetails": [
            {
                "person": "Sanket",
                "value": 100.0
            },
            {
                "person": "Om",
                "value": 100.0
            },
            {
                "person": "Shantanu",
                "value": 100.0
            }
        ]
    },
    {
        "category": "TRAVEL",
        "recurring": false,
        "recurrenceType": null,
        "recurrenceEndDate": null,
        "id": 3,
        "description": "Weekend Trip",
        "amount": 1000.0,
        "paidBy": "Om",
        "splitType": "PERCENTAGE",
        "date": "2025-06-16",
        "splitDetails": [
            {
                "person": "Om",
                "value": 500.0
            },
            {
                "person": "Shantanu",
                "value": 300.0
            },
            {
                "person": "Sanket",
                "value": 200.0
            }
        ]
    }
]
```

#### ✅ 1.5 Update an Expense
Endpoint: PUT /expenses/{id}
Description: Update an existing expense (amount, participants, type of split, etc).
Example Request:
```
{
  "id": 1,
  "amount": 450.00,
  "description": "Updated Dinner Expense",
  "paidBy": "Shantanu",
  "splitType": "EQUAL",
  "splitDetails": [
    { "person": "Shantanu", "value": 0 },
    { "person": "Sanket", "value": 0 },
    { "person": "Om", "value": 0 }
  ],
  "date": "2025-06-16",
  "recurring": false,
  "recurrenceType": null,
  "recurrenceEndDate": null,
  "category": "FOOD"
}
```
Expected Response:
```
{
    "category": "FOOD",
    "recurring": false,
    "recurrenceType": null,
    "recurrenceEndDate": null,
    "id": 1,
    "description": "Updated Dinner Expense",
    "amount": 450.0,
    "paidBy": "Shantanu",
    "splitType": "EQUAL",
    "date": "2025-06-16",
    "splitDetails": [
        {
            "person": "Shantanu",
            "value": 150.0
        },
        {
            "person": "Sanket",
            "value": 150.0
        },
        {
            "person": "Om",
            "value": 150.0
        }
    ]
}
```

#### ✅ 1.6 Delete an Expense
Endpoint: DELETE /expenses/{id}
Description: Delete an existing expense by ID.
Expected Response:
```
{
    "success": true,
    "message": "Expense deleted successfully."
}
```

### 📁 2. Settlements and People

#### ✅ 2.1 Get All People
Endpoint: GET /expenses/people
Description: Retrieve a list of all unique people involved in any expense.
Expected Response:
```
[
    "Shantanu",
    "Sanket",
    "Om"
]
```

#### ✅ 2.2 Get Balances
Endpoint: GET /expenses/balances
Description: Show each person’s current balance (positive = owed, negative = owes).
Expected Response:
```
{
    "balances": {
        "Shantanu": -400.0,
        "Sanket": 0.0,
        "Om": 400.0
    }
}
```

#### ✅ 2.3 Get Settlement Summary
Endpoint: GET /expenses/settlements
Description: Get simplified transactions that show who needs to pay whom and how much.
Expected Response:
```
[
    {
        "amount": 400.0,
        "from": "Shantanu",
        "to": "Om"
    }
]
```

### ⚠️ 3. Edge Cases & Validations

#### ⚠️ 3.1 Add Invalid Expense - Negative Amount
Endpoint: POST /expenses
Description: Test system behavior when amount is negative. Should return a validation error.
Example Request:
```
{
  "amount": -100,
  "description": "Invalid negative expense",
  "paidBy": "Shantanu",
  "splitType": "EQUAL",
  "splitDetails": [
    { "person": "Shantanu", "value": 0 },
    { "person": "Sanket", "value": 0 }
  ],
  "date": "2025-06-16",
  "recurring": false,
  "category": "OTHER"
}
```
Expected Response:
```
{
    "success": false,
    "message": "Amount must be greater than 0",
    "status": 400
}
```

#### ⚠️ 3.2 Add Invalid Expense - Empty Description
Endpoint: POST /expenses
Description: Test system behavior when description is missing or empty.
Example Request:
```
{
  "amount": 200,
  "description": "",
  "paidBy": "Om",
  "splitType": "EQUAL",
  "splitDetails": [
    { "person": "Om", "value": 0 },
    { "person": "Sanket", "value": 0 }
  ],
  "date": "2025-06-16",
  "recurring": false,
  "category": "FOOD"
}
```
Expected Response:
```
{
    "success": false,
    "message": "Description cannot be empty",
    "status": 400
}
```

#### ⚠️ 3.3 Add Invalid Expense - Missing PaidBy Field
Endpoint: POST /expenses
Description: Test system behavior when paidBy field is missing or null.
Example Request:
```
{
  "amount": 250,
  "description": "Snacks",
  "splitType": "EQUAL",
  "splitDetails": [
    { "person": "Shantanu", "value": 0 },
    { "person": "Om", "value": 0 }
  ],
  "date": "2025-06-16",
  "recurring": false,
  "category": "FOOD"
}
```
Expected Response:
```
{
    "success": false,
    "message": "PaidBy cannot be empty",
    "status": 400
}
```

#### ⚠️ 3.4 Split sum doesn’t match amount (for EXACT/PERCENTAGE)
Endpoint: PUT /expenses/{invalid-id}
Description: Split summing value entered by user doesnt match the amount.
Example Request:
```
{
  "amount": 500,
  "description": "Invalid Split",
  "paidBy": "Shantanu",
  "splitType": "EXACT",
  "splitDetails": [
    { "person": "Shantanu", "value": 200 },
    { "person": "Sanket", "value": 100 }
  ],
  "date": "2025-06-16",
  "recurring": false,
  "category": "TRAVEL"
}
```
Expected Response:
```
{
    "success": false,
    "message": "Sum of exact split values doesn't match total amount.",
    "status": 400
}
```

#### ⚠️ 3.5 Delete Non-Existent Expense
Endpoint: DELETE /expenses/{invalid-id}
Description: Try deleting a non-existent expense. Should return 500 error.
Expected Response:
```
{
    "success": false,
    "message": "Internal Server Error: Expense not found with id: 999",
    "status": 500
}
```

#### ⚠️ 3.6 Update Non-Existent Expense
Endpoint: GET /expenses/balances
Description: Try updating a non-existent expense. Should return 500 error.
Expected Response:
```
{
    "success": false,
    "message": "Internal Server Error: Expense not found with id: 999",
    "status": 500
}
```

### 🌟 4. Optional / Advanced Features

#### 🌟 4.1 Add Recurring Monthly Expense
Endpoint: POST /expenses
Description: Add a recurring expense like rent or subscriptions with recurring: true and type MONTHLY.
Example Request:
```
{
  "description": "Monthly Netflix",
  "amount": 500,
  "paidBy": "saksham",
  "splitType": "EQUAL",
  "splitDetails": [
    { "person": "saksham", "value": 0 },
    { "person": "friend1", "value": 0 }
  ],
  "date": "2025-06-16",
  "recurring": true,
  "recurrenceType": "MONTHLY",
  "recurrenceEndDate": "2025-09-16",
  "category": "ENTERTAINMENT"
}

```
Expected Response:
```
{
    "category": "ENTERTAINMENT",
    "recurring": true,
    "recurrenceType": "MONTHLY",
    "recurrenceEndDate": "2025-09-16",
    "id": 4,
    "description": "Monthly Netflix",
    "amount": 500.0,
    "paidBy": "saksham",
    "splitType": "EQUAL",
    "date": "2025-06-16",
    "splitDetails": [
        {
            "person": "saksham",
            "value": 250.0
        },
        {
            "person": "friend1",
            "value": 250.0
        }
    ]
}
```

#### 🌟 4.2 GET Monthly Expense by filter
Endpoint: POST /expenses
Description: Assign a category (e.g., FOOD, TRAVEL, ENTERTAINMENT) while adding an expense.
Example Request:
```
/expenses/summary/monthly?person={name}&month={Date}
```
Expected Response:
```
{
    "totalPaid": 0.0,
    "netBalance": 0.0,
    "totalOwed": 0.0
}
```

#### 🌟 4.3 Filter Expenses by Category (UI Only)

Note: This is a frontend feature. Filter visible expenses by category like FOOD, TRAVEL, etc.

<img width="598" alt="image" src="https://github.com/user-attachments/assets/9d511527-b97d-4bd4-8041-0ad0f38177f2" />
<img width="598" alt="image" src="https://github.com/user-attachments/assets/9d511527-b97d-4bd4-8041-0ad0f38177f2" />


#### 🌟 4.4 Export Expenses to PDF or CSV
Endpoint:
GET /expenses/export/pdf
GET /expenses/export/csv

Description: Export all current expenses in PDF or CSV format.

CSV:

<img width="1037" alt="image" src="https://github.com/user-attachments/assets/c302c6f7-7641-4f1c-aeb6-7592afc10722" />
<img width="1037" alt="image" src="https://github.com/user-attachments/assets/c302c6f7-7641-4f1c-aeb6-7592afc10722" />

PDF:

<img width="1035" alt="image" src="https://github.com/user-attachments/assets/c1c4fead-b461-4aec-ad3c-f818729fb463" />
<img width="1035" alt="image" src="https://github.com/user-attachments/assets/c1c4fead-b461-4aec-ad3c-f818729fb463" />

---

## 🌟 Optional Features (Implemented)

- Recurring Expenses (monthly/weekly)
- Expense Categories: FOOD, TRAVEL, UTILITIES, ENTERTAINMENT, OTHER
- Monthly Summaries by Person or Date
- Export to CSV & PDF
- Frontend UI (HTML + JS) for adding/viewing expenses (basic)

---

## 🚀 Deployment

**Deployed URL:** `https://split-app-1j4m.onrender.com`  
> APIs are publicly accessible, no local setup required.

---

## 🔧 Tech Stack

- **Backend**: Java 17 with Spring Boot
- **Database**: PostgreSQL (via Railway)
- **Deployment**: Render.com
- **Export**: iTextPDF, CSV via Servlet
- **Testing**: Postman (public collection)

---

## 📂 Postman Collection

- **Link to Public Gist:** [Split App Postman Collection](https://gist.github.com/SakshamMarutiPilane/06fbca33123a0b1d15ecca6026fec89a)
- Includes:
  - All core and optional APIs
  - Realistic test data (Shantanu, Sanket, Om)
  - Scenarios:
    - Equal/Exact/Percentage splits
    - Invalid inputs
    - Update/Delete cases
    - Monthly/category summaries

---

## 🧮Settlement Logic Explained
1. Track Total Paid by Each Person: For each expense, identify the person who paid (paidBy) and sum all such amounts for each individual.
2. Track Total Owed by Each Person: For each expense, use the splitDetails to determine how much each person owes (their fair share of that expense).
3. Calculate Net Balance: `netBalance = totalPaid - totalOwed;`
``If netBalance > 0 → the person is owed money (creditor).
If netBalance < 0 → the person owes money (debtor).``
4. Build Two Lists:
   a. creditors: People with positive balances.
   b. debtors: People with negative balances.
5. Optimize Settlement: Iterate through debtors and creditors.
6. For each pair: Determine the minimum of (abs(debtor’s balance), creditor’s balance).
7. Record a transaction: Debtor → Creditor: Amount
8. Update both balances.
9. Continue until all balances are zero.
Example:
`` "Sanket owes ₹200 to Om"
"Shantanu owes ₹100 to Om"
This means these two transactions alone are sufficient to settle all debts instead of everyone paying everyone.``

---

## ⚠️ Known Limitations & Assumptions

### 🔒 Assumptions:

1. Person Names are Unique & Case-Sensitive: "Shantanu" and "shantanu" are treated as two different individuals.
2. Split Details Are Always Valid: It is assumed that the frontend or API consumer will provide valid and matching split details (e.g., sum of percentages = 100%).
3. Equal Split Assumes Equal Division: For EQUAL splitType, the system equally divides the total amount among all listed persons in splitDetails.
4. All Dates are in ISO Format: Dates provided via API are assumed to be in YYYY-MM-DD format.
5. No Concurrency Handling: The app assumes that there is no simultaneous editing/deleting of expenses, as concurrency control (e.g., versioning or locks) is not implemented.
6. No Decimal Rounding Errors Handled Explicitly: Rounding is applied to two decimal places, but cumulative precision errors may still occur in rare edge cases.

### 🚫 Known Limitations:

1. No User Authentication: The application does not include login or authentication mechanisms; any user can access and modify data.
2. No Multi-Group Support: All expenses are considered to be within a single group; no group or trip-level separation is implemented.
3. Simplified Recurring Logic: Recurring expenses are stored with metadata (recurring, recurrenceType, recurrenceEndDate), but actual auto-generation of future instances is not yet automated.
4. No Currency Support: All expenses are assumed to be in INR (₹). There is no support for multi-currency transactions.
5. Limited Error Handling in Frontend: The basic HTML/JS frontend currently has minimal input validation. Unexpected or malformed input can lead to errors.
6. Manual Data Entry: There’s no person management UI — people are inferred only from expense input.
7. PDF/CSV Export is Static: The export feature captures current data as-is. It doesn’t support custom filters or grouped exports yet.

<img width="1003" alt="image" src="https://github.com/user-attachments/assets/80895a66-38e6-4011-93f1-08c170a17327" /><img width="1003" alt="image" src="https://github.com/user-attachments/assets/80895a66-38e6-4011-93f1-08c170a17327" />
