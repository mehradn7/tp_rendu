
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.ImageIO;

/**
 * 2D Texture class.
 */
public class Texture {
    int width;
    int height;
    BufferedImage image;

    /**
     * Constructs a new Texture with the content of the image at @path.
     */ 
    public Texture (String path) throws Exception {
        image = ImageIO.read (new File (path));
        width = image.getWidth ();
        height = image.getHeight ();
    }

    /**
     * Samples the texture at texture coordinates (u,v), using nearest neighbor interpolation
     * u and v and wrapped around to [0,1].
     */ 
    public Color sample (double u, double v) {


	/* à compléter */
    	int couleur = image.getRGB((int) (u*width)%width, (int) (v*height)%height);
    	
        return new Color (couleur);
    }
}
