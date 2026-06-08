package turquia.util;

import java.awt.Color;
import java.awt.Font;

public class Palette {
    // Colores principales
    public static final Color AMARILLO    = new Color(0xF5A800);
    public static final Color AMARILLO_OS = new Color(0xD48F00);
    public static final Color NAVY        = new Color(0x1A2744);
    public static final Color NAVY_LIGHT  = new Color(0x253560);
    public static final Color BLANCO      = Color.WHITE;
    public static final Color GRIS_FONDO  = new Color(0xF4F6FA);
    public static final Color VERDE_BTN   = new Color(0x26A69A);
    public static final Color VERDE_HOVER = new Color(0x1D8A80);
    public static final Color TEXTO_GRIS  = new Color(0x5A6680);
    public static final Color NARANJA_PT  = new Color(0xE65C00);

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
