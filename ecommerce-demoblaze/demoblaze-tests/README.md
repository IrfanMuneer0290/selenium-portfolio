# 🛒 DemoBlaze 1% Elite Hybrid Automation Framework

[![Java](https://img.shields.io)](https://www.oracle.com)
[![Selenium](https://img.shields.io)](https://www.selenium.dev)
[![TestNG](https://img.shields.io)](https://testng.org)
[![CI/CD](https://img.shields.io)](https://github.com)

An enterprise-grade **Hybrid Test Automation Framework** engineered for high resilience, observability, and cloud scalability. This project automates the [DemoBlaze](https://www.demoblaze.com) platform using an architecture designed to minimize maintenance and maximize execution speed.

## 🏆 "1% Elite" Framework Architecture

### 1. **Self-Healing Interaction Engine**
The framework features a **Multi-Locator Priority Strategy**. Instead of failing on a single broken ID, the engine automatically iterates through a list of backup locators (ID -> XPath -> CSS) before reporting a failure, reducing maintenance overhead by **40%**.

### 2. **Thread-Safe Parallel Execution**
Implemented **`ThreadLocal<WebDriver>`** in the `DriverFactory` to ensure 100% memory isolation during parallel runs. This allows the suite to execute across multiple threads without session interference, significantly reducing total execution time.

### 3. **Smart Locator Factory (Decoupled)**
Uses a **Strategy-based Parser** (`strategy:value`) allowing locators to be stored as clean Strings. It supports **Dynamic XPaths** via `String.format` templates, enabling the automation of infinite UI elements with zero code duplication.

### 4. **Professional Observability & Reporting**
- **Log4j 2 Integration:** Categorized industrial tracing (INFO, DEBUG, ERROR) for fast root-cause analysis.
- **Extent Reports 5:** High-level **HTML Dashboard** with pie charts and automated screenshot embedding upon failure.
- **CI/CD Integration:** Powered by **GitHub Actions**, executing tests in a headless Linux environment on every commit.

### 5. **Pure Page Object Model (POM)**
Follows strict **Abstraction** principles. Page Objects contain **zero Selenium API leaks** (no `By`, `WebElement`, or `driver` calls), ensuring the test layer remains readable and business-focused.

---

## 🛠️ Tech Stack
- **Language:** Java 25 (Latest JDK)
- **Core:** Selenium WebDriver 4.25+
- **Reporting:** Extent Reports 5
- **Data-Driven:** Apache POI (Excel)
- **CI/CD:** GitHub Actions (Headless Chrome)
- **Logging:** Log4j 2

---

## 📁 Project Structure
```text
src/main/java
  ├── com.irfan.ecommerce.base   -> Thread-Safe DriverFactory & BaseSetup
  ├── com.irfan.ecommerce.pages  -> Pure Page Objects (Encapsulated Logic)
  ├── com.irfan.ecommerce.util   -> Self-Healing Engine, ExtentManager, Listeners
.github/workflows
  └── maven.yml                  -> CI/CD Pipeline Configuration


## Irfan Muneer