Actually, no. Because we implemented the **Production-Ready** stack in `docker-compose.prod.yaml`, the migration happens **automatically** when you run `make prod-up`. 

In our `Dockerfile`, the `ENTRYPOINT` is set to run the Java JAR. In the `prod-up` flow:
1. Docker builds the app.
2. Docker starts MySQL.
3. Once MySQL is healthy, Docker starts the App container.
4. The App container runs the migration and then exits.

However, for a "Golden Standard" repo, we should provide the command to **verify** that the data actually made it into the database after the migration runs. 

I’ve added a **"4. Verification"** section to the `README.md` below, which includes the command to check the database records.

---

### README.md (Final Validated Version)

```markdown
# JavaApps: Contact Migrator 🚀

A production-grade Java migration utility designed to orchestrate data transfer from CSV sources to MySQL destinations. This project has been refactored from legacy procedural scripts into a modern **Domain-Driven Design (DDD)** architecture, featuring a resilient **Test Pyramid** and containerized infrastructure.

---

## 🛠 1. Local Setup
Before you begin, ensure your environment has the following pinned versions:
* **JDK**: 11.0.30+
* **Maven**: 3.8+
* **Docker**: 29.4.0+

### Step 1: Clone the Repository
```bash
git clone [https://github.com/surya-hadoop/JavaApps.git](https://github.com/surya-hadoop/JavaApps.git)
cd JavaApps
git checkout dev
```

### Step 2: Verify Your Environment
Ensure your local tools are correctly installed and visible to the project:
```bash
make check
```

---

## 💻 2. Development Workflow

### Step 3: Initialize Infrastructure & Clean
Prepare a fresh environment by removing old build artifacts and spinning up the dev database:
```bash
make clean
make infra-up
```

### Step 4: Execute the Test Pyramid
Run the full suite of Unit and Integration tests. The suite includes a resilient retry loop to wait for MySQL readiness:
```bash
make test
```

---

## 📦 3. Production & Containerization

### Step 5: Full-Stack Execution (End-to-End)
This command builds the Java application inside a Docker image and runs the migration against a production-configured MySQL container. **The migration triggers automatically on startup.**
```bash
make prod-up
```

---

## 🔍 4. Verification

### Step 6: Verify Data Persistence
To confirm the 10 contacts (plus any test data) were successfully saved into the MySQL database, run the following command:
```bash
docker exec -it mysql_prod mysql -uroot -pprod_password -e "SELECT * FROM contact_vault.contacts;"
```

### Step 7: Teardown
Stop the production containers and clean up the environment:
```bash
make prod-down
```

---

## 🏗 Project Folder Structure

The project follows the **Standard Maven Layout**, organized by Domain-Driven Design principles:

```text
.
├── docker-compose.infra.yaml # Local MySQL Dev database
├── docker-compose.prod.yaml  # Full stack (App + DB) for production
├── Dockerfile                # Multi-stage build (Maven Build -> JRE Run)
├── Makefile                  # Developer command center (The Glue)
├── pom.xml                   # Pinned Java 11 & Shade Plugin (Uber-JAR)
└── src
    ├── main
    │   ├── java/com/migration
    │   │   ├── ContactMigrator.java # Application Entry Point
    │   │   ├── model/               # Data Entities (POJOs)
    │   │   ├── repository/          # SQL & JDBC logic (Data Access)
    │   │   └── service/             # Business Logic & CSV Parsing
    │   └── resources/               # Source CSV (contacts.csv)
    └── test
        └── java/com/migration       # Test Pyramid (Unit & Integration)
```

---

## 🛡 CI/CD Status
Every push to the `dev` branch triggers a **GitHub Action** that validates the build and executes all tests in a clean cloud environment to ensure zero regressions.
```

---

### Final Checkpoint:
* **Check:** `make check` verifies the version.
* **Clean:** `make clean` wipes the slate.
* **Infra-up:** `make infra-up` starts the DB.
* **Test:** `make test` runs the logic checks.
* **Prod-up:** `make prod-up` runs the full automation.
* **Verification:** The `docker exec` command proves it worked.

---
**Maintained by**: Surya & Nithin
```