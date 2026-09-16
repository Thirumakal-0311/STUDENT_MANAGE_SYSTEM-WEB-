@IF "%DEBUG%"=="" @ECHO OFF
SETLOCAL ENABLEDELAYEDEXPANSION

SET ERROR_CODE=0

SETLOCAL

IF NOT "%JAVA_HOME%"=="" GOTO OkJHome
FOR %%i IN (java.exe) DO SET JAVA_EXE=%%~$PATH:i
IF NOT "%JAVA_EXE%"=="" GOTO OkJava

ECHO.
ECHO Error: JAVA_HOME is not defined and java.exe not found in PATH.
ECHO.
GOTO error

:OkJHome
SET JAVA_EXE=%JAVA_HOME%\bin\java.exe

:OkJava
IF EXIST "%JAVA_EXE%" GOTO init

ECHO.
ECHO Error: JAVA_HOME is set to an invalid directory: "%JAVA_HOME%"
ECHO.
GOTO error

:init
SET "BASE_DIR=%~dp0"
IF "%BASE_DIR:~-1%"=="\" SET "BASE_DIR=%BASE_DIR:~0,-1%"
SET "WWRAPPER_JAR=%BASE_DIR%\.mvn\wrapper\maven-wrapper.jar"

IF EXIST "%WWRAPPER_JAR%" GOTO run

ECHO Downloading Maven Wrapper jar...
powershell -NoProfile -ExecutionPolicy Bypass -Command "[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; (New-Object Net.WebClient).DownloadFile('https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar', '%WWRAPPER_JAR%')"

:run
"%JAVA_EXE%" "-Dmaven.multiModuleProjectDirectory=%BASE_DIR%" -cp "%WWRAPPER_JAR%" org.apache.maven.wrapper.MavenWrapperMain %*
IF ERRORLEVEL 1 GOTO error
GOTO end

:error
SET ERROR_CODE=1

:end
ENDLOCAL
EXIT /B %ERROR_CODE%
