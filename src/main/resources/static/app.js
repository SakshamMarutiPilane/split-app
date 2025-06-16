document.addEventListener("DOMContentLoaded", function () {
  const form = document.getElementById("expenseForm");
  const recurringCheckbox = document.getElementById("recurring");
  const recurringFields = document.getElementById("recurringFields");
  const splitDetailsContainer = document.getElementById("splitDetailsContainer");
  const splitTypeSelect = document.getElementById("splitType");
  const addPersonBtn = document.getElementById("addPersonBtn");

  // Show/hide recurring fields
  recurringCheckbox.addEventListener("change", () => {
    recurringFields.style.display = recurringCheckbox.checked ? "block" : "none";
  });

  // Add person input block
  addPersonBtn.addEventListener("click", () => {
    addPersonField("", 0);
  });

  // Adjust value input fields when split type changes
  splitTypeSelect.addEventListener("change", () => {
    updateAllSplitFields();
  });

  // Add person input row
  function addPersonField(name = "", value = 0) {
    const container = document.createElement("div");
    container.classList.add("person-entry");

    const splitType = splitTypeSelect.value;

    container.innerHTML = `
      <label>Person Name:
        <input type="text" class="person-name" value="${name}" required />
      </label>
      <label>Value:
        <input type="${splitType === 'EQUAL' ? 'hidden' : 'number'}"
               class="person-value"
               value="${value}"
               step="0.01"
               ${splitType === 'EQUAL' ? 'readonly' : ''} />
      </label>
      <button type="button" class="removeBtn">Remove</button>
    `;

    container.querySelector(".removeBtn").addEventListener("click", () => {
      container.remove();
    });

    splitDetailsContainer.appendChild(container);
  }

  // Update all value inputs on split type change
  function updateAllSplitFields() {
    const entries = document.querySelectorAll(".person-entry");
    const splitType = splitTypeSelect.value;

    entries.forEach(entry => {
      const valueInput = entry.querySelector(".person-value");

      if (splitType === "EQUAL") {
        valueInput.type = "hidden";
        valueInput.readOnly = true;
        valueInput.value = 0;
      } else {
        valueInput.type = "number";
        valueInput.readOnly = false;
      }
    });
  }

  // Submit form logic
  form.addEventListener("submit", function (e) {
    e.preventDefault();

    const formData = new FormData(form);
    const data = {
      description: formData.get("description"),
      amount: parseFloat(formData.get("amount")),
      paidBy: formData.get("paidBy"),
      splitType: formData.get("splitType"),
      splitDetails: [],
      date: formData.get("date"),
      recurring: recurringCheckbox.checked,
      recurrenceType: formData.get("recurrenceType") || null,
      recurrenceEndDate: formData.get("recurrenceEndDate") || null,
      category: formData.get("category"),
    };

    // Collect split details
    const entries = document.querySelectorAll(".person-entry");
    entries.forEach(entry => {
      const person = entry.querySelector(".person-name").value;
      const value = parseFloat(entry.querySelector(".person-value").value) || 0;
      if (person.trim() !== "") {
        data.splitDetails.push({ person, value });
      }
    });

    if (data.splitDetails.length === 0) {
      alert("Please add at least one person in the split!");
      return;
    }

    // Send data to backend
    fetch("/expenses", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(data)
    })
      .then(res => {
        if (!res.ok) throw new Error("Server error");
        return res.json();
      })
      .then(response => {
        document.getElementById("responseBox").innerText = "✅ Expense added successfully!";
        form.reset();
        splitDetailsContainer.innerHTML = "";
        recurringFields.style.display = "none";
        addPersonField();
        addPersonField();
      })
      .catch(error => {
        document.getElementById("responseBox").innerText = "❌ Error adding expense!";
        console.error("Submission error:", error);
      });
  });

  // Add two people by default on page load
  addPersonField();
  addPersonField();
});
