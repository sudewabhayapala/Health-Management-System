# Healthcare Management System - Build Instructions

## Quick Start

### Windows Users
1. **Compile**: Double-click `compile.bat` or run in PowerShell/CMD:
   ```
   .\compile.bat
   ```

2. **Run**: Double-click `run.bat` or run:
   ```
   .\run.bat
   ```

### Manual Compilation
If the batch files don't work, compile manually:

```bash
# Create bin directory
mkdir bin

# Compile in dependency order
javac -source 1.8 -target 1.8 -d bin -cp bin src/model/*.java
javac -source 1.8 -target 1.8 -d bin -cp bin src/util/*.java
javac -source 1.8 -target 1.8 -d bin -cp bin src/controller/*.java
javac -source 1.8 -target 1.8 -d bin -cp bin src/view/*.java
```

### Manual Execution
```bash
java -cp bin view.MainFrame
```

## Requirements
- Java JDK 8 or higher
- Java JRE 8 or higher (must match or be newer than compilation target)

## Verifying Java Version
```bash
java -version
javac -version
```

## Common Issues

### "javac is not recognized"
- Add Java bin directory to PATH environment variable
- Example: `C:\Program Files\Java\jdk1.8.0_202\bin`

### "Could not find or load main class"
- Ensure you're in the project root directory
- Verify bin directory contains compiled .class files
- Check classpath is set correctly

### Version Mismatch Error
- Compile with: `-source 1.8 -target 1.8` flags
- Or update your JRE to match compiler version

## Project Structure After Build
```
healthcare-management-system/
├── bin/
│   ├── model/          (compiled .class files)
│   ├── view/           (compiled .class files)
│   ├── controller/     (compiled .class files)
│   └── util/           (compiled .class files)
├── src/                (source .java files)
├── data/               (CSV data files)
├── compile.bat         (Windows compile script)
└── run.bat            (Windows run script)
```

## Data Files Location
All CSV files should be in the `data/` directory:
- patients.csv
- clinicians.csv
- appointments.csv
- prescriptions.csv
- referrals.csv

The application will also create:
- data/email_communications.txt (referral emails)
- data/ehr_updates.txt (EHR updates)

## First Run
1. Compile the application
2. Ensure data CSV files exist
3. Run the application
4. Click **File → Load Data** to load sample data
5. Explore the different tabs to use features

## Incremental Development
After making code changes:
1. Run `compile.bat` to recompile
2. Run `run.bat` to test changes
3. Commit changes to git: `git add . && git commit -m "Description"`

## Git Workflow
```bash
# View commit history
git log --oneline

# Check current status
git status

# Add all changes
git add .

# Commit with message
git commit -m "Your descriptive message"
```
