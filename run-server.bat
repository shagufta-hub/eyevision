@echo off
cd /d %~dp0backend\eyevision
mvn clean spring-boot:run
pause
