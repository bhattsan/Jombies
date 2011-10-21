import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
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
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import org.jombie.common.Vector;
import org.jombie.projectile.PistolBullet;
import org.jombie.projectile.Projectile;
import org.jombie.unit.Unit;
import org.jombie.unit.marines.Marine;
import org.jombie.weapon.RangedWeapon;
import org.jombie.weapon.Weapon;

public class MapView extends JFrame {
	protected static final int NUM_BUFFERS = 100;

	public static void main(String[] args) {
		MapView s = new MapView();
	}

	MapView() {
		setSize(800, 800);
		GridLayout gridL = new GridLayout(2, 1);
		setLayout(new GridLayout(2, 1));

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setTitle("Game");
		setResizable(false);

		setVisible(true);
		PanelTest s = new PanelTest();
		s.setBounds(0, 50, 800, 700);
		NotificationBar bar = new NotificationBar();
		bar.setBounds(0, 0, 800, 40);
		bar.add(s.healthBar);
		AmmunitionBar ammoBar = new AmmunitionBar(s.bulletImg,
				(RangedWeapon) s.myUnit.myWeapon);
		ammoBar.setBounds(500, 0, 300, 40);
		bar.add(ammoBar);
		add(bar);
		add(s);

		addKeyListener(s);
		s.healthBar.setVisible(true);
		requestFocus();
	}
}

class PanelTest extends JPanel implements KeyListener, FocusListener,
		MouseMotionListener, ImageObserver, MouseListener {
	private static final int POKEZERO = -65794;
	private static final int _sizeX = 800;
	private static final int _sizeY = 700;
	private static int _meRadius = 20;
	private static int _gunRadius = 15;
	private static int _gunSize = 10;
	private static final int RIGHT = 3;
	private static final int LEFT = 2;
	private static final int UP = 1;
	private static final int DOWN = 0;
	private static int SPEED = 10;
	private static int personX = _sizeX / 2, personY = _sizeY / 2;
	private Color map = Color.WHITE;
	private Vector mousePoint = new Vector();

	public Unit myUnit;
	public Vector myLocation;
	private List<Projectile> projectiles;
	public HealthBar healthBar;

	int x = 0, y = 0;
	private String craft1 = "TestMap2.png";

	BufferedImage actual;
	private String explosion = "Explode.gif";
	private String bullet = "Bullet.png";
	BufferedImage plane, expl, bulletImg;
	boolean explode = false;
	double weaponAngle = 0;

	byte direction = 0; // 0 -down, 1-right, 2-up, 3-left
	private Set<Integer> pressedKeys;

	int countUIThread = 0;

	public boolean imageUpdate(Image img, int flags, int x, int y, int w, int h) {

		repaint();
		return true;
	}

	public int getHeight() {
		return _sizeY;
	};

	public int getWidth() {
		return _sizeX;
	};

	PanelTest() {

		
		setBackground(Color.BLACK);
		myUnit = new Marine();
		myLocation = new Vector(){
			public double getxCoord() {return x+personX;};
			public double getyCoord() {return y+personY;};
		};
		myUnit.setLocation(myLocation);
		healthBar = new HealthBar(myUnit);
		_gunSize = ((RangedWeapon) myUnit.myWeapon).getRadius();
		_meRadius = myUnit.getSize();
		SPEED = myUnit.getSpeed();
		// _gunRadius
		healthBar.setBounds(0, 0, 800, 50);
		pressedKeys = new LinkedHashSet<>();
		ImageIcon ii = null;

		try {
			ii = new ImageIcon(ImageIO.read(new File(craft1)));
			actual = ImageIO.read(new File(craft1));
			expl = (BufferedImage) new ImageIcon(ImageIO.read(new File(
					explosion))).getImage();
			bulletImg = ImageIO.read(new File(bullet));
		} catch (IOException e) {
			e.printStackTrace();
		}
		plane = (BufferedImage) ii.getImage();

		projectiles = new ArrayList<Projectile>();

		setDoubleBuffered(true);
		setFocusable(true);

		Timer s = new Timer(5, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// countUIThread++;
				// countUIThread%=5;
				Vector me = new Vector();
				me.setxCoord(personX);
				me.setyCoord(personY);
				weaponAngle = me.findAngle(mousePoint);
				ArrayList<Projectile> toKill = new ArrayList<Projectile>();
				for (Projectile proj : projectiles) {
					proj.updatePosition();
					if(proj.hasCollided(myUnit)){
						if(proj.getOwner().myTeam!=myUnit.myTeam)
						myUnit.health-=proj.getDamage();
						toKill.add(proj);
					}
					else if (!isValidSpace(proj.getPosition(), proj.getRadius())) {
						toKill.add(proj);
					}
				}
				projectiles.removeAll(toKill);
				// System.out.println(weaponAngle);
				// if(countUIThread%5==0){
				repaint();
				healthBar.repaint();
				// }
			}
		});
		s.start();

		requestFocus();
		addKeyListener(this);
		addFocusListener(this);
		addMouseMotionListener(this);
		addMouseListener(this);
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

	public static boolean isValidSpace(int x, int y, int radius, BufferedImage image) {
		if (x <= 0 || x > image.getWidth() || y <= 0 || y > image.getHeight()) {
			return false;
		}
		for (int i = x; i < x + radius; i++) {
			if (i < image.getWidth()) {
				for (int j = y; j < y + radius; j++) {
					if (j < image.getHeight()) {
						if (image.getRGB(i, j) == Color.black.getRGB())
							return false;
					}
				}
			}
		}
		return true;
	}

	public boolean isValidSpace(Vector position, int radius) {
		return isValidSpace((int) position.getxCoord(),
				(int) position.getyCoord(), radius, actual);
	}

	private void moveLeft() {
		if ((x == 0 && personX > _meRadius / 2)
				|| (x + getWidth() == plane.getWidth() && personX > _sizeX / 2)) {
			if (isValidSpace(x + personX - 1 - _meRadius / 2, y + personY
					- _meRadius / 2, _meRadius, actual))
				personX--;
		} else if (x > 0) {
			if (isValidSpace(x + personX - 1 - _meRadius / 2, y + personY
					- _meRadius / 2, _meRadius, actual))
				x--;
		}
	}

	private void moveUp() {
		if ((y == 0 && personY > _meRadius / 2)
				|| (y + getHeight() == plane.getHeight() && personY > _sizeY / 2)) {
			if (isValidSpace(x + personX - _meRadius / 2, y + personY
					- _meRadius / 2 - 1, _meRadius, actual))
				personY--;
		} else if (y > 0) {
			if (isValidSpace(x + personX - _meRadius / 2, y + personY
					- _meRadius / 2 - 1, _meRadius, actual))
				y--;
		}
	}

	private void moveRight() {
		if ((x == 0 && personX < (_sizeY / 2))
				|| (x + getWidth() == plane.getWidth() && personX + _meRadius
						/ 2 < getWidth())) {
			if (isValidSpace(x + personX + 1 - _meRadius / 2, y + personY
					- _meRadius / 2, _meRadius, actual))
				personX++;
		} else if (x + getWidth() < plane.getWidth()) {
			if (isValidSpace(x + personX + 1 - _meRadius / 2, y + personY
					- _meRadius / 2, _meRadius, actual))
				x++;
		}
	}

	private void moveDown() {
		if ((y == 0 && personY < _sizeY / 2)
				|| (y + getHeight() == plane.getHeight() && y + personY
						+ _meRadius / 2 < plane.getHeight())) {
			if (isValidSpace(x + personX - _meRadius / 2, y + personY + 1
					- _meRadius / 2, _meRadius, actual))
				personY++;
		} else if (y + getHeight() < plane.getHeight()) {
			if (isValidSpace(x + personX - _meRadius / 2, y + personY + 1
					- _meRadius / 2, _meRadius, actual))
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
		int gunX = (int) (_gunRadius * Math.cos(weaponAngle));
		int gunY = (int) (_gunRadius * Math.sin(weaponAngle));
		g.setColor(Color.RED);
		g.fillOval(personX + gunX - _gunSize / 2,
				personY - gunY - _gunSize / 2, _gunSize, _gunSize);

		for (Projectile proj : projectiles) {
			// System.out.println("??");
			g.setColor(Color.BLUE);
			Vector pos = proj.getPosition();
			if (pos.getxCoord() < x + _sizeX && pos.getxCoord() > x
					&& pos.getyCoord() < y + _sizeY && pos.getyCoord() > y) {
				g.fillOval((int) pos.getxCoord() - x,
						(int) pos.getyCoord() - y, proj.getRadius(),
						proj.getRadius());
			}
			// g.fillOval(pos, y, width, height)
		}

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
		mouseClicked(arg0);
	}

	@Override
	public void mouseMoved(MouseEvent mouseHitPoint) {
		int xHit = mouseHitPoint.getX();
		int yHit = mouseHitPoint.getY();
		mousePoint.setxCoord(xHit);
		mousePoint.setyCoord(yHit);
		healthBar.setOpaque(false);
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {

		// explode = true;
		if (myUnit.myWeapon.attack()) {
			int xHit = arg0.getX();
			int yHit = arg0.getY();
			mousePoint.setxCoord(xHit);
			mousePoint.setyCoord(yHit);

			System.err.println("Y no explode?");
			PistolBullet s = new PistolBullet();
			s.setOwner(myUnit);
			Vector pos = new Vector();
			pos.setxCoord(x + personX);
			pos.setyCoord(y + personY);
			Vector me = new Vector();
			me.setxCoord(personX);
			me.setyCoord(personY);
			weaponAngle = me.findAngle(mousePoint);
			Vector direction = new Vector();
			direction.setxCoord((int) (100 * Math.cos(weaponAngle)));
			direction.setyCoord((int) (100 * Math.sin(weaponAngle)));
			pos.addScalarVector(direction, _meRadius / 2 + _gunRadius / 2);
			s.setPosition(pos);
			s.setDirection(direction);
			System.out.println(direction);
			System.out.println(direction.findAngle());
			projectiles.add(s);
		}
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
		mouseClicked(arg0);
	}
}

class NotificationBar extends JPanel {
	public NotificationBar() {
		setLayout(new FlowLayout());
	}
}

class HealthBar extends JPanel {
	Unit myU;
	
	public HealthBar(Unit myUnit){
		myU = myUnit;
	}

	@Override
	public void paint(Graphics g) {
		g.setColor(Color.RED);
		g.fill3DRect(10, 10, myU.getHealthCapacity(), 40, false);
		g.setColor(Color.blue);
		g.fill3DRect(10, 10, myU.getHealth(), 40, false);
		g.setColor(Color.WHITE);
		g.drawString(myU.getHealth() + "%", myU.getHealth() / 2, 40);
	}
}

class AmmunitionBar extends JPanel {
	int clip = 5;
	int capacity = 10;
	BufferedImage bulletImg;
	RangedWeapon myWeapon;

	public AmmunitionBar(BufferedImage bullet, RangedWeapon weaaaaponn) {
		bulletImg = bullet;
		myWeapon = weaaaaponn;
	}

	@Override
	public void paint(Graphics g) {
		clip = myWeapon.getCurrentClip();
		capacity = myWeapon.getCurrentClip();
		g.setColor(Color.BLACK);
		g.fill3DRect(10, 10, capacity * 50, 40, false);
		for (int i = 0; i < clip; i++)
			g.drawImage(bulletImg, i * 10, 10, 10, 30, this);
	}
}
