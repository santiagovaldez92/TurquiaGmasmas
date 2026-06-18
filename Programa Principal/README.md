# TURQUIA - Sistema Universitario UAJMS
## Versión trucha de Tariquia 🦃

Aplicación de escritorio Java (Swing) que simula un sistema universitario,
con navegación dinámica según el rol del usuario y paleta cian/magenta sobre guindo oscuro.

---

## Estructura del Proyecto

```
turquia/
├── src/
│   └── turquia/
│       ├── Main.java                  ← Punto de entrada
│       ├── model/
│       │   ├── Usuario.java           ← Entidad de usuario
│       │   ├── Rol.java               ← Enum de roles (Estudiante/Docente/Administrador)
│       │   ├── Materia.java           ← Entidad de materia
│       │   ├── Nota.java              ← Entidad de nota (estudiante-materia)
│       │   └── Reporte.java           ← Entidad de reporte docente -> admin
│       ├── util/
│       │   ├── AuthService.java       ← Servicio de autenticación
│       │   ├── DataStore.java         ← Almacén central de datos (en memoria)
│       │   ├── Palette.java           ← Colores y fuentes
│       │   ├── RoundedPanel.java      ← Componente personalizado
│       │   └── UIFactory.java         ← Fábrica de botones/inputs estilizados
│       └── ui/
│           ├── LoginFrame.java        ← Ventana de login
│           ├── MainFrame.java         ← Ventana principal con navegación por rol
│           ├── InicioPanel.java       ← Dashboard de inicio
│           ├── EstudiantesPanel.java  ← Lista y perfiles de estudiantes
│           ├── DocentesPanel.java     ← Lista y perfiles de docentes
│           ├── HorariosPanel.java     ← Materias del usuario
│           ├── MateriasPanel.java     ← Catálogo de materias
│           ├── ReportesPanel.java     ← Reportes docente -> administración
│           └── UsuariosPanel.java     ← CRUD de usuarios (solo admin)
├── compilar.bat   ← Ejecutar en Windows
└── compilar.sh    ← Ejecutar en Linux/Mac
```

---

## Cómo compilar y ejecutar

### Requisitos
- **JDK 17 o superior** instalado (no solo JRE)

### Windows
```
compilar.bat
```

### Linux / Mac
```bash
chmod +x compilar.sh
./compilar.sh
```

### Manual
```bash
mkdir classes
javac -cp src -d classes $(find src -name "*.java")
java -cp classes turquia.Main
```

---

## Usuarios de prueba

| Usuario   | Contraseña | Rol           |
|-----------|-----------|---------------|
| diogo     | 1234      | Estudiante    |
| ana       | 1234      | Estudiante    |
| luis      | 1234      | Estudiante    |
| carlos    | profe     | Docente       |
| patricia  | profe     | Docente       |
| maria     | admin     | Administrador |

---

## Funcionalidades por rol

### 🎓 Estudiante
- Ve el menú: **Inicio · Estudiantes · Docentes · Horarios**
- En **Estudiantes**: ve la lista completa, puede abrir el perfil de cualquiera,
  pero **solo ve sus propias notas** (las de otros aparecen como "🔒 Nota privada").
- En **Docentes**: ve la lista y las materias que imparte cada docente.
- En **Horarios**: ve sus materias inscritas, el docente asignado y su nota.

### 👨‍🏫 Docente
- Ve el menú: **Inicio · Estudiantes · Horarios · Materias · Reportes**
- En **Estudiantes**: puede ver perfiles, **asignar/quitar materias** a cada
  estudiante y **calificar** (asignar notas 0-100).
- En **Materias**: catálogo completo; puede **auto-asignarse** como docente de una materia.
- En **Horarios**: ve las materias que imparte y cuántos estudiantes tiene en cada una.
- En **Reportes**: redacta y envía informes a la administración; ve los que él mismo envió.

### 🛠 Administrador
- Acceso total: ve el menú: **Inicio · Estudiantes · Docentes · Horarios · Materias · Reportes · Usuarios**
- Todo lo de Docente, más:
- En **Docentes**: puede asignar/quitar materias a cualquier docente.
- En **Reportes**: ve **todos** los informes enviados por los docentes.
- En **Usuarios**: **crear, editar y eliminar** cualquier usuario del sistema
  (estudiantes, docentes y otros administradores).

---

## Materias precargadas
Programación · Estructura de Datos · Cálculo · Álgebra ·
Arquitectura de Computadoras · Investigación Operativa · Inglés

> Nota: las asignaciones (estudiante↔materia, docente↔materia) parten vacías;
> los docentes/admins las configuran desde la app.

---

## Notas técnicas
- Todos los datos (usuarios, materias, notas, reportes) se almacenan en memoria
  mediante el patrón **Singleton** (`DataStore`). Se reinician al cerrar la app.
- El menú superior se construye **dinámicamente** según el `Rol` del usuario logueado.
