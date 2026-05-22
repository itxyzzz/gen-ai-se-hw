@echo off
setlocal
cd /d "%~dp0\.."
call npm run verify:baseline || exit /b 1
call npm run pipeline:mock -- --scenario bug-001 --run run-001 || exit /b 1
call npm run promote -- --scenario bug-001 --run run-001 || exit /b 1
call npm test || exit /b 1
call npm run compare -- --scenario bug-001 || exit /b 1
