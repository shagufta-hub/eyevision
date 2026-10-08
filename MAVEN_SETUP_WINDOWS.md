# Maven Installation - Step by Step (with Screenshots)

## 📥 Step 1: Download Maven

### 1.1 Go to Maven Website
- Open: https://maven.apache.org/download.cgi

### 1.2 Download Binary Zip Archive
```
Look for section: "Files"
Download: apache-maven-3.9.5-bin.zip (or latest version)
File size: ~11 MB
```

### 1.3 Save the File
```
Save to: Downloads folder (or any temporary location)
```

---

## 📦 Step 2: Extract Maven

### 2.1 Right-click the Downloaded File
```
File: apache-maven-3.9.5-bin.zip
Right-click → Extract All...
```

### 2.2 Choose Extract Location
```
IMPORTANT: Choose permanent location
Recommended: C:\Program Files\

Path should look like:
C:\Program Files\apache-maven-3.9.5\
```

### 2.3 Verify Extraction
Navigate to the extracted folder:
```
C:\Program Files\apache-maven-3.9.5\
  ├── bin\         (contains mvn.cmd)
  ├── boot\
  ├── conf\
  ├── lib\
  └── README.txt
```

---

## 🔧 Step 3: Set Environment Variables

### Option A: Using Windows GUI (Easiest for Beginners)

#### 3A.1: Open System Properties
1. Press: `Win + X`
2. Select: "System"
3. In left sidebar, click: "Advanced system settings"

#### 3A.2: Open Environment Variables
1. Click button: "Environment Variables..." (bottom right)
2. Dialog opens with two sections:
   - User variables (top)
   - System variables (bottom)

#### 3A.3: Create MAVEN_HOME Variable
1. In "System variables" section, click: "New..."
2. Fill in:
   ```
   Variable name:  MAVEN_HOME
   Variable value: C:\Program Files\apache-maven-3.9.5
   ```
3. Click: "OK"

#### 3A.4: Update PATH Variable
1. In "System variables" section, find: "Path"
2. Select "Path" → Click: "Edit..."
3. Click: "New"
4. Add:
   ```
   %MAVEN_HOME%\bin
   ```
5. Click: "OK"
6. Click: "OK" (again)

#### 3A.5: Click "OK" to Close
Close all dialog boxes

---

### Option B: Using PowerShell (For Advanced Users)

Open **PowerShell as Administrator** and run:

```powershell
# Step 1: Set MAVEN_HOME
[Environment]::SetEnvironmentVariable(
    "MAVEN_HOME",
    "C:\Program Files\apache-maven-3.9.5",
    "Machine"
)

# Step 2: Get current PATH
$currentPath = [Environment]::GetEnvironmentVariable("Path", "Machine")

# Step 3: Add Maven to PATH
$newPath = $currentPath + ";%MAVEN_HOME%\bin"
[Environment]::SetEnvironmentVariable("Path", $newPath, "Machine")

# Step 4: Verify
Write-Host "Maven environment variables set successfully!"
```

---

## ⚡ Step 4: Restart Terminal

**This is CRITICAL** - Changes won't take effect until you restart

### Option 1: Restart Command Prompt
1. Close all Command Prompt windows
2. Open a NEW Command Prompt window
3. Type: `mvn -version`

### Option 2: Restart PowerShell
1. Close all PowerShell windows
2. Open a NEW PowerShell window
3. Type: `mvn -version`

---

## ✅ Step 5: Verify Installation

Open Command Prompt or PowerShell and run:

```bash
mvn -version
```

### Expected Output:
```
Apache Maven 3.9.5
Maven home: C:\Program Files\apache-maven-3.9.5
Java version: 17.0.X
```

### If you see "mvn is not recognized":
```
❌ Environment variables didn't take effect
✓ Solution: Restart your terminal and try again
```

### If you see error about Java:
```
❌ Java is not installed or not in PATH
✓ Solution: Install Java 17 first
   Download from: https://www.oracle.com/java/technologies/downloads/
```

---

## 🚀 Step 6: Build Your Project

Once Maven is verified working:

```bash
# Navigate to project
cd backend/eyevision

# Clean and build
mvn clean install

# Wait for completion (2-5 minutes)
# You should see: [INFO] BUILD SUCCESS
```

---

## 🐛 Troubleshooting

### Problem 1: "mvn is not recognized"
```
Cause: Terminal not restarted after setting env variables
Fix: Close and reopen your terminal/PowerShell
```

### Problem 2: "Maven cannot find Java"
```
Cause: Java is not installed
Fix: Install Java 17+ from https://www.oracle.com/java/technologies/downloads/
```

### Problem 3: "Cannot find MAVEN_HOME"
```
Cause: Path to Maven folder doesn't exist
Fix: Check if Maven is extracted to the correct location
   C:\Program Files\apache-maven-3.9.5\bin\mvn.cmd
```

### Problem 4: "BUILD FAILURE"
```
Possible causes:
- Network issue (can't download dependencies)
- Maven repository server down
- Corrupted pom.xml file

Solution: 
- Run: mvn clean install -DskipTests
- Or run: mvn clean -Dmaven.wagon.http.ssl.insecure=true clean install
```

### Problem 5: "Connection refused"
```
Cause: Firewall blocking Maven's internet access
Fix: Temporarily disable firewall or add exception for Maven
```

---

## 📋 Quick Checklist

Before building your project:

```
☐ Maven downloaded and extracted to C:\Program Files\apache-maven-3.9.5\
☐ MAVEN_HOME environment variable created
☐ %MAVEN_HOME%\bin added to PATH variable
☐ Terminal/PowerShell restarted
☐ Verified: mvn -version shows Maven version
☐ Verified: java -version shows Java 17+
☐ Ready to build: mvn clean install
```

---

## 📞 Need Help?

1. Run verification script:
   ```bash
   # For Command Prompt:
   check_prerequisites.bat
   
   # For PowerShell:
   powershell -ExecutionPolicy Bypass -File .\check_prerequisites.ps1
   ```

2. See detailed guide:
   → MAVEN_INSTALLATION.md

3. See build instructions:
   → COMPLETE_SETUP_GUIDE.md

---

## ⏱️ Next Steps

After Maven is installed:

```bash
# 1. Run database migration
mysql -u root -p eyevision < backend/eyevision/src/main/resources/db_migration.sql

# 2. Configure email in application.properties
# Edit: backend/eyevision/src/main/resources/application.properties
# Set: mail.smtp.username=your-email@gmail.com
#      mail.smtp.password=your-app-password

# 3. Build project
cd backend/eyevision
mvn clean install

# 4. Run server
mvn spring-boot:run

# 5. Test (in another terminal)
curl -X POST http://localhost:8080/api/orders/place \
  -H "Content-Type: application/json" \
  -d '{"customerName":"Test","email":"test@gmail.com",...}'
```

---

**Maven Installation Complete! 🎉**

Your system is now ready to build and run the backend.
