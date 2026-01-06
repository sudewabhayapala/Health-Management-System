@echo off
echo Compiling Healthcare Management System...
echo.

REM Create bin directory if it doesn't exist
if not exist "bin" mkdir bin

REM Compile in dependency order
echo Compiling model classes...
javac -source 1.8 -target 1.8 -d bin -cp bin src\model\*.java
if %ERRORLEVEL% NEQ 0 goto :error

echo Compiling utility classes...
javac -source 1.8 -target 1.8 -d bin -cp bin src\util\*.java
if %ERRORLEVEL% NEQ 0 goto :error

echo Compiling controller classes...
javac -source 1.8 -target 1.8 -d bin -cp bin src\controller\*.java
if %ERRORLEVEL% NEQ 0 goto :error

echo Compiling view classes...
javac -source 1.8 -target 1.8 -d bin -cp bin src\view\*.java
if %ERRORLEVEL% NEQ 0 goto :error

echo.
echo Compilation successful!
goto :end

:error
echo.
echo Compilation failed! Please check the errors above.
exit /b 1

:end
