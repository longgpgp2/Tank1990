package tank1990.common.classes;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;

public class GameSprite {
  private String imageSource;
  private BufferedImage bufferedImage;

  public GameSprite(String imageSource) {
    this.imageSource = imageSource;
    this.bufferedImage = getBufferedImage();
  }

  public BufferedImage getBufferedImage() {
    try {
      if (bufferedImage != null) {
        return bufferedImage;
      }

      if (imageSource.length() == 0) {
        return null;
      }

      return ImageIO.read(new File(imageSource));
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  public void setImageSource(String imageSource) {
    this.imageSource = imageSource;
  }

  public String getImageSource() {
    return this.imageSource;
  }

  /**
   * Create white rectangle representing gray scale masking for sprite
   */
  public static BufferedImage createdGrayScaleMask(int x, int y, int width, int height, GameSprite sprite) {
    if (sprite == null) {
      return null;
    }

    int spriteWidth = sprite.bufferedImage.getWidth();
    int spriteHeight = sprite.bufferedImage.getHeight();

    BufferedImage maskImage = new BufferedImage(spriteWidth, spriteHeight, BufferedImage.TYPE_INT_RGB);
    int[] maskImageRGB = new int[spriteWidth * spriteHeight];

    Arrays.fill(maskImageRGB, new Color(255, 255, 255).getRGB());

    maskImage.setRGB(x, y, width, height, maskImageRGB, 0, 0);

    return maskImage;
  }

  /**
   * Masking image with mask of white area and remove image pixel of black area
   * <p>
   * Reference:
   * https://stackoverflow.com/questions/221830/set-bufferedimage-alpha-mask-in-java
   */
  public void applyGrayscaleMaskToAlpha(BufferedImage mask) {
    if (bufferedImage == null) {
      return;
    }

    int width = bufferedImage.getWidth();
    int height = bufferedImage.getHeight();

    int[] imagePixels = bufferedImage.getRGB(0, 0, width, height, null, 0, width);
    int[] maskPixels = mask.getRGB(0, 0, width, height, null, 0, width);

    for (int i = 0; i < imagePixels.length; i++) {

      // Mask preexisting alpha
      // (Basically turn every pixel opacity into 0 since binary bit 24 to 31 which is
      // alpha channel are 0 along with bitwise AND)
      // 0x00ffffff: white with 0 opacity (new Color(255, 255, 255, 0).getRGB())
      // 0x00ffffff: 00000000111111111111111111111111 (HEX to BIN)
      int color = imagePixels[i] & 0x00ffffff;

      // Shift blue to alpha: alpha now has blue value and everything else is set to 0
      // Only alpha has value
      // if blue = 255 (white), alpha has max opacity
      // if blue = 0 (black), alpha has 0 opacity
      // (alpha-red-green-blue)
      int alpha = maskPixels[i] << 24;

      // Basically toggle opacity. If white => visible. If black => not visible
      imagePixels[i] = color | alpha;
    }

    bufferedImage.setRGB(0, 0, width, height, imagePixels, 0, width);
  }
}
