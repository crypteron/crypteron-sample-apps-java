# on linux, launch with `pwsh`
param (
    [string]$projectName = "cipherobject-sample"
)

& mvn package --projects $projectName --also-make

$prevLoc = Get-Location
Set-Location $projectName

$jarFile = Get-ChildItem target/$projectName*.jar |  Select-Object -First 1
java -jar $jarFile

Set-Location $prevLoc