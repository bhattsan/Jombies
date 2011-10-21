import java.awt.Color;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class MapView extends JFrame {
	protected static final int NUM_BUFFERS = 100;

	public static void main(String[] args) {
		MapView s = new MapView();
	}

	MapView() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setTitle("Game");
		setResizable(true);

		setVisible(true);
		PanelTest s = new PanelTest();
		add(s);
		addKeyListener(s);
		setSize(400, 400);
		requestFocus();
	}
}

class PanelTest extends JPanel implements KeyListener, FocusListener,
		MouseMotionListener {
	private static final int POKEZERO = -65794;
	private static final int _sizeX = 400;
	private static final int _sizeY = 400;
	private static final int _meRadius = 20;
	private static final int _gunRadius = 5;
	private static int personX = _sizeX/2, personY = _sizeY/2;
	private boolean inPlace = false;
	int x = 0, y = 0;
	private String craft1 = "TestMap.png";
	BufferedImage plane;

	double weaponAngle = 0;

	byte direction = 0; // 0 -down, 1-right, 2-up, 3-left

	public boolean isZero(int RGB) {
		return (RGB == POKEZERO) || RGB == -1;
	}

	PanelTest() {
		setBackground(Color.BLACK);
		ImageIcon ii = null;
		try {
			ii = new ImageIcon(ImageIO.read(new File(craft1)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		plane = (BufferedImage) ii.getImage();

		setDoubleBuffered(true);
		setFocusable(true);
		Timer s = new Timer(2, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				repaint();
			}
		});
		// s.start();
		requestFocus();
		addKeyListener(this);
		addFocusListener(this);
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		if (arg0.getKeyCode() == KeyEvent.VK_D) {
			moveRight();
		} else if (arg0.getKeyCode() == KeyEvent.VK_A) {
			moveLeft();
		} else if (arg0.getKeyCode() == KeyEvent.VK_W) {
			moveUp();
		} else if (arg0.getKeyCode() == KeyEvent.VK_S) {
			moveDown();
		}
		repaint();
	}

	public boolean isFocusTraversable() {
		return true;
	}

	private void moveLeft() {
		if (x > 0) {
			x--;
		}
	}

	private void moveUp() {
		if (y > 0) {
			y--;
		}
	}

	private void moveRight() {
		if (x < plane.getWidth()) {
			x++;
		}
	}

	private void moveDown() {
		if (y < plane.getHeight()) {
			y++;
		}
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(Color.WHITE);
		g.drawImage(plane, 0, 0, _sizeX, _sizeY, x, y, x + _sizeX, y + _sizeY,
				this);
		g.setColor(Color.GREEN);
		g.fillOval(personX, personY, _meRadius, _meRadius);

		Toolkit.getDefaultToolkit().sync();
		g.dispose();
	}

	@Override
	public void focusGained(FocusEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void focusLost(FocusEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		int x = arg0.getX();
		int y = arg0.getY();
		weaponAngle = Math.atan(y / x);
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}