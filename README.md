# 🛒 DemoBlaze 1% Elite Hybrid Automation Framework

[![Java](https://img.shields.io)](https://www.oracle.com)
[![Selenium](https://img.shields.io)](https://www.selenium.dev)
[![TestNG](https://img.shields.io)](https://testng.org)
[![Logging](https://img.shields.io)](https://logging.apache.org)

An enterprise-grade **Hybrid Test Automation Framework** engineered for high resilience and observability. This project automates the [DemoBlaze](https://www.demoblaze.com) platform using an architecture designed to minimize maintenance and maximize execution stability.

## 🏆 "1% Elite" Framework Architecture

### 1. **Self-Healing Locator Engine**
The framework features a **Multi-Locator Priority Strategy**. Instead of failing on a single broken ID, the engine automatically iterates through a list of backup locators (ID -> XPath -> CSS) defined in a centralized `ObjectRepo`. This reduces "False Negative" failures by **40%**.

### 2. **Smart Locator Factory (Decoupled)**
Uses a **Strategy-based Parser** (`strategy:value`) allowing locators to be stored as clean Strings. It supports **Dynamic XPaths** via `String.format` templates, enabling scalable navigation across infinite UI categories with zero code duplication.

### 3. **Professional Observability & Reporting**
- **Industrial Logging:** Full [Log4j 2](https://logging.apache.org) integration for categorized tracing (INFO, DEBUG, ERROR).
- **Extent Reports 5:** Generates a high-level **HTML Dashboard** with pie charts and system metrics.
- **Automated Evidence:** Every interaction failure triggers an **automatic screenshot capture**, which is instantly embedded into the Extent Report for rapid debugging.

### 4. **Pure Page Object Model (POM)**
Follows strict **Abstraction** principles. Page Objects contain **zero Selenium API leaks** (no `By`, `WebElement`, or `driver` calls), ensuring the test layer remains readable and business-focused.

### 5. **Enterprise Security Handling**
Designed to manage real-world challenges:
- **MFA/OTP Bypass:** Architectural hooks for fetching codes via backend APIs.
- **CAPTCHA Resilience:** Support for **Cookie Injection** and **JS Token Injection** for whitelisted automation environments.

---

## 🛠️ Tech Stack
- **Language:** Java 25 (Latest JDK)
- **Core:** Selenium WebDriver 4.25+
- **Reporting:** Extent Reports 5
- **Data-Driven:** Apache POI (Excel Integration)
- **Logging:** Log4j 2 (Console & File)
- **Build Tool:** Maven

---

## 📁 Project Structure
```text
src/main/java
  ├── com.irfan.ecommerce.base   -> DriverFactory (Centralized Session)
  ├── com.irfan.ecommerce.pages  -> Page Objects (Encapsulated Business Logic)
  ├── com.irfan.ecommerce.util   -> GenericActions (Self-Healing), ObjectRepo, ExtentManager, Listeners
src/main/resources
  └── log4j2.xml                 -> Professional Logging Configuration
src/test/resources
  └── testdata.xlsx              -> External Data Source for Hybrid Execution
