# ⚡ Maven Installation - Quick Reference Card

## The Problem
```
mvn : The term 'mvn' is not recognized...
```

## The Solution (3 Steps)

### STEP 1️⃣: Download Maven
```
Go to: https://maven.apache.org/download.cgi
Download: apache-maven-3.9.5-bin.zip
```

### STEP 2️⃣: Extract Maven
```
Extract to: C:\Program Files\apache-maven-3.9.5\
```

### STEP 3️⃣: Set Environment Variables

#### Easiest Way (GUI):
```
1. Right-click "This PC" → Properties
2. Click "Advanced system settings"
3. Click "Environment Variables..."
4. Click "New..." in System variables
5. Add:
   Name: MAVEN_HOME
   Value: C:\Program Files\apache-maven-3.9.5
6. Find "Path" → Edit → New
7. Add: %MAVEN_HOME%\bin
8. Click OK everywhere
9. ⚠️ RESTART YOUR TERMINAL
```

#### PowerShell Way (Admin):
```powershell
[Environment]::SetEnvironmentVariable("MAVEN_HOME", "C:\Program Files\apache-maven-3.9.5", "Machine")
$env:Path += ";%MAVEN_HOME%\bin"
```

---

## 🔍 Verify Installation

```bash
# Open NEW terminal and run:
mvn -version

# Should see:
# Apache Maven 3.9.5
# Maven home: C:\Program Files\apache-maven-3.9.5
# Java version: 17.X.X
```

---

## 🚀 Now You Can Build

```bash
cd backend/eyevision
mvn clean install
mvn spring-boot:run
```

---

## ❓ Still Getting "mvn not recognized"?

✅ **Solution:** You didn't restart your terminal!
```
Close terminal → Open NEW terminal → Try again
```

❓ **If still failing:**

Check Maven exists:
```bash
dir "C:\Program Files\apache-maven-3.9.5\bin"
# Should show: mvn.cmd, mvn.bat
```

Check Java installed:
```bash
java -version
# Should show version info
```

Run auto-check script:
```bash
# Windows CMD:
check_prerequisites.bat

# PowerShell:
powershell -ExecutionPolicy Bypass -File .\check_prerequisites.ps1
```

---

## 📋 Checklist

```
✓ Maven downloaded
✓ Maven extracted to C:\Program Files\apache-maven-3.9.5\
✓ MAVEN_HOME env variable set
✓ Path variable updated with %MAVEN_HOME%\bin
✓ Terminal restarted
✓ mvn -version works
✓ java -version works
✓ Ready to run: mvn clean install
```

---

## 🆘 Quick Help Commands

| What | Command |
|-----|---------|
| Check Maven | `mvn -version` |
| Check Java | `java -version` |
| View PATH | `echo %PATH%` (CMD) or `$env:Path` (PS) |
| Check Maven exists | `dir "C:\Program Files\apache-maven-3.9.5\bin"` |
| Build project | `mvn clean install` |
| Run server | `mvn spring-boot:run` |

---

## 📖 For More Help

- Detailed setup: See `MAVEN_SETUP_WINDOWS.md`
- Full guide: See `MAVEN_INSTALLATION.md`
- Build issues: See `COMPLETE_SETUP_GUIDE.md` (Phase 10: Troubleshooting)

---

**That's it! Maven should work now.** 🎉
