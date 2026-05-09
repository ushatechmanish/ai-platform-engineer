# Enable Spring Boot DevTools in IntelliJ IDEA

## 1. Add Dependency

### Gradle

```gradle
dependencies {
    developmentOnly 'org.springframework.boot:spring-boot-devtools'
}
```

For Spring Boot 4:

```gradle
dependencies {
    developmentOnly("org.springframework.boot:spring-boot-devtools")
}
```

---

### Maven

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-devtools</artifactId>
    <scope>runtime</scope>
</dependency>
```

---

# 2. Enable Auto Build in IntelliJ

Navigate to:

```text
File
 → Settings
   → Build, Execution, Deployment
     → Compiler
```

Enable:

```text
Build project automatically
```

---

# 3. Advanced Settings
```text
Settings
→ Advanced Settings
 ```

Enable:

```text
Allow auto-make to start even if developed application is currently running
```

