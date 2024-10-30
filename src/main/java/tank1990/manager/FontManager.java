package tank1990.manager;

import java.awt.*;
import java.io.InputStream;

public class FontManager {
    private Font customFont;

    public FontManager(String fontPath, float size) {
        loadFont(fontPath, size);
    }

    private void loadFont(String fontPath, float size) {
        try {
            InputStream is = getClass().getResourceAsStream(fontPath);
            if (is != null) {
                customFont = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(size);
                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                ge.registerFont(customFont);
            } else {
                System.out.println("Font not found at path: " + fontPath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Font getCustomFont() {
        return customFont;
    }
}