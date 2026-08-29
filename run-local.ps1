# Load .env and start backend on port 3000
# Usage: .\run-local.ps1

$ErrorActionPreference = "Stop"
$root = $PSScriptRoot
Set-Location $root

$envFile = Join-Path $root ".env"
$exampleFile = Join-Path $root ".env.example"

if (-not (Test-Path $envFile)) {
    Write-Host ""
    Write-Host "[run-local] .env not found." -ForegroundColor Yellow
    if (Test-Path $exampleFile) {
        Write-Host "Run: Copy-Item .env.example .env" -ForegroundColor Yellow
        Write-Host "Then set AI_API_KEY in .env" -ForegroundColor Yellow
    }
    exit 1
}

Get-Content $envFile -Encoding UTF8 | ForEach-Object {
    $line = $_.Trim()
    if ($line -eq "" -or $line.StartsWith("#")) { return }
    $eq = $line.IndexOf("=")
    if ($eq -lt 1) { return }
    $name = $line.Substring(0, $eq).Trim()
    $value = $line.Substring($eq + 1).Trim()
    if (
        ($value.StartsWith('"') -and $value.EndsWith('"')) -or
        ($value.StartsWith("'") -and $value.EndsWith("'"))
    ) {
        $value = $value.Substring(1, $value.Length - 2)
    }
    Set-Item -Path "env:$name" -Value $value
}

if (-not $env:AI_API_KEY) {
    Write-Host ""
    Write-Host "[run-local] Warning: AI_API_KEY is empty. AI will use fallback." -ForegroundColor Yellow
}

Write-Host ""
Write-Host "[run-local] Loaded .env, starting http://localhost:3000 ..." -ForegroundColor Green
Write-Host ""

mvn spring-boot:run
