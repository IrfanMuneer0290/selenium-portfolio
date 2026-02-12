# 🛒 DemoBlaze Hybrid Automation Framework

A scalable, robust **Hybrid Test Automation Framework** built to demonstrate industry-standard practices in Web UI Automation. This project targets the [DemoBlaze](https://www.demoblaze.com) e-commerce platform using a maintainable and decoupled architecture.

## 🚀 Key Framework Features
- **Page Object Model (POM):** Enhances maintainability by separating UI elements from test logic.
- **Generic Action Wrappers:** Centralized interaction library using **Method Overloading** and **Explicit Waits** (`WebDriverWait`) to eliminate test flakiness.
- **Data-Driven Testing (DDT):** Integrated **Apache POI** to drive test execution via external `.xlsx` files.
- **Centralized Driver Factory:** Manages the browser lifecycle using a static `DriverFactory` for consistent session handling.
- **Dynamic Locators:** UI selectors are decoupled into a `webelement.properties` file for easy updates without recompiling code.

## 🛠️ Tech Stack
- **Language:** Java 25
- **Automation Tool:** [Selenium WebDriver 4.25+](https://www.selenium.dev)
- **Test Runner:** [TestNG](https://testng.org)
- **Build Tool:** [Apache Maven](https://maven.apache.org)
- **Excel Support:** [Apache POI](https://poi.apache.org)

## 📁 Project Structure
```text
src/main/java
  ├── com.irfan.ecommerce.base   -> DriverFactory, BaseTest setup
  ├── com.irfan.ecommerce.pages  -> Page Objects (Encapsulated UI logic)
  ├── com.irfan.ecommerce.util   -> ExcelUtil, GenericActions, Property Readers
src/test/resources
  └── testdata.xlsx              -> External test data for Hybrid execution
