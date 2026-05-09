# Ubuntu Ollama Installation and Service Setup Guide

# Overview

This guide explains:

* Installing Ollama on Ubuntu
* Starting Ollama automatically on system startup
* Installing CPU-only compatible models
* Running models locally
* Debugging Ollama service issues
* Connecting Ollama with Spring AI

---

# 1. Install Ollama

Install using the official installer:

```bash
curl -fsSL https://ollama.com/install.sh | sh
```

Verify installation:

```bash
ollama --version
```

---

# 2. Start Ollama Service

Start service:

```bash
sudo systemctl start ollama
```

Enable service on system startup:

```bash
sudo systemctl enable ollama
```

Check service status:

```bash
systemctl status ollama
```

Expected:

```text
active (running)
```

---

# 3. Verify Ollama API

Ollama exposes local API on:

```text
http://localhost:11434
```

Verify:

```bash
curl http://localhost:11434/api/tags
```

Expected response:

```json
{
  "models": []
}
```

---

# 4. CPU-Only Setup

If you see:

```text
WARNING: No NVIDIA/AMD GPU detected.
Ollama will run in CPU-only mode.
```

This is normal.

Ollama will use CPU inference.

Recommended CPU-friendly models:

| Model               | Recommendation   |
| ------------------- | ---------------- |
| qwen2.5-coder:7b    | Best balance     |
| llama3.1:8b         | Fast             |
| deepseek-coder:6.7b | Very fast        |
| qwen2.5-coder:14b   | Heavy but usable |
| qwen2.5-coder:30b   | Slow on CPU      |

---

# 5. Install Model

Recommended:

```bash
ollama pull qwen2.5-coder:7b
```

Alternative:

```bash
ollama pull llama3.1:8b
```

List installed models:

```bash
ollama list
```

---

# 6. Run Model

Run interactively:

```bash
ollama run qwen2.5-coder:7b
```

Example prompt:

```text
Write a Spring Boot REST API
```

Exit:

```text
/bye
```

---

# 7. Auto Start Ollama on Boot

Ollama service itself should auto start:

```bash
sudo systemctl enable ollama
```

Normally this is enough.

Ollama automatically loads models when first request arrives.

Recommended production flow:

```text
Spring AI Request
      |
      v
Ollama API
      |
      v
Auto-load Model
```

---

# 8. Optional: Auto Load Model at Startup

Create service:

```bash
sudo nano /etc/systemd/system/qwen.service
```

Add:

```ini
[Unit]
Description=Load Qwen Model in Ollama
After=network.target ollama.service

[Service]
Type=simple
ExecStart=/usr/bin/ollama run qwen2.5-coder:7b
Restart=always

[Install]
WantedBy=multi-user.target
```

Reload systemd:

```bash
sudo systemctl daemon-reload
```

Enable service:

```bash
sudo systemctl enable qwen
```

Start service:

```bash
sudo systemctl start qwen
```

Check:

```bash
systemctl status qwen
```

---

# 9. Debugging Ollama Service

## Check Service Status

```bash
systemctl status ollama
```

---

## Restart Service

```bash
sudo systemctl restart ollama
```

---

## View Logs

```bash
journalctl -u ollama -f
```

---

## Check Port

```bash
sudo ss -lntp | grep 11434
```

Expected:

```text
LISTEN 0 4096 127.0.0.1:11434
```

---

## Test API Directly

```bash
curl http://localhost:11434/api/tags
```

---

## Common Issue: Service Not Found

Wrong:

```bash
systemctl --user start ollama
```

Correct:

```bash
sudo systemctl start ollama
```

Reason:

```text
Ollama installs as system service, not user service.
```

---

# 10. Remove Model

```bash
ollama rm qwen2.5-coder:7b
```

---

# 11. Connect Ollama with Spring AI

## Gradle Dependency

```gradle
implementation 'org.springframework.ai:spring-ai-starter-model-ollama'
```

---

## application.properties

```properties
spring.ai.ollama.base-url=http://localhost:11434
spring.ai.ollama.chat.model=qwen2.5-coder:7b
```

---

## Spring Boot Controller

```java
@RestController
public class AIController {

    private final ChatClient chatClient;

    public AIController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @GetMapping("/ask")
    public String ask(@RequestParam String q) {

        return chatClient.prompt(q)
                .call()
                .content();
    }
}
```

---

# 12. Test Spring AI Endpoint

Browser:

```text
http://localhost:8080/ask?q=Explain%20microservices
```

Curl:

```bash
curl "http://localhost:8080/ask?q=Explain%20microservices"
```

---

# 13. Recommended Local AI Stack

```text
Ubuntu
+
Ollama
+
Qwen2.5-Coder 7B
+
Spring Boot
+
Spring AI
+
IntelliJ IDEA
```
