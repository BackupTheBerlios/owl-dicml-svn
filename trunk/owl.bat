REM starts owl, please don't start owl.jar directly or make sure that this 
REM directory is the working-directory
ECHO OFF
cd $INSTALL_PATH
START javaw -jar bin/owl.jar
EXIT