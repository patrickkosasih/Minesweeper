package misc;

import javax.swing.*;
import java.awt.*;

public class ScaledImageIcon extends ImageIcon {

    public ScaledImageIcon(String filename,float scale) {
        this(new ImageIcon(filename),scale);
    }

    public ScaledImageIcon(ImageIcon unscaled,float scale) {
        this(unscaled,(int) (unscaled.getIconWidth() * scale),(int) (unscaled.getIconHeight() * scale));
    }

    public ScaledImageIcon(ImageIcon unscaled,int width,int height) {
        super(unscaled.getImage().getScaledInstance(width,height,Image.SCALE_SMOOTH));
    }
}
