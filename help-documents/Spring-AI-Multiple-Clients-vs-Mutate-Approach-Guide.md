# Spring AI - Multiple ChatClient Beans vs mutate() Approach

# Overview

In Spring AI, there are two common approaches for working with multiple AI providers and models:

1. Multiple Spring Beans Approach
2. mutate() Configuration Reuse Approach

Both solve different problems.

This guide explains:

* Internal logic
* Architecture
* Real-world use cases
* Advantages
* Limitations
* When to use which approach

---

# 1. Multiple Spring Beans Approach

## Example

```java
@Configuration
public class ChatClientConfig {

    @Bean
    @Qualifier("openAPIChatClient")
    public ChatClient openAiChatClient(OpenAiChatModel chatModel) {
        return ChatClient.create(chatModel);
    }

    @Bean
    @Primary
    public ChatClient ollamaChatClient(OllamaChatModel chatModel) {
        return ChatClient.create(chatModel);
    }
}
```

---

# Internal Logic

Spring Boot creates:

```text
OpenAiChatModel Bean
        |
        v
openAiChatClient Bean

OllamaChatModel Bean
        |
        v
ollamaChatClient Bean
```

Each bean is independently managed by Spring IOC container.

---

# Dependency Injection Flow

```text
Application Startup
        |
        +----> OpenAiChatModel
        |
        +----> OllamaChatModel
        |
        +----> openAiChatClient
        |
        +----> ollamaChatClient
```

---

# Injection Example

## Specific Bean

```java
@Autowired
@Qualifier("openAPIChatClient")
private ChatClient openAiClient;
```

---

## Default Bean

```java
@Autowired
private ChatClient chatClient;
```

Spring injects:

```text
@Primary bean
```

which is:

```text
ollamaChatClient
```

---

# Real Production Use Cases

## Use Case 1 - Cost Optimization

```text
OpenAI
   -> premium reasoning

Ollama
   -> cheap/local requests
```

Architecture:

```text
User Request
      |
      +----> Premium AI Service -> OpenAI
      |
      +----> Internal Chat -> Ollama
```

---

## Use Case 2 - Department-Based AI Routing

```text
HR Team -> OpenAI
Engineering -> Qwen Local
Support -> Azure OpenAI
```

---

## Use Case 3 - Compliance / Security

Sensitive company data:

```text
Use local Ollama only
```

Public/general AI:

```text
Use OpenAI
```

---

# Advantages

| Advantage                 | Description                               |
| ------------------------- | ----------------------------------------- |
| Strong Spring integration | Works naturally with dependency injection |
| Clean architecture        | Separate AI services/providers            |
| Easy testing              | Mock individual beans                     |
| Production-friendly       | Standard enterprise design                |
| Stable configuration      | Provider configs fixed at startup         |

---

# Limitations

| Limitation                | Description                       |
| ------------------------- | --------------------------------- |
| More boilerplate          | Multiple beans/configurations     |
| Less dynamic              | Runtime provider switching harder |
| Configuration duplication | Similar configs repeated          |

---

# Best When

Use this approach when:

* Application permanently uses multiple providers
* Enterprise architecture
* Clear separation of AI services
* Production microservices
* Team-based ownership

---

# 2. mutate() Approach

## Example

```java
OpenAiApi openAiApi = OpenAiApi.builder()
        .apiKey(System.getenv("OPENAI_API_KEY"))
        .build();
```

Create modified version:

```java
OpenAiApi ollamaApi = openAiApi.mutate()
        .baseUrl("http://localhost:11434/v1")
        .apiKey("ollama")
        .build();
```

---

# Internal Logic

```text
Base OpenAI Config
        |
        +----> mutate()
                    |
                    +----> modified config
```

It clones existing configuration and changes selected properties.

---

# Real Meaning of mutate()

```text
clone existing object
+
change few fields
+
reuse remaining configuration
```

---

# Real-World Use Cases

## Use Case 1 - Runtime Model Switching

User selects model dynamically:

```text
GPT-4o
DeepSeek
Qwen
Ollama
```

Application creates modified clients dynamically.

---

## Use Case 2 - Multi-Tenant AI Platform

Each customer has:

* separate API key
* separate endpoint
* separate model

Instead of creating hundreds of Spring beans:

```text
Reuse base config using mutate()
```

---

## Use Case 3 - Testing Different Models

```text
Same request
+
multiple models
+
compare responses
```

Example:

```text
GPT-4o
GPT-4o-mini
Qwen
DeepSeek
```

---

## Use Case 4 - Failover Architecture

```text
Primary -> OpenAI
Backup -> Ollama
```

If OpenAI fails:

```text
mutate() to backup endpoint
```

---

# Advantages

| Advantage                | Description                   |
| ------------------------ | ----------------------------- |
| Less duplication         | Reuse existing configuration  |
| Dynamic runtime behavior | Change configs dynamically    |
| Flexible                 | Easy provider/model switching |
| Good for AI platforms    | Supports many providers       |
| Fast experimentation     | Easy testing/comparison       |

---

# Limitations

| Limitation                              | Description                   |
| --------------------------------------- | ----------------------------- |
| Less Spring-centric                     | More manual object management |
| Runtime complexity                      | Harder debugging              |
| Not ideal for static enterprise configs | Can become dynamic chaos      |

---

# Best When

Use mutate() when:

* Dynamic provider switching
* Runtime model selection
* AI experimentation platforms
* SaaS AI products
* Multi-tenant architectures
* Rapid prototyping

---

# Key Architectural Difference

## Multiple Beans

```text
Startup Time Decision
```

Spring decides provider wiring during application startup.

---

## mutate()

```text
Runtime Decision
```

Application dynamically creates modified configurations during execution.

---

# Real-Life Analogy

## Multiple Beans Approach

```text
Different cars in garage
- Honda
- Toyota
- BMW
```

Spring chooses which car to inject.

---

## mutate() Approach

```text
Same car platform
+
change engine/color/features dynamically
```

Reuse base configuration.

---

# Combined Enterprise Architecture

Large AI systems often use BOTH.

Example:

```text
Spring Beans
      |
      +----> Base OpenAI Client
                     |
                     +----> mutate() GPT-4o
                     |
                     +----> mutate() GPT-4o-mini
                     |
                     +----> mutate() Azure endpoint
                     |
                     +----> mutate() Local Ollama
```

---

# Recommended Approach for Different Scenarios

| Scenario                     | Recommended Approach |
| ---------------------------- | -------------------- |
| Enterprise microservices     | Multiple Beans       |
| Runtime model switching      | mutate()             |
| Stable provider architecture | Multiple Beans       |
| AI experimentation platform  | mutate()             |
| SaaS AI products             | mutate()             |
| Production Spring apps       | Multiple Beans       |
| Multi-provider routing       | Combination          |

---

# Final Recommendation

## For Enterprise Spring Boot Applications

Start with:

```text
Multiple Spring Beans
```

because:

* cleaner architecture
* easier debugging
* better dependency injection
* easier testing

---

## For Advanced AI Platforms

Use:

```text
Multiple Beans
+
mutate()
```

This gives:

* stable provider architecture
* dynamic runtime flexibility
* multi-model support
* scalable AI infrastructure
