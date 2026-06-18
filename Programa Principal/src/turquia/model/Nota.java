package turquia.model;

public class Nota {
    private int idEstudiante;
    private int idMateria;
    private double valor; // 0 - 100

    public Nota(int idEstudiante, int idMateria, double valor) {
        this.idEstudiante = idEstudiante;
        this.idMateria = idMateria;
        this.valor = valor;
    }

    public int getIdEstudiante() { return idEstudiante; }
    public int getIdMateria()    { return idMateria; }
    public double getValor()     { return valor; }
    public void setValor(double valor) { this.valor = valor; }

    public String getEstado() {
        return valor >= 51 ? "Aprobado" : "Reprobado";
    }
}
