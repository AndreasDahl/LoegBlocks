package view;

import view.gui.GameView;
import view.gui.GuiComponent;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import javax.swing.JFrame;

import controller.InputHandler;
import view.gui.MainMenu;

public class GameFrame extends Canvas implements Runnable {
	private static final long serialVersionUID = 5761980091712249788L;

	public static final String TITLE = "TETRIS";
	public static final int BLOCK_SCALE = 24;
	public static final int TICKS_PER_SECOND = 60;
	public static final int WIDTH = 528;
	public static final int HEIGHT = 528;
	
	private static JFrame frame;
	private static GameFrame instance;

	private BufferedImage image;
	private int[] pixels;
	private boolean running;
	private Screen screen;
	private GuiComponent activeComponent;
	
	public GameFrame() {
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.addKeyListener(InputHandler.getInstance());
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
	}
	
	public static GameFrame getInstance() {
		if (instance == null)
			instance = new GameFrame();
		return instance;
	}
	
	public void start() {
		running = true;
		setComponent(new MainMenu());
		new Thread(this).start();
	}
	
	public void stop() {
		running = false;
	}
	
	public void init() {
		screen = new Screen(pixels, WIDTH);
	}
	
	@Override
	public void run() {
		long lastTime = System.nanoTime();
		double nsPerTick = 1000000000.0 / TICKS_PER_SECOND;
		double unprocessed = 0;
		int ticks = 0;
		int frames = 0;
		long lastTimer1 = System.currentTimeMillis();
		
		init();
		
		while (running) {
			long now = System.nanoTime();

			unprocessed += (now - lastTime) / nsPerTick;
			lastTime = now;
			boolean shouldRender = true;
			if (unprocessed >= 1) {
				tick();
				ticks++;
				unprocessed -= 1;
				shouldRender = true;
			}
			
			/*try {
				Thread.sleep(0,2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}*/
			
			if (shouldRender) {
				frames++;
				render();
			}
			
			if (System.currentTimeMillis() - lastTimer1 > 1000) {
				lastTimer1 += 1000;
		 		System.out.println(ticks + " ticks, " + frames + " fps");
				frames = 0;
				ticks = 0;
			}
		}
	}
	
	private void tick() {
		activeComponent.tick();
        InputHandler.getInstance().tick();
	}
	
	public void render() {
		BufferStrategy bs = getBufferStrategy();
		
		if (bs == null) {
			createBufferStrategy(3);
			requestFocus();
			return;
		}
		
		activeComponent.render(screen);
		
		Graphics g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		g.dispose();
		bs.show();
	}
	
	public long getTime() {
		return System.nanoTime();
	}
	
	public void setComponent(GuiComponent component) {
		InputHandler.getInstance().clearAll();
        if (activeComponent != null)
            activeComponent.deactivate();
        component.activate();
		activeComponent = component;
	}
	
	public void startGame() {
        GameView gameView = new GameView();
		setComponent(gameView);
		gameView.getBoard().newGame();
	}
 	
	public static void main(String[] args) {
		frame = new JFrame(TITLE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		
		GameFrame game = GameFrame.getInstance();
		frame.add(game);
		
		frame.pack();
		frame.setVisible(true);
		
		game.start();
	}
}
