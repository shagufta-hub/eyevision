# Maven Installation Verification Script for PowerShell
# Run as: powershell -ExecutionPolicy Bypass -File .\check_prerequisites.ps1

Write-Host "`n" -ForegroundColor Green
Write-Host "╔════════════════════════════════════════════════════════════════╗" -ForegroundColor Green
Write-Host "║         Maven & Java Installation Verification Tool           ║" -ForegroundColor Green
Write-Host "╚════════════════════════════════════════════════════════════════╝" -ForegroundColor Green
Write-Host "`n" -ForegroundColor Green

# Check Java Installation
Write-Host "[1/2] Checking Java Installation..." -ForegroundColor Yellow

try {
    $javaVersion = java -version 2>&1
    Write-Host "✓ Java is installed" -ForegroundColor Green
    Write-Host $javaVersion -ForegroundColor Cyan
} catch {
    Write-Host "✗ Java is NOT installed" -ForegroundColor Red
    Write-Host "`nSOLUTION: Install Java 17 from https://www.oracle.com/java/technologies/downloads/" -ForegroundColor Yellow
    exit 1
}

Write-Host "`n─────────────────────────────────────────────────────────────────" -ForegroundColor Gray

# Check Maven Installation
Write-Host "[2/2] Checking Maven Installation..." -ForegroundColor Yellow

try {
    $mavenVersion = mvn -version 2>&1
    Write-Host "✓ Maven is installed" -ForegroundColor Green
    Write-Host $mavenVersion -ForegroundColor Cyan
    
    Write-Host "`n═════════════════════════════════════════════════════════════════" -ForegroundColor Green
    Write-Host "✓ All prerequisites are installed and ready!" -ForegroundColor Green
    Write-Host "═════════════════════════════════════════════════════════════════" -ForegroundColor Green
    
    Write-Host "`nYou can now run:" -ForegroundColor Green
    Write-Host "  cd backend\eyevision" -ForegroundColor Cyan
    Write-Host "  mvn clean install" -ForegroundColor Cyan
    Write-Host "  mvn spring-boot:run" -ForegroundColor Cyan
} catch {
    Write-Host "✗ Maven is NOT installed" -ForegroundColor Red
    Write-Host "`nSOLUTION:" -ForegroundColor Yellow
    Write-Host "  1. Download Maven from: https://maven.apache.org/download.cgi" -ForegroundColor Yellow
    Write-Host "  2. Extract to: C:\Program Files\apache-maven-3.9.X" -ForegroundColor Yellow
    Write-Host "  3. Set MAVEN_HOME environment variable" -ForegroundColor Yellow
    Write-Host "  4. Add %MAVEN_HOME%\bin to PATH" -ForegroundColor Yellow
    Write-Host "  5. Restart PowerShell" -ForegroundColor Yellow
    Write-Host "  6. Run this script again" -ForegroundColor Yellow
    Write-Host "`nFor detailed instructions, see: MAVEN_INSTALLATION.md" -ForegroundColor Yellow
    exit 1
}

Write-Host "`n" -ForegroundColor Green
