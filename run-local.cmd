@echo off
cd /d %~dp0
if not exist .env (
  echo [run-local] .env not found. Run: copy .env.example .env
  exit /b 1
)
for /f "usebackq eol=# tokens=1,* delims==" %%a in (".env") do (
  if not "%%a"=="" set "%%a=%%b"
)
echo [run-local] Loaded .env, starting http://localhost:3000 ...
mvn spring-boot:run
