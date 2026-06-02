#Requires -Version 5.1
<#
.SYNOPSIS
    Detiene todos los servicios de PiedraAzul.
#>

function Stop-Port([int]$Port) {
    $lines = (netstat -ano 2>&1) | Select-String "TCP\s+[0-9.:]+:$Port\s+[0-9.:]+:0\s+LISTENING"
    foreach ($line in $lines) {
        $pidStr = ($line.ToString().Trim() -split '\s+')[-1]
        if ($pidStr -match '^\d+$' -and [int]$pidStr -gt 4) {
            Write-Host "  Deteniendo PID $pidStr (puerto $Port)"
            taskkill /PID $pidStr /F /T 2>&1 | Out-Null
        }
    }
}

Write-Host "Deteniendo servicios PiedraAzul..." -ForegroundColor Yellow
Stop-Port 8081
Stop-Port 4200
Stop-Port 4300

$repoDir = Split-Path -Parent $MyInvocation.MyCommand.Definition
Push-Location $repoDir
docker compose down
Pop-Location

Write-Host "Todos los servicios detenidos." -ForegroundColor Green
