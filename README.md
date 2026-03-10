
# 💰 Expense Tracker Backend

A RESTful backend application built using **Spring Boot and MySQL** that helps users track and analyze their expenses.  
This project provides APIs for managing expenses, setting budgets, and generating insights such as category summaries and monthly reports.

The APIs can be tested easily using **Postman**.

---

# 🚀 Tech Stack

- Java 17
- Spring Boot
- Spring Data JPA
- MySQL
- Maven
- REST APIs
- Postman (API testing)

---

# 📌 Features

### Expense Management
- Add new expense
- View all expenses
- Update expense
- Delete expense
- Search expenses

### Budget Management
- Set monthly budget
- Check budget status
- Alert when expenses exceed budget

### Expense Analytics
- Total expenses
- Category summary
- Category percentage
- Monthly expense summary
- Dashboard overview

### Backend Enhancements
- Global exception handling
- Standard API response format
- Clean layered architecture

---

# 📂 Project Structure

```

src/main/java/com/sadhana/expensetracker

auth
config
controller
dto
exception
model
repository
user

```

Explanation:

controller → REST APIs  
model → Database entities  
repository → JPA database operations  
dto → API response structure  
exception → Global error handling  

---

# ⚙️ Setup Instructions

## 1️⃣ Clone the repository

```

git clone [https://github.com/SADHANA-2931/expense-tracker-backend.git](https://github.com/SADHANA-2931/expense-tracker-backend.git)

cd expense-tracker-backend

```

---

## 2️⃣ Create MySQL Database

Open **MySQL Workbench** and run:

```

CREATE DATABASE expense_tracker;

```

---

## 3️⃣ Configure Database Connection

Open:

```

src/main/resources/application.properties

```

Update username and password if required:

```

spring.datasource.url=jdbc:mysql://localhost:3306/expense_tracker
spring.datasource.username=root
spring.datasource.password=${DB_PASSWORD}

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

```

---

## 4️⃣ Set Environment Variable

In PowerShell:

```

$env:DB_PASSWORD="your_mysql_password"

```

---

## 5️⃣ Run the Application

```

mvn spring-boot:run

```

Server will start at:

```

[http://localhost:8080](http://localhost:8080)

```

---

# 📡 API Endpoints (Test using Postman)

Base URL:

```

[http://localhost:8080](http://localhost:8080)

```

---

# 🧾 Expense APIs

## 1️⃣ Add Expense

POST /expenses

Body (JSON)

```

{
"title": "Lunch",
"amount": 200,
"category": "Food",
"description": "Lunch at restaurant",
"date": "2026-03-10"
}

```

Response

```

{
"message": "Expense added successfully",
"data": {
"id": 1,
"title": "Lunch",
"amount": 200
}
}

```

---

## 2️⃣ Get All Expenses

GET /expenses

Response

```

[
{
"id": 1,
"title": "Lunch",
"amount": 200,
"category": "Food",
"description": "Lunch at restaurant",
"date": "2026-03-10"
}
]

```

---

## 3️⃣ Get Expense by ID

GET /expenses/{id}

Example

```

GET /expenses/1

```

---

## 4️⃣ Update Expense

PUT /expenses/{id}

Body

```

{
"title": "Dinner",
"amount": 300,
"category": "Food",
"description": "Dinner with friends",
"date": "2026-03-10"
}

```

---

## 5️⃣ Delete Expense

DELETE /expenses/{id}

Example

```

DELETE /expenses/1

```

---

# 📊 Analytics APIs

## 1️⃣ Total Expenses

GET /expenses/total

Response

```

1500.0

```

---

## 2️⃣ Category Summary

Shows total spent per category.

GET /expenses/category-summary

Response

```

{
"Food": 1200,
"Fashion": 300
}

```

---

## 3️⃣ Category Percentage

Shows percentage spending by category.

GET /expenses/category-percentage

Response

```

{
"Food": 71.42,
"Fashion": 28.57
}

```

---

## 4️⃣ Monthly Expense Summary

GET /expenses/monthly-summary

Response

```

{
"2": 500,
"3": 1500,
"7": 500
}

```

Numbers represent **month number**.

---

## 5️⃣ Dashboard API

Provides overall summary.

GET /expenses/dashboard

Response

```

{
"totalExpenses": 1500,
"totalTransactions": 5,
"topCategory": "Food"
}

```

---

# 💰 Budget APIs

## 1️⃣ Set Budget

POST /budget

Body

```

{
"monthlyBudget": 5000
}

```

---

## 2️⃣ Check Budget Status

GET /budget/status

Response if exceeded

```

⚠ Budget exceeded!

```

Response if within limit

```

Budget within limit

```

---

# 🧪 Testing with Postman

Steps:

1️⃣ Open Postman  
2️⃣ Create new request  
3️⃣ Select method (GET / POST / PUT / DELETE)  
4️⃣ Enter URL  
5️⃣ For POST/PUT select **Body → raw → JSON**  
6️⃣ Send request  

---

# 📌 Future Improvements

Possible future enhancements:

- React frontend dashboard
- JWT authentication
- Expense charts and graphs
- Email notifications
- Docker deployment
- User accounts and login system

