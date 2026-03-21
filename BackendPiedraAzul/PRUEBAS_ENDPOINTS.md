# Pruebas Rápidas - Endpoints de Usuarios

## 1️⃣ CREAR USUARIO (Sin autenticación - Público)

### Request:
```http
POST http://localhost:8081/api/users/create
Content-Type: application/json

{
  "email": "juan@ejemplo.com",
  "password": "Juan123!",
  "firstName": "Juan",
  "lastName": "Perez",
  "roles": ["medico", "paciente"]
}
```

### Response (201 Created):
```json
{
  "status": "success",
  "message": "User created successfully!!",
  "email": "juan@ejemplo.com"
}
```

### Response (409 Conflict - Usuario existe):
```json
{
  "status": "error",
  "message": "User exist already!"
}
```

---

## 2️⃣ LISTAR TODOS LOS USUARIOS

### Request:
```http
GET http://localhost:8081/api/users/all
Authorization: Bearer YOUR_ADMIN_TOKEN
```

### Response (200 OK):
```json
[
  {
    "id": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
    "username": "juan@ejemplo.com",
    "email": "juan@ejemplo.com",
    "firstName": "Juan",
    "lastName": "Perez",
    "emailVerified": true,
    "enabled": true,
    "createdTimestamp": 1678934567000,
    "notBefore": 0,
    "realmRoles": ["medico", "paciente", "user"],
    "clientRoles": {
      "piedraAzul-dev": ["medico", "paciente"]
    },
    "clientConsents": [],
    "requiredActions": [],
    "federatedIdentities": [],
    "socialLinks": [],
    "attributes": {}
  }
]
```

---

## 3️⃣ BUSCAR USUARIO POR EMAIL

### Request:
```http
GET http://localhost:8081/api/users/search?username=juan@ejemplo.com
Authorization: Bearer YOUR_ADMIN_TOKEN
```

### Response (200 OK):
```json
[
  {
    "id": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
    "username": "juan@ejemplo.com",
    "email": "juan@ejemplo.com",
    "firstName": "Juan",
    "lastName": "Perez",
    "emailVerified": true,
    "enabled": true
  }
]
```

---

## 4️⃣ ASIGNAR ROLES A USUARIO EXISTENTE

### Request:
```http
POST http://localhost:8081/api/users/a1b2c3d4-e5f6-7890-abcd-ef1234567890/assign-client-roles
Authorization: Bearer YOUR_ADMIN_TOKEN
Content-Type: application/json

{
  "roles": ["admin", "medico", "paciente"]
}
```

### Response (200 OK):
```json
{
  "status": "success",
  "message": "Roles del cliente asignados exitosamente",
  "userId": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
  "rolesAssigned": "admin, medico, paciente"
}
```

### Response (400 Bad Request - Sin roles):
```json
{
  "status": "error",
  "message": "Debe proporcionar al menos un rol"
}
```

---

## 5️⃣ ELIMINAR USUARIO

### Request:
```http
DELETE http://localhost:8081/api/users/a1b2c3d4-e5f6-7890-abcd-ef1234567890
Authorization: Bearer YOUR_ADMIN_TOKEN
```

### Response (200 OK):
```json
{
  "status": "success",
  "message": "Usuario eliminado exitosamente"
}
```

---

## 6️⃣ VER ROLES DEL USUARIO (Usando token del usuario)

### Request:
```http
GET http://localhost:8081/test/roles
Authorization: Bearer USER_TOKEN_HERE
```

### Response (200 OK):
```json
["medico", "paciente"]
```

---

## OBTENER TOKEN DE USUARIO EN KEYCLOAK

### Request:
```http
POST http://localhost:8080/realms/piedraAzul-dev/protocol/openid-connect/token
Content-Type: application/x-www-form-urlencoded

grant_type=password
&client_id=piedraAzul-dev
&username=juan@ejemplo.com
&password=Juan123!
&client_secret=YOUR_CLIENT_SECRET
```

### Response (200 OK):
```json
{
  "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJrZXlfaWQifQ...",
  "expires_in": 300,
  "refresh_expires_in": 1800,
  "refresh_token": "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJrZXlfaWQifQ...",
  "token_type": "Bearer",
  "not_before_policy": 0,
  "session_state": "a1b2c3d4-e5f6",
  "scope": "openid profile email"
}
```

Usa el `access_token` en los requests que requieren autenticación:
```http
Authorization: Bearer <access_token>
```

---

## 🧪 SCRIPT PARA PRUEBAS BASH

```bash
#!/bin/bash

# Colores
GREEN='\033[0;32m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

BASE_URL="http://localhost:8081/api/users"

echo -e "${BLUE}=== PRUEBA 1: Crear Usuario ===${NC}"
curl -X POST $BASE_URL/create \
  -H "Content-Type: application/json" \
  -d '{
    "email": "testuser@test.com",
    "password": "Test123!",
    "firstName": "Test",
    "lastName": "User",
    "roles": ["medico"]
  }'

echo -e "\n\n${BLUE}=== PRUEBA 2: Buscar Usuario ===${NC}"
# Reemplaza YOUR_ADMIN_TOKEN con un token real
curl -X GET "$BASE_URL/search?username=testuser@test.com" \
  -H "Authorization: Bearer YOUR_ADMIN_TOKEN"

echo -e "\n\n${GREEN}=== Pruebas Completadas ===${NC}"
```

---

## 📝 IMPORTAR EN POSTMAN

1. Crea una colección nueva en Postman
2. Crea 5 requests:
   - **POST /api/users/create**
   - **GET /api/users/all**
   - **GET /api/users/search**
   - **POST /api/users/{userId}/assign-client-roles**
   - **DELETE /api/users/{userId}**
3. Usa los ejemplos anteriores
4. Guarda variables de entorno:
   - `BASE_URL`: http://localhost:8081
   - `ADMIN_TOKEN`: Tu token de admin
   - `USER_ID`: ID de usuario para pruebas

---

## ⚠️ NOTAS IMPORTANTES

1. El endpoint `/api/users/create` **NO requiere autenticación**
2. Los demás endpoints **SÍ requieren token y rol admin**
3. Reemplaza `YOUR_ADMIN_TOKEN` con un token real de un usuario con rol admin
4. Reemplaza `YOUR_CLIENT_SECRET` con el cliente secret de Keycloak
5. Los IDs de usuario cambian según tu Keycloak

---

## 🔧 VARIABLES ÚTILES PARA POSTMAN

```
{
  "base_url": "http://localhost:8081",
  "keycloak_url": "http://localhost:8080",
  "realm": "piedraAzul-dev",
  "client_id": "piedraAzul-dev",
  "admin_username": "amendeza",
  "admin_password": "1234",
  "test_user_email": "test@test.com",
  "test_user_password": "Test123!"
}
```

---

## ✅ CHECKLIST ANTES DE PROBAR

- [ ] Keycloak está corriendo en `localhost:8080`
- [ ] El realm `piedraAzul-dev` existe
- [ ] El cliente `piedraAzul-dev` existe
- [ ] La aplicación está corriendo en `localhost:8081`
- [ ] Tienes un token de admin válido
- [ ] Los puertos no están ocupados

---

## 🎯 FLUJO RECOMENDADO

1. Crear usuario: `/api/users/create`
2. Obtener token del usuario: `POST /realms/.../token`
3. Ver roles del usuario: `/test/roles`
4. Acceder a endpoint protegido: `/test/admin-only2`
5. Listar usuarios: `/api/users/all` (con token admin)
6. Eliminar usuario: `/api/users/{userId}` (con token admin)

