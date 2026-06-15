package modelos;

public class Estudiante extends Usuario {
    private String carrera;

    public Estudiante(String id, String nombre, String password, String carrera) {
        super(id, nombre, password);// Llama al constructor del padre
        this.carrera = carrera;
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    // Sobrescribimos el método para agregar el campo de estudiante
    @Override
    public String toLineaTxt() {
        return super.toLineaTxt() + ";" + carrera;
    }
}