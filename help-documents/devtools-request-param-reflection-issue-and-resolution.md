# Spring Boot DevTools RequestParam Reflection Issue and Resolution

# Overview

This document explains a common issue in Spring Boot applications when using:

* Spring Boot DevTools
* Request parameters without @RequestParam
* Incremental compilation in IntelliJ IDEA

It also explains:

* Root cause
* Internal Spring logic
* Why it works sometimes and fails later
* Permanent resolutions
* Best practices

---

# Problem Statement

Application endpoint:

```java
@GetMapping("/question")
public String question(String userInput) {

    return this.chatClient.prompt(userInput)
            .call()
            .content();
}
```

Request:

```http
GET http://localhost:8080/question?userInput=hello
```

Initially works correctly.

After DevTools restart or recompilation:

```http
HTTP 500 Internal Server Error
```

---

# Actual Error

```text
java.lang.IllegalArgumentException:
Name for argument of type [java.lang.String] not specified,
and parameter name information not available via reflection.
Ensure that the compiler uses the '-parameters' flag.
```

---

# Root Cause

Spring tries to automatically infer request parameter names.

This works only when:

```text
Java compiler stores method parameter names in bytecode.
```

Example:

```java
public String question(String userInput)
```

Spring internally attempts:

```text
method parameter name -> userInput
```

using Java reflection.

---

# Internal Spring Flow

```text
HTTP Request
      |
      v
/question?userInput=hello
      |
      v
DispatcherServlet
      |
      v
HandlerMethodArgumentResolver
      |
      v
Reflection API
      |
      v
Read parameter name from bytecode
      |
      +----> SUCCESS -> bind value
      |
      +----> FAILURE -> exception
```

---

# Why It Worked Earlier

Initially:

```text
parameter metadata existed in compiled classes
```

because compiler generated:

```text
-parameters
```

metadata.

---

# Why It Failed After DevTools Restart

Spring Boot DevTools performs:

```text
incremental recompilation + class reloading
```

Sometimes IntelliJ incremental compiler:

* skips parameter metadata
* recompiles differently
* reloads classes without reflection parameter names

Then Spring can no longer infer:

```text
userInput
```

from:

```java
String userInput
```

---

# Why @RequestParam Fixes It

Correct code:

```java
@GetMapping("/question")
public String question(@RequestParam String userInput) {

    return this.chatClient.prompt(userInput)
            .call()
            .content();
}
```

Now Spring does NOT need reflection metadata.

It directly knows:

```text
map request parameter -> userInput
```

---

# Internal Flow with @RequestParam

```text
HTTP Request
      |
      v
/question?userInput=hello
      |
      v
@RequestParam("userInput")
      |
      v
Direct binding
      |
      v
SUCCESS
```

---

# Recommended Production Fix

Always explicitly specify:

```java
@RequestParam
@PathVariable
@RequestHeader
@RequestBody
```

instead of relying on reflection inference.

---

# Alternative Fix - Enable Parameter Metadata

## Gradle Configuration

```gradle
tasks.withType(JavaCompile) {
    options.compilerArgs += '-parameters'
}
```

Rebuild:

```bash
./gradlew clean build
```

Restart application.

---

# Why This Works

Compiler now stores:

```text
method parameter names in bytecode
```

allowing Spring reflection to discover:

```text
userInput
```

---

# Best Practice Comparison

| Approach                       | Recommended     |
| ------------------------------ | --------------- |
| String userInput               | Not recommended |
| @RequestParam String userInput | Recommended     |
| Reflection-based inference     | Fragile         |
| Explicit annotations           | Stable          |

---

# Real Production Recommendation

Use:

```java
@GetMapping("/question")
public String question(@RequestParam String userInput) {

    return this.chatClient.prompt(userInput)
            .call()
            .content();
}
```

Advantages:

* explicit mapping
* stable after DevTools restart
* easier debugging
* better readability
* safer production behavior
* no dependency on reflection metadata

---

# Key Learning

Spring automatic parameter mapping:

```java
public String question(String userInput)
```

works only when:

```text
parameter name metadata exists in compiled bytecode.
```

DevTools/incremental compilation can break this assumption.

Explicit annotations avoid this problem completely.

---

# Final Recommendation

For all Spring Boot APIs:

Always prefer:

```java
@RequestParam
@PathVariable
@RequestBody
@RequestHeader
```

instead of implicit reflection-based parameter inference.
