#Requires -Version 5.1
<#
.SYNOPSIS
    Despliega todos los servicios de PiedraAzul (PostgreSQL, Backend, FrontendPiedraAzul, FrontEndMedicos).
.DESCRIPTION
    Lee los secretos desde C:\PiedraAzul\secrets\, levanta PostgreSQL via Docker,
    construye el backend con Maven y arranca los tres servicios en background.
#>
$ErrorActionPreference = 'Stop'

$RepoDir   = Split-Path -Parent $MyInvocation.MyCommand.Definition
$LogDir    = "C:\PiedraAzul\logs"
$SecretsDir = "C:\PiedraAzul\secrets"

New-Item -ItemType Directory -Force $LogDir | Out-Null

Write-Host "============================================" -ForegroundColor Cyan
Write-Host "  PiedraAzul - Despliegue"                   -ForegroundColor Cyan
Write-Host "============================================" -ForegroundColor Cyan
Write-Host "  Repo : $RepoDir"
Write-Host "  Logs : $LogDir"
Write-Host ""

# ---------------------------------------------------------------------------
# Helper: mata todos los procesos escuchando en un puerto dado
# ---------------------------------------------------------------------------
function Stop-Port([int]$Port) {
    $pids = (netstat -ano 2>&1) | Select-String ":${Port}\s" | ForEach-Object {
        ($_.ToString().Trim() -split '\s+')[-1]
    } | Sort-Object -Unique
    foreach ($pidStr in $pids) {
        if ($pidStr -match '^\d+$' -and [int]$pidStr -gt 4) {
            Write-Host "  Deteniendo PID $pidStr (puerto $Port)" -ForegroundColor DarkGray
            taskkill /PID $pidStr /F /T 2>&1 | Out-Null
        }
    }
}

# ---------------------------------------------------------------------------
# Helper: espera hasta que un puerto TCP acepte conexiones
# ---------------------------------------------------------------------------
function Wait-ForPort([int]$Port, [int]$TimeoutSec = 120) {
    $deadline = (Get-Date).AddSeconds($TimeoutSec)
    Write-Host -NoNewline "  Esperando puerto $Port"
    while ((Get-Date) -lt $deadline) {
        try {
            $tcp = New-Object System.Net.Sockets.TcpClient
            $tcp.Connect("127.0.0.1", $Port)
            $tcp.Close()
            Write-Host " listo!" -ForegroundColor Green
            return $true
        } catch {
            Start-Sleep -Seconds 2
            Write-Host -NoNewline "."
        }
    }
    Write-Host " tiempo agotado (puede que aun este iniciando)" -ForegroundColor Yellow
    return $false
}

# ---------------------------------------------------------------------------
# 0. Copiar secretos al workspace
# ---------------------------------------------------------------------------
Write-Host "[0/4] Copiando secretos..." -ForegroundColor Yellow
Copy-Item "$SecretsDir\backend.env"        "$RepoDir\BackendPiedraAzul\.env"    -Force
Copy-Item "$SecretsDir\frontend-azul.env"  "$RepoDir\FrontendPiedraAzul\.env"   -Force
Copy-Item "$SecretsDir\frontend-medicos.env" "$RepoDir\FrontEndMedicos\.env"    -Force
Write-Host "  OK"

# ---------------------------------------------------------------------------
# 1. PostgreSQL via Docker Compose
# ---------------------------------------------------------------------------
Write-Host ""
Write-Host "[1/4] Iniciando PostgreSQL..." -ForegroundColor Yellow
Push-Location $RepoDir
docker compose up -d postgres
if ($LASTEXITCODE -ne 0) { throw "docker compose up fallo con codigo $LASTEXITCODE" }
Pop-Location
Wait-ForPort 5432 60 | Out-Null

# ---------------------------------------------------------------------------
# 2. Build del Backend (Spring Boot) y arranque
# ---------------------------------------------------------------------------
Write-Host ""
Write-Host "[2/4] Compilando Backend (Maven)..." -ForegroundColor Yellow
Push-Location "$RepoDir\BackendPiedraAzul"
& .\mvnw.cmd package -DskipTests -B -q
if ($LASTEXITCODE -ne 0) { throw "mvnw package fallo con codigo $LASTEXITCODE" }
Pop-Location
Write-Host "  Compilacion exitosa"

Stop-Port 8081
Start-Sleep -Seconds 1

$jar = Get-ChildItem "$RepoDir\BackendPiedraAzul\target\BackendPiedraAzul-*.jar" |
    Where-Object { $_.Name -notlike '*plain*' } |
    Select-Object -First 1

if (-not $jar) { throw "No se encontro el JAR del backend en target/" }

Write-Host "  Iniciando Backend en puerto 8081..."
$backendCmd = "cd /d `"$RepoDir\BackendPiedraAzul`" && java -jar `"$($jar.FullName)`" > `"$LogDir\backend.log`" 2>&1"
Start-Process "cmd.exe" -ArgumentList "/c", $backendCmd -WindowStyle Hidden
Wait-ForPort 8081 120 | Out-Null

# ---------------------------------------------------------------------------
# 3. FrontendPiedraAzul (Angular, puerto 4200)
# ---------------------------------------------------------------------------
Write-Host ""
Write-Host "[3/4] Build y arranque FrontendPiedraAzul (puerto 4200)..." -ForegroundColor Yellow
Push-Location "$RepoDir\FrontendPiedraAzul"
npm install --legacy-peer-deps 2>&1 | Out-Null
npm run build 2>&1 | Select-Object -Last 3
Pop-Location

Stop-Port 4200
Start-Sleep -Seconds 1

$azulCmd = "cd /d `"$RepoDir\FrontendPiedraAzul`" && set PORT=4200 && node dist\FrontendPiedraAzul\server\server.mjs > `"$LogDir\frontend-azul.log`" 2>&1"
Start-Process "cmd.exe" -ArgumentList "/c", $azulCmd -WindowStyle Hidden
Write-Host "  FrontendPiedraAzul iniciado (SSR server, ~66 MB RAM)"

# ---------------------------------------------------------------------------
# 4. FrontEndMedicos (Angular SSR, puerto 4300)
# ---------------------------------------------------------------------------
Write-Host ""
Write-Host "[4/4] Build y arranque FrontEndMedicos (puerto 4300)..." -ForegroundColor Yellow
Push-Location "$RepoDir\FrontEndMedicos"
npm install --legacy-peer-deps 2>&1 | Out-Null
npm run build 2>&1 | Select-Object -Last 3
Pop-Location

Stop-Port 4300
Start-Sleep -Seconds 1

$medicosCmd = "cd /d `"$RepoDir\FrontEndMedicos`" && set PORT=4300 && node dist\FrontEndMedicos\server\server.mjs > `"$LogDir\frontend-medicos.log`" 2>&1"
Start-Process "cmd.exe" -ArgumentList "/c", $medicosCmd -WindowStyle Hidden
Write-Host "  FrontEndMedicos iniciado (SSR server, ~66 MB RAM)"

# ---------------------------------------------------------------------------
# Resumen
# ---------------------------------------------------------------------------
Write-Host ""
Write-Host "============================================" -ForegroundColor Green
Write-Host "  Despliegue completado!" -ForegroundColor Green
Write-Host "============================================" -ForegroundColor Green
Write-Host "  Backend:            http://localhost:8081"
Write-Host "  FrontendPiedraAzul: http://localhost:4200  (compila en ~60s)"
Write-Host "  FrontEndMedicos:    http://localhost:4300  (compila en ~60s)"
Write-Host "  PostgreSQL:         localhost:5432 / piedraazul"
Write-Host "  Logs:               $LogDir"
Write-Host ""
