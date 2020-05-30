
echo Executing Grouping test in Whistle mobile project

pushd %~dp0%

mvn clean test -Dsurefire.suiteXmlFiles=TestngXml/commandParameterstestng.xml -DPlatform=ios

pause
