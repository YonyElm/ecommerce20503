
# Ecommerce backend

This is a simple e-commerce backend project built with Spring Boot, and JDBC

---

## Getting Started

### 0. Requirements

* java -version
* mvn -v

### 1. Create Project via Maven Archetype

```bash
mvn archetype:generate -DgroupId=com.example.ecommerce \
  -DartifactId=ecommerce \
  -DarchetypeArtifactId=maven-archetype-quickstart \
  -DinteractiveMode=false
```

### 2. Install Maven Dependencies

```bash
mvn clean install
```

---

## 🔧 Running the Project

### Start the Application

In **Terminal 1**, run:

```bash
mvn spring-boot:run
```

The server should start at `http://localhost:8080`.

---

## Testing the API

In **Terminal 2**, run:

```bash
./api-tests.sh
```

Make sure the application is running before executing the script.

---

## Debugging

To start the server with remote debugging enabled on port 5005:

```bash
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005"
```

You can then attach your IDE's debugger to `localhost:5005`.
