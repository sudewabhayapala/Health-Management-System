@echo off
echo Starting Healthcare Management System...
echo.

REM Check if compiled
if not exist "bin\view\MainFrame.class" (
    echo Error: Application not compiled. Please run compile.bat first.
    pause
    exit /b 1
)

REM Run the application
java -cp bin view.MainFrame

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo Error: Application failed to start.
    pause
    exit /b 1
)
