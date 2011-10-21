import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

//hi
public class Test {
	public static void main(String[] args) throws Exception {
		JFrame f = new JFrame("Animation");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setLocationRelativeTo(null);
		f.setTitle("Game??");
		f.setResizable(true);

		f.setVisible(true);
		PanelTest s = new PanelTest();
		f.add(s);
		f.addKeyListener(s);
		f.setSize(800, 600);
		f.requestFocus();
		HealthBar2 bar2 = new HealthBar2();
		bar2.setOpaque(false);
		f.add(bar2);
		f.setVisible(true);

	}
}

class HealthBar2 extends JPanel {
	int health = 185;
	int capacity = 200;

	@Override
	public void paint(Graphics g) {
		g.setColor(Color.RED);
		g.fill3DRect(10, 10, capacity, 40, false);
		g.setColor(Color.blue);
		g.fill3DRect(10, 10, health, 40, false);
		g.setColor(Color.WHITE);
		g.drawString(health + "%", health / 2, 40);
	}
}