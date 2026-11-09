$ErrorActionPreference = 'Stop'

$classesDirectory = Join-Path $PSScriptRoot 'out/portable-classes'
$jarFile = Join-Path $PSScriptRoot 'out/WarCardGame-JAVA-Implementation.jar'
New-Item -ItemType Directory -Force -Path $classesDirectory | Out-Null

$sources = @(Get-ChildItem -LiteralPath (Join-Path $PSScriptRoot 'src/main/java') -Filter '*.java' -Recurse |
    ForEach-Object { $_.FullName })
& javac --release 11 -encoding UTF-8 -d $classesDirectory $sources
if ($LASTEXITCODE -ne 0) { throw 'Java compilation failed.' }

& jar cfe $jarFile cardgame.Main -C $classesDirectory cardgame -C (Join-Path $PSScriptRoot 'src/main/resources') resources/input.txt
if ($LASTEXITCODE -ne 0) { throw 'JAR packaging failed.' }

Write-Output "Built $jarFile"
