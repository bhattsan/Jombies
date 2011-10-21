import java.awt.Color;

import org.jombie.common.Vector;

//hi
public class Test {
public static void main(String[] args) {
	System.out.println(Integer.toHexString(Color.RED.getRGB()));
	Vector t = new Vector();
	t.setxCoord(-2);
	t.setyCoord(0);
	System.out.println(t.findAngle());
}
}
