import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

//hi
public class Test {
public static void main(String[] args) throws Exception {
	URL url = new URL("http://bestanimations.com/Military/Explosions/Explode-01-june.gif");
    Icon icon = new ImageIcon(url);
    JLabel label = new JLabel(icon);

    JFrame f = new JFrame("Animation");
    f.getContentPane().add(label);
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    f.pack();
    f.setLocationRelativeTo(null);
    f.setVisible(true);
}
}
