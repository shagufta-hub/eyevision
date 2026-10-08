# Maven Installation Guide for Windows

## ❌ Problem
`mvn` command is not recognized in PowerShell/Command Prompt

## ✅ Solution: Install Maven

### Step 1: Download Maven

1. Go to: https://maven.apache.org/download.cgi
2. Download the **Binary zip archive** (e.g., `apache-maven-3.9.x-bin.zip`)
   - Look for: "apache-maven-X.X.X-bin.zip"

### Step 2: Extract Maven

1. Extract the downloaded zip file to a permanent location:
   ```
   Recommended: C:\Program Files\apache-maven-3.9.X
   ```
   
2. **DO NOT** extract to Desktop or Downloads (temporary locations)

### Step 3: Set Up Environment Variables

#### Option A: Using GUI (Easiest)

1. Right-click **This PC** or **My Computer** → **Properties**
   
2. Click **Advanced system settings** on the left
   
3. Click **Environment Variables** button
   
4. In **System variables** section, click **New**
   
   | Variable Name | Variable Value |
   |---|---|
   | MAVEN_HOME | C:\Program Files\apache-maven-3.9.X |
   
5. Click **OK**

6. Find **Path** variable in System variables → **Edit**
   
7. Click **New** and add:
   ```
   %MAVEN_HOME%\bin
   ```
   
8. Click **OK** multiple times

#### Option B: Using PowerShell (Advanced)

Open PowerShell as Administrator and run:

```powershell
# Set MAVEN_HOME
[Environment]::SetEnvironmentVariable("MAVEN_HOME", "C:\Program Files\apache-maven-3.9.X", "Machine")

# Add to PATH
$currentPath = [Environment]::GetEnvironmentVariable("Path", "Machine")
$newPath = $currentPath + ";%MAVEN_HOME%\bin"
[Environment]::SetEnvironmentVariable("Path", $newPath, "Machine")
```

### Step 4: Restart Command Prompt / PowerShell

**Important:** Close and reopen your terminal window for changes to take effect

### Step 5: Verify Installation

Open a new Command Prompt or PowerShell and run:

```bash
mvn -version
```

**Expected Output:**
```
Apache Maven 3.9.X
Maven home: C:\Program Files\apache-maven-3.9.X
Java version: 17.X.X
```

---

## 🎯 Next Steps After Maven Installation

Once Maven is installed and verified:

```bash
# Navigate to backend directory
cd backend/eyevision

# Run the build command
mvn clean install

# If build succeeds, run:
mvn spring-boot:run
```

---

## ⚠️ Common Issues

### Issue: Still getting "mvn not recognized"
**Solution:** You didn't restart PowerShell/Command Prompt after setting environment variables
→ Close and reopen the terminal

### Issue: MAVEN_HOME path not found
**Solution:** Check the actual path where you extracted Maven
→ Run `dir "C:\Program Files"` to verify folder name

### Issue: Java is not installed
**Solution:** You need Java 17+ before Maven can work
→ Download from https://www.oracle.com/java/technologies/downloads/
→ Check: `java -version`

---

## 📝 Summary

1. ✓ Download Maven from maven.apache.org
2. ✓ Extract to C:\Program Files\apache-maven-3.9.X
3. ✓ Set MAVEN_HOME environment variable
4. ✓ Add %MAVEN_HOME%\bin to PATH
5. ✓ Restart terminal
6. ✓ Verify: `mvn -version`
7. ✓ Build: `mvn clean install`

---

## Quick Verification Checklist

After installation, verify by running:

```bash
# Check Maven
mvn -version

# Check Java
java -version

# Both should return version information without errors
```

**Once both work, you can run:**
```bash
cd backend/eyevision
mvn clean install
mvn spring-boot:run
```
