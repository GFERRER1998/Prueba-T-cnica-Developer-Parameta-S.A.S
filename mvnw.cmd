@REM ----------------------------------------------------------------------------
@REM Maven Wrapper startup batch script
@REM ----------------------------------------------------------------------------
@IF "%__MVNW_ARG0_NAME__%"=="" (SET "BASE_DIR=%~dp0") ELSE (SET "BASE_DIR=%~dp0\%__MVNW_ARG0_NAME__%")
@SET MAVEN_PROJECTBASEDIR=%BASE_DIR%
@SET "WRAPPER_LAUNCHER=org.apache.maven.wrapper.MavenWrapperMain"
@SET "WRAPPER_JAR=%BASE_DIR%.mvn\wrapper\maven-wrapper.jar"
@SET "WRAPPER_PROPERTIES=%BASE_DIR%.mvn\wrapper\maven-wrapper.properties"

@IF NOT EXIST "%WRAPPER_JAR%" (
    ECHO ERROR: Maven Wrapper .mvn\wrapper\maven-wrapper.jar not found! 1>&2
    GOTO fail
)

@SET JAVA_EXE=java.exe
@IF NOT "%JAVA_HOME%"=="" (
    SET "JAVA_EXE=%JAVA_HOME%\bin\java.exe"
)

"%JAVA_EXE%" -classpath "%WRAPPER_JAR%" "-Dmaven.multiModuleProjectDirectory=%MAVEN_PROJECTBASEDIR:~0,-1%" %WRAPPER_LAUNCHER% %MAVEN_CONFIG% %*
IF %ERRORLEVEL% NEQ 0 GOTO error
GOTO end

:error
SET ERROR_CODE=%ERRORLEVEL%
:end
EXIT /B %ERROR_CODE%

:fail
EXIT /B 1
