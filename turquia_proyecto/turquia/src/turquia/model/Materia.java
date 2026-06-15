package turquia.model;

public class Materia {
    private int id;
    private String nombre;
    private String sigla;

    public Materia(int id, String nombre, String sigla) {
        this.id = id;
        this.nombre = nombre;
        this.sigla = sigla;
    }

    public int getId()       { return id; }
    public String getNombre() { return nombre; }
    public String getSigla()  { return sigla; }

    @Override
    public String toString() {
        return nombre;
    }
}
