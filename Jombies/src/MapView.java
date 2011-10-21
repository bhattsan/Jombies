import java.awt.Color;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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

class PanelTest extends JPanel implements KeyListener, FocusListener {
	private static final int POKEZERO = -65794;
	private static final int _sizeX = 400;
	private static final int _sizeY = 400;
	private boolean inPlace = false;
	int x = 0, y = 0;
	private String craft1 = "TestMap.png";
	BufferedImage plane;

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

//		setDoubleBuffered(true);
//		setFocusable(true);
		Timer s = new Timer(2, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (!inPlace) {
					if (isLeftWhite()&&isRightWhite()) {
						direction = 1; // right
						moveRight();
						moveDown();
						System.out.println("??");
					} 
					else if(isLeftWhite()){
						moveRight();
					}
					else if (isRightWhite()) {
						direction = 3; // left
						moveLeft();
					} else if (isBottomWhite()) {
						direction = 2; // up
						moveUp();
					} else if (isTopWhite()) {
						direction = 0; // down
						moveDown();
					} else {
						inPlace = true;
						System.out.println("I'm in place");
						direction = 0;
					}
				} else {
//					System.out.println("HEROO");
					if (y - 1 <= 0) {
						direction = 0;
					} else if (x - 1 <= 0) {
						direction = 1;
					}
					if (direction == 0) {
						moveDown();
//						System.out.println("Down");
					} else if (direction == 1) {
						moveRight();
//						System.out.println("Right");
					} else if (direction == 2) {
						moveUp();
//						System.out.println("Up");
					} else if (direction == 3) {
						moveLeft();
//						System.out.println("Left");
					}
				}
				repaint();
			}
		});
//		s.start();
		requestFocus();
		addKeyListener(this);
		addFocusListener(this);
	}
	
	@Override
	public void keyTyped(KeyEvent arg0) {
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		if (arg0.getKeyCode() == KeyEvent.VK_D) {
			moveRight();
		} else if (arg0.getKeyCode() == KeyEvent.VK_A) {
			x-=10;
		} else if (arg0.getKeyCode() == KeyEvent.VK_W) {
			y-=10;
		} else if (arg0.getKeyCode() == KeyEvent.VK_S) {
			y += 10;
			System.out.println("HI");
		}
		System.out.println("HI?");
		repaint();
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		if (arg0.getKeyCode() == KeyEvent.VK_D) {
			x+=10;
		} else if (arg0.getKeyCode() == KeyEvent.VK_A) {
			x-=10;
		} else if (arg0.getKeyCode() == KeyEvent.VK_W) {
			y-=10;
		} else if (arg0.getKeyCode() == KeyEvent.VK_S) {
			y += 10;
			System.out.println("HI");
		}
		System.out.println("HI?");
		repaint();
	}
	
	public boolean isFocusTraversable() {
	    return true;
	  }
	

	private void moveLeft() {
		if (x > 0) {
			if (inPlace&&x > _sizeX && isLeftWhite()) {
				direction = 0;
				System.out.println("Going down now");
			} else {
				x--;
			}
		} else {
			direction = 0;
		}
	}

	private void moveUp() {
		if (y > 0) {
			if (inPlace&&y > _sizeY && isTopWhite()) {
				direction = 3;
				System.out.println("Going left now");
			} else {
				y--;
			}
		} else {
			direction = 3;
		}
	}

	private void moveRight() {
		if (x < plane.getWidth()) {
			if (inPlace&&isRightWhite()) {
				direction = 2;
//				System.out.println("Going up now");
			} else {
				x++;
			}
			x++;
		} else {
			direction = 2;
		}
	}

	private void moveDown() {
		if (y < plane.getHeight()) {
			if (inPlace&&isBottomWhite()) {
				direction = 1;
				System.out.println("Going right now");
			} else {
				y++;
			}
		} else {
			direction = 1;
		}
	}


	public boolean isLeftWhite() {
		for (int j = 0; j <= _sizeY; j++) {
			if (isZero(plane.getRGB(x-(inPlace?1:0), y + j))) {
				return true;
			}
		}
		return false;
	}

	public boolean isRightWhite() {
		for (int j = 0; j <= _sizeY; j++) {
			if (isZero(plane.getRGB(x + _sizeX+(inPlace?1:0), y + j))) {
				return true;
			}
		}
		return false;
	}

	public boolean isTopWhite() {
		for (int i = 0; i <= _sizeX; i++) {
			if (isZero(plane.getRGB(x + i, y-((inPlace?1:0))))) {
				return true;
			}
		}
		return false;
	}

	public boolean isBottomWhite() {
		for (int i = 0; i <= _sizeX; i++) {
			if (isZero(plane.getRGB(x + i, y + _sizeY+(inPlace?1:0)))) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(Color.WHITE);
		g.drawImage(plane, 0, 0, _sizeX, _sizeY, x, y, x + _sizeX, y + _sizeY,
				this);

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
}