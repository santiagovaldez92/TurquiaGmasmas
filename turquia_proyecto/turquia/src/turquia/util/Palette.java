package turquia.util;

import java.awt.Color;
import java.awt.Font;

public class Palette {
    // ── Paleta principal: Cian + Magenta sobre guindo oscuro ────────────
    public static final Color GUINDO_OSCURO  = new Color(0x1A0A14); // fondo principal
    public static final Color GUINDO         = new Color(0x2A0F1F); // paneles/header
    public static final Color GUINDO_CLARO   = new Color(0x3D1830); // hover / cards
    public static final Color GUINDO_BORDE   = new Color(0x4A2038); // bordes sutiles

    public static final Color CIAN           = new Color(0x00E5FF);
    public static final Color CIAN_OSCURO    = new Color(0x00B8CC);
    public static final Color MAGENTA        = new Color(0xFF1493);
    public static final Color MAGENTA_OSCURO = new Color(0xD4106F);

    public static final Color BLANCO         = Color.WHITE;
    public static final Color TEXTO_CLARO    = new Color(0xE8DCE4);
    public static final Color TEXTO_GRIS     = new Color(0xA899A6);
    public static final Color TEXTO_TENUE    = new Color(0x7A6B76);

    public static final Color EXITO          = new Color(0x00E5A0);
    public static final Color ERROR          = new Color(0xFF4569);
    public static final Color ADVERTENCIA    = new Color(0xFFB300);

    // Aliases para compatibilidad con código previo
    public static final Color AMARILLO    = MAGENTA;
    public static final Color AMARILLO_OS = MAGENTA_OSCURO;
    public static final Color NAVY        = GUINDO;
    public static final Color NAVY_LIGHT  = GUINDO_CLARO;
    public static final Color GRIS_FONDO  = GUINDO_OSCURO;
    public static final Color VERDE_BTN   = CIAN;
    public static final Color VERDE_HOVER = CIAN_OSCURO;
    public static final Color NARANJA_PT  = MAGENTA;

    // Fuentes
    public static Font fontTitle(float size) {
        return new Font("SansSerif", Font.BOLD, (int) size);
    }
    public static Font fontBold(float size) {
        return new Font("SansSerif", Font.BOLD, (int) size);
    }
    public static Font fontPlain(float size) {
        return new Font("SansSerif", Font.PLAIN, (int) size);
    }
}
