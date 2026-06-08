# TURQUIA - Sistema Universitario UAJMS
## Versión trucha de Tariquia 🦃

Aplicación de escritorio Java (Swing) que simula el sistema universitario de la UAJMS.

---

## Estructura del Proyecto

```
turquia/
├── src/
│   └── turquia/
│       ├── Main.java              ← Punto de entrada
│       ├── model/
│       │   └── Usuario.java       ← Entidad de usuario (POO)
│       ├── util/
│       │   ├── AuthService.java   ← Servicio de autenticación
│       │   ├── Palette.java       ← Colores y fuentes
│       │   └── RoundedPanel.java  ← Componente personalizado
│       └── ui/
│           ├── LoginFrame.java    ← Ventana de login
│           ├── MainFrame.java     ← Ventana principal
│           └── InicioPanel.java   ← Panel de bienvenida
├── compilar.bat   ← Ejecutar en Windows
└── compilar.sh    ← Ejecutar en Linux/Mac
```

---

## Cómo compilar y ejecutar

### Requisitos
- **JDK 8 o superior** instalado (no solo JRE)

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

| Usuario | Contraseña | Rol           |
|---------|-----------|---------------|
| diogo   | 1234      | Estudiante    |
| maria   | admin     | Administrador |
| carlos  | profe     | Docente       |

---

## Funcionalidades
- ✅ Login con validación
- ✅ Ventana principal con navbar (Inicio / Estudiantes / Horarios)
- ✅ Panel de inicio con cards de módulos
- ✅ Mostrar nombre y rol del usuario logueado
- ✅ Botón Salir (regresa al login)
- 🔧 Módulos en construcción: Estudiantes, Horarios, Materias...
