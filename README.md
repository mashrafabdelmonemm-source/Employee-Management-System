# 👥 Employee Management System

### 📋 Overview
A comprehensive, desktop-based **Employee Management System** developed in Java. This application provides a full suite of administrative and employee dashboards to streamline workplace management, using JSON for local data storage.

### ✨ Key Features
*   **💻 Dual Dashboards:** Separate, customized graphical interfaces (GUI) for both Admins and Employees.
*   **📅 Attendance Tracking:** Automated logs to monitor and report employee clock-in/out records.
*   **💳 Payroll Management:** Built-in calculation and management system for salaries and payroll distribution.
*   **🚪 Leave Management:** Seamless submission, tracking, and approval workflow for employee leave requests.
*   **🏢 Department Configuration:** Easy tools to add, modify, and assign departments across the organization.

### 🛠️ Architecture & Technologies
*   **Language:** Java
*   **UI Framework:** Java Swing / AWT (GUI)
*   **Data Storage:** Local JSON files (`attendance.json`, `employees.json`, etc.)
*   **Libraries:** Google Gson (for efficient JSON parsing and serialization)

### 🚀 Setup and Run
1. Ensure you have the **Java Development Kit (JDK)** installed.
2. Include the provided `gson-2.10.1.jar` from the `lib/` directory in your project's classpath.
3. Run the application through the main class:
```bash
   java src/ui/Main.java
