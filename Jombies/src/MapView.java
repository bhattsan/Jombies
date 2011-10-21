import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import org.jombie.common.Vector;

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
		MouseMotionListener, ImageObserver, MouseListener {
	private static final int POKEZERO = -65794;
	private static final int _sizeX = 400;
	private static final int _sizeY = 400;
	private static final int _meRadius = 20;
	private static final int _gunRadius = 15;
	private static final int _gunSize = 10;
	private static final int RIGHT = 3;
	private static final int LEFT = 2;
	private static final int UP = 1;
	private static final int DOWN = 0;
	private static final int SPEED = 10;
	private static int personX = _sizeX / 2, personY = _sizeY / 2;
	private Color map = Color.WHITE;
	private Vector mousePoint = new Vector();

	int x = 0, y = 0;
	private String craft1 = "TestMap.png";

	BufferedImage actual;
	private String explosion = "Explode.gif";
	BufferedImage plane, expl;
	boolean explode = false;

	double weaponAngle = 0;

	byte direction = 0; // 0 -down, 1-right, 2-up, 3-left
	private Set<Integer> pressedKeys;

	public boolean imageUpdate(Image img, int flags, int x, int y, int w, int h) {

		repaint();
		return true;
	}

	PanelTest() {
		setBackground(Color.BLACK);
		pressedKeys = new LinkedHashSet<>();
		ImageIcon ii = null;

		try {
			ii = new ImageIcon(ImageIO.read(new File(craft1)));
			actual = ImageIO.read(new File(craft1));
			expl = ImageIO.read(new File(explosion));
		} catch (IOException e) {
			e.printStackTrace();
		}
		plane = (BufferedImage) ii.getImage();

		setDoubleBuffered(true);
		setFocusable(true);
		Timer s = new Timer(10, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Vector me = new Vector();
				me.setxCoord(personX);
				me.setyCoord(personY);
				weaponAngle = me.findAngle(mousePoint);
				// System.out.println(weaponAngle);
				repaint();
			}
		});
		s.start();
		requestFocus();
		addKeyListener(this);
		addFocusListener(this);
		addMouseMotionListener(this);
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		if (arg0.getKeyCode() == KeyEvent.VK_D) {
			pressedKeys.remove(RIGHT);
		} else if (arg0.getKeyCode() == KeyEvent.VK_A) {
			pressedKeys.remove(LEFT);
		} else if (arg0.getKeyCode() == KeyEvent.VK_W) {
			pressedKeys.remove(UP);
		} else if (arg0.getKeyCode() == KeyEvent.VK_S) {
			pressedKeys.remove(DOWN);
		}
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		if (arg0.getKeyCode() == KeyEvent.VK_D) {
			pressedKeys.add(RIGHT);
		} else if (arg0.getKeyCode() == KeyEvent.VK_A) {
			pressedKeys.add(LEFT);
		} else if (arg0.getKeyCode() == KeyEvent.VK_W) {
			pressedKeys.add(UP);
		} else if (arg0.getKeyCode() == KeyEvent.VK_S) {
			pressedKeys.add(DOWN);
		}
	}

	private void move() {
		if (pressedKeys.size() == 1) {
			moveAStep(pressedKeys.iterator().next());
		} else if (pressedKeys.size() > 0) {
			int dirUp = -1;
			int dirLeft = -2;
			for (Integer curr : pressedKeys) {
				if (curr == UP || curr == DOWN)
					dirUp = curr;
				else
					dirLeft = curr;
			}
			moveAStep(dirUp);
			moveAStep(dirLeft);
		}
	}

	private void moveAStep(Integer next) {
		for (int i = 0; i < SPEED; i++) {
			if (next == UP)
				moveUp();
			else if (next == DOWN)
				moveDown();
			else if (next == LEFT)
				moveLeft();
			else if (next == RIGHT)
				moveRight();
		}
	}

	public boolean isFocusTraversable() {
		return true;
	}

	public boolean compareColors(int first, int second) {
		first = first & 0xFFFFFF;
		second = second & 0xFFFFFF;
		return first == second;
	}

	public boolean isValidSpace(int x, int y, int radius) {
		for (int i = x; i < x + radius; i++) {
			if (i < actual.getWidth()) {
				for (int j = y; j < y + radius; j++) {
					if (j < actual.getHeight()) {
						if (actual.getRGB(i, j) == Color.black.getRGB())
							return false;
					}
				}
			}
		}
		return true;
	}

	private void moveLeft() {
		if ((x == 0 && personX > _meRadius / 2)
				|| (x + getWidth() == plane.getWidth() && personX > _sizeX / 2)) {
			if (isValidSpace(x + personX - 1 - _meRadius / 2, y + personY
					- _meRadius / 2, _meRadius))
				personX--;
		} else if (x > 0) {
			if (isValidSpace(x + personX - 1 - _meRadius / 2, y + personY
					- _meRadius / 2, _meRadius))
				x--;
		}
	}

	private void moveUp() {
		if ((y == 0 && personY > _meRadius / 2)
				|| (y + getHeight() == plane.getHeight() && personY > _sizeY / 2)) {
			if (isValidSpace(x + personX - _meRadius / 2, y + personY
					- _meRadius / 2 - 1, _meRadius))
				personY--;
		} else if (y > 0) {
			if (isValidSpace(x + personX - _meRadius / 2, y + personY
					- _meRadius / 2 - 1, _meRadius))
				y--;
		}
	}

	private void moveRight() {
		if ((x == 0 && personX < (_sizeY / 2))
				|| (x + getWidth() == plane.getWidth() && personX + _meRadius
						/ 2 < getWidth())) {
			if (isValidSpace(x + personX + 1 - _meRadius / 2, y + personY
					- _meRadius / 2, _meRadius))
				personX++;
		} else if (x + getWidth() < plane.getWidth()) {
			if (isValidSpace(x + personX + 1 - _meRadius / 2, y + personY
					- _meRadius / 2, _meRadius))
				x++;
		}
	}

	private void moveDown() {
		if ((y == 0 && personY < _sizeY / 2)
				|| (y + getHeight() == plane.getHeight() && y + personY
						+ _meRadius / 2 < plane.getHeight())) {
			if (isValidSpace(x + personX - _meRadius / 2, y + personY + 1
					- _meRadius / 2, _meRadius))
				personY++;
		} else if (y + getHeight() < plane.getHeight()) {
			if (isValidSpace(x + personX - _meRadius / 2, y + personY + 1
					- _meRadius / 2, _meRadius))
				y++;
		}
	}

	@Override
	public void paint(Graphics g) {

		super.paint(g);
		move();
		g.setColor(Color.WHITE);
		g.drawImage(plane, 0, 0, _sizeX, _sizeY, x, y, x + _sizeX, y + _sizeY,
				this);
		g.setColor(Color.GREEN);
		g.fillOval(personX - _meRadius / 2, personY - _meRadius / 2, _meRadius,
				_meRadius);
		int x = (int) (_gunRadius * Math.cos(weaponAngle));
		int y = (int) (_gunRadius * Math.sin(weaponAngle));
		g.setColor(Color.RED);
		g.fillOval(personX + x - _gunSize / 2, personY - y - _gunSize / 2,
				_gunSize, _gunSize);

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
	public void mouseMoved(MouseEvent mouseHitPoint) {
		int xHit = mouseHitPoint.getX();
		int yHit = mouseHitPoint.getY();

		mousePoint.setxCoord(xHit);
		mousePoint.setyCoord(yHit);
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}
}
