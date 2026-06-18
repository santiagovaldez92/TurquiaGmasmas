package turquia.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Reporte {
    private static int contador = 1;

    private final int id;
    private final String autor;
    private final String asunto;
    private final String mensaje;
    private final LocalDateTime fecha;

    public Reporte(String autor, String asunto, String mensaje) {
        this.id = contador++;
        this.autor = autor;
        this.asunto = asunto;
        this.mensaje = mensaje;
        this.fecha = LocalDateTime.now();
    }

    public int getId()           { return id; }
    public String getAutor()     { return autor; }
    public String getAsunto()    { return asunto; }
    public String getMensaje()   { return mensaje; }
    public LocalDateTime getFecha() { return fecha; }

    public String getFechaTexto() {
        return fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }
}
