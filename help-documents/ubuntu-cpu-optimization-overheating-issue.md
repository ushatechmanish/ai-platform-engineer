# Complete Ollama Optimization Setup for Dell Latitude 7300 (Ubuntu)

# Objective

Run Ollama models safely on Ubuntu laptop without:
- overheating
- loud fan noise
- UI lag
- CPU throttling
- battery drain

Target usage:
- Spring Boot + AI
- Local LLM experimentation
- Coding assistants
- Lightweight AI agents

---

# System Details

Machine:
- Dell Latitude 7300
- Ubuntu Linux
- CPU-based inference
- No dedicated GPU

---

# Main Problem

Running models like:
- deepseek-r1:1.5b
- deepseek-coder:1.3b

causes:
- 100% CPU usage
- fan continuously spinning
- temperature spikes
- system lag

Reason:
- Ollama uses CPU heavily
- Intel Turbo Boost increases clock speeds aggressively
- LLM inference is compute intensive

---

# Final Solution Overview

Applied optimizations:

| Optimization | Purpose |
|---|---|
| Reduce thread count | Lower CPU load |
| Disable Turbo Boost | Lower temperature |
| Use nice priority | Keep system responsive |
| Use lightweight models | Reduce thermal pressure |
| Create wrapper script | Automate everything |

---

# Step 1 - Install Required Utilities

## Install lm-sensors

```bash id="0nkmqy"
sudo apt install lm-sensors

Used for:

temperature monitoring

Check temperature:

sensors
Install TLP
sudo apt install tlp
sudo systemctl enable tlp
sudo systemctl start tlp

Purpose:

laptop thermal optimization
battery optimization
better CPU power management
Step 2 - Verify Ollama CPU Usage

Check running models:

ollama ps

Example:

NAME                   PROCESSOR
deepseek-coder:1.3b    100% CPU

This means:

no GPU acceleration
CPU is doing all inference
Step 3 - Understand Turbo Boost

Intel CPUs increase frequency automatically.

Example:

Mode	Frequency
Base Clock	1.9 GHz
Turbo Boost	3.8+ GHz

Turbo Boost increases:

heat
fan speed
battery usage

during AI workloads.

Step 4 - Disable Turbo Boost
Disable
echo 1 | sudo tee /sys/devices/system/cpu/intel_pstate/no_turbo
Enable Again
echo 0 | sudo tee /sys/devices/system/cpu/intel_pstate/no_turbo
Check Status
cat /sys/devices/system/cpu/intel_pstate/no_turbo
Value	Meaning
1	Turbo OFF
0	Turbo ON
Result of Turbo Disable
Before	After
85-95°C	65-75°C
Loud fan	Much quieter
CPU spikes	Stable temperatures

Estimated reduction:

10-20°C
Step 5 - Reduce Thread Usage
Environment Variables
export OMP_NUM_THREADS=2
export OLLAMA_NUM_PARALLEL=1
What They Do
OMP_NUM_THREADS=2

Limits inference threads.

Result:

lower CPU usage
lower heat
OLLAMA_NUM_PARALLEL=1

Prevents multiple parallel model executions.

Result:

reduced memory pressure
lower CPU spikes
Step 6 - Use Lower CPU Priority
Command
nice -n 19 ollama run MODEL
Result

Keeps Ubuntu responsive.

Without nice:

UI freezes
keyboard lag
browser stutter

With nice:

smoother system usage
Step 7 - Why cpulimit Was Removed

Initial attempt:

cpulimit -l 60 -- ollama run phi3:mini

Problems:

Process dead
input/output error

Reason:

interactive Ollama sessions spawn child processes
cpulimit loses tracking

Final decision:

removed cpulimit
Step 8 - Create Optimized Script
Create Script Folder
mkdir -p ~/scripts
Create Script File
nano ~/scripts/ollama-run

Paste below script.

Final Optimized Script
#!/bin/bash

declare -A MODELS
MODELS[1]="deepseek-r1:1.5b"
MODELS[2]="deepseek-coder:1.3b"
MODELS[3]="qwen2.5:0.5b"
MODELS[4]="qwen2.5:1.5b"
MODELS[5]="phi3:mini"
MODELS[6]="tinyllama"

echo "=============================="
echo "      OLLAMA MODEL MENU"
echo "=============================="
echo "1) DeepSeek R1 1.5B"
echo "2) DeepSeek Coder 1.3B"
echo "3) Qwen 2.5 0.5B"
echo "4) Qwen 2.5 1.5B"
echo "5) Phi3 Mini"
echo "6) TinyLlama"
echo "=============================="

read -p "Enter option number: " OPTION

MODEL="${MODELS[$OPTION]}"

if [[ -z "$MODEL" ]]; then
  echo "Invalid option"
  exit 1
fi

echo ""
echo "Running: $MODEL"
echo "Turbo Boost OFF"
echo ""

echo 1 | sudo tee /sys/devices/system/cpu/intel_pstate/no_turbo > /dev/null

export OMP_NUM_THREADS=2
export OLLAMA_NUM_PARALLEL=1

nice -n 19 ollama run "$MODEL"

echo ""
echo "Turbo Boost ON"

echo 0 | sudo tee /sys/devices/system/cpu/intel_pstate/no_turbo > /dev/null
Step 9 - Make Script Executable
chmod +x ~/scripts/ollama-run
Step 10 - Create Alias

Edit bashrc:

vi ~/.bashrc

Add:

alias ollama-run='~/scripts/ollama-run'

Reload:

source ~/.bashrc
Step 11 - Run Script
ollama-run
Example Usage
==============================
      OLLAMA MODEL MENU
==============================
1) DeepSeek R1 1.5B
2) DeepSeek Coder 1.3B
3) Qwen 2.5 0.5B
4) Qwen 2.5 1.5B
5) Phi3 Mini
6) TinyLlama
==============================

Enter option number: 5
Recommended Models
Best for This Laptop
Model	Heat	Speed
qwen2.5:0.5b	Very Low	Fast
tinyllama	Lowest	Very Fast
phi3:mini	Moderate	Good
Avoid

Avoid continuous usage of:

7B+ models
coding models for long durations
multiple loaded models
Spring Boot Integration

Run Ollama separately:

ollama-run

Run Spring Boot app:

./gradlew bootRun

or

mvn spring-boot:run

Spring Boot connects to:

http://localhost:11434
Monitoring Commands
CPU Usage
htop
Running Models
ollama ps
Temperature
sensors
Stop Ollama
Stop Model
ollama stop MODEL_NAME

Example:

ollama stop phi3:mini
Stop Entire Ollama Service
sudo pkill ollama
Final Results
Before Optimization
Metric	Value
CPU Usage	100%
Temperature	90°C+
Fan Noise	Very High
Responsiveness	Poor
Battery	Fast drain
After Optimization
Metric	Value
CPU Usage	40-70%
Temperature	65-75°C
Fan Noise	Much Lower
Responsiveness	Stable
Battery	Improved
Recommended Future Upgrade

For serious local AI development:

Recommended:

NVIDIA GPU
64GB RAM
Desktop machine
Better cooling

Current laptop is suitable for:

Spring AI learning
Small LLMs
AI experimentation
Lightweight coding assistants