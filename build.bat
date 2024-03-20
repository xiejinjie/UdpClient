@echo off

set current_directory=%~dp0

rd /q /s %current_directory%\dist\
md %current_directory%\dist\

md %current_directory%\dist\app\
cd %current_directory% || exit /b 1
call mvn clean package -Dmaven.test.skip=true || exit /b 1

copy %current_directory%\target\UdpClient.jar %current_directory%\dist\app\
copy %current_directory%\fms.ico %current_directory%\dist\app\
xcopy %current_directory%\app\ %current_directory%\dist\app\ /s /e

cd %current_directory%
jpackage --type app-image --name UdpClient --input dist\app --dest dist --main-jar UdpClient.jar --icon fms.ico

cd %current_directory%\dist || exit /b 1
zip -r UdpClient.zip UdpClient

