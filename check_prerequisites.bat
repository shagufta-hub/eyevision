@echo off
REM Maven Installation Verification Script for Windows

echo.
echo ╔════════════════════════════════════════════════════════════════╗
echo ║         Maven & Java Installation Verification Tool           ║
echo ╚════════════════════════════════════════════════════════════════╝
echo.

REM Check Java Installation
echo [1/2] Checking Java Installation...
java -version
if %ERRORLEVEL% EQU 0 (
    echo ✓ Java is installed
) else (
    echo ✗ Java is NOT installed
    echo.
    echo SOLUTION: Install Java 17 from https://www.oracle.com/java/technologies/downloads/
    pause
    exit /b 1
)

echo.
echo ─────────────────────────────────────────────────────────────────

REM Check Maven Installation
echo [2/2] Checking Maven Installation...
mvn -version
if %ERRORLEVEL% EQU 0 (
    echo ✓ Maven is installed
    echo.
    echo You can now run:
    echo   cd backend/eyevision
    echo   mvn clean install
    echo   mvn spring-boot:run
) else (
    echo ✗ Maven is NOT installed
    echo.
    echo SOLUTION:
    echo   1. Download Maven from: https://maven.apache.org/download.cgi
    echo   2. Extract to: C:\Program Files\apache-maven-3.9.X
    echo   3. Set MAVEN_HOME environment variable
    echo   4. Add %%MAVEN_HOME%%\bin to PATH
    echo   5. Restart this terminal
    echo   6. Run this script again
    echo.
    echo For detailed instructions, see: MAVEN_INSTALLATION.md
    pause
    exit /b 1
)

echo.
echo ═════════════════════════════════════════════════════════════════
echo ✓ All prerequisites are installed and ready!
echo ═════════════════════════════════════════════════════════════════
echo.
pause
