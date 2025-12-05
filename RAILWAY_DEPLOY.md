# ComicVerse Backend - Despliegue en Railway

## 🚀 Instrucciones de Despliegue

### Paso 1: Preparar el repositorio
El código ya está en GitHub: https://github.com/M4zls/comicverse-backend

### Paso 2: Crear proyecto en Railway

1. Ve a [Railway.app](https://railway.app/)
2. Inicia sesión con GitHub
3. Click en "New Project"
4. Selecciona "Deploy from GitHub repo"
5. Busca y selecciona: `M4zls/comicverse-backend`
6. Railway detectará automáticamente el Dockerfile

### Paso 3: Configurar Variables de Entorno

En la pestaña "Variables" de Railway, NO necesitas agregar nada porque Supabase ya está hardcoded en el código.

**Variables que Railway configura automáticamente:**
- `PORT` - Puerto dinámico (Railway lo asigna automáticamente)

### Paso 4: Desplegar

1. Railway comenzará a construir automáticamente
2. Espera 5-10 minutos para el primer build
3. Una vez desplegado, Railway te dará una URL pública

### Paso 5: Obtener la URL

1. Ve a "Settings" en tu proyecto Railway
2. En "Domains" -> "Generate Domain"
3. Railway generará una URL como: `https://tu-proyecto.up.railway.app`

### Paso 6: Verificar que funciona

Abre en el navegador:
```
https://tu-proyecto.up.railway.app/swagger-ui.html
```

Deberías ver la documentación Swagger de tu API.

### Paso 7: Actualizar el Frontend Android

Una vez tengas la URL de Railway, actualiza en tu app Android:

**Archivo a modificar:** 
`ComicVerse-main/app/src/main/java/com/example/myapplication/data/network/SupabaseClient.kt`

No necesitas cambiar nada en Supabase porque ya está configurado.

Si quisieras usar el backend de Railway en lugar de acceder directamente a Supabase, tendrías que crear un nuevo cliente HTTP en tu app Android que apunte a tu URL de Railway.

---

## 📋 Archivos importantes para Railway

- ✅ `Dockerfile` - Configuración Docker multi-stage
- ✅ `railway.toml` - Configuración específica de Railway
- ✅ `nixpacks.toml` - Configuración alternativa de build
- ✅ `.railwayignore` - Archivos a ignorar en deploy

---

## 🔧 Comandos útiles

### Probar localmente con Docker:
```bash
docker build -t comicverse-backend .
docker run -p 8080:8080 comicverse-backend
```

### Ver logs en Railway:
- Ve a tu proyecto en Railway
- Click en "Deployments"
- Click en el deployment activo
- Verás los logs en tiempo real

---

## 🌐 Endpoints disponibles

Una vez desplegado, tu API estará disponible en:

- **Swagger UI:** `https://tu-url.railway.app/swagger-ui.html`
- **API Docs JSON:** `https://tu-url.railway.app/api-docs`
- **Productos:** `https://tu-url.railway.app/api/mangas`
- **Usuarios:** `https://tu-url.railway.app/api/users`
- **Pedidos:** `https://tu-url.railway.app/api/orders`

---

## 🐛 Troubleshooting

### Build falla:
- Verifica que el Dockerfile esté en la raíz
- Revisa los logs de Railway para ver el error específico

### App no inicia:
- Verifica que `server.port=${PORT:8080}` esté en `application.properties`
- Railway necesita que la app use la variable de entorno `PORT`

### 502 Bad Gateway:
- La app puede estar iniciando, espera 1-2 minutos
- Verifica logs para ver si hay errores de Java

---

## ✅ Checklist de Despliegue

- [x] Dockerfile configurado con Java 21
- [x] application.properties con puerto dinámico
- [x] railway.toml creado
- [x] .railwayignore creado
- [x] Swagger configurado
- [x] CORS habilitado con `@CrossOrigin(origins = ["*"])`
- [ ] Código subido a GitHub
- [ ] Proyecto creado en Railway
- [ ] Deploy exitoso
- [ ] URL generada
- [ ] Swagger funcionando

---

## 📞 Soporte

Si tienes problemas:
1. Revisa los logs en Railway
2. Verifica que Supabase esté accesible
3. Asegúrate que el puerto sea dinámico (${PORT})

**Nota:** Railway tiene un tier gratuito con 500 horas/mes y $5 de crédito gratis.
