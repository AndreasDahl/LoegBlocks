package oldView;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

import controller.ArrowKeyListener;

public class MainFrame extends JFrame {
	private static final long serialVersionUID = 4757225177683645439L;
	private static MainFrame instance;
	
	private JPanel mainPanel;
	
	public MainFrame() {
		super();
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.add(GameBoard.getInstance());
		this.setVisible(true);
		this.addKeyListener(new ArrowKeyListener());
		this.pack();
	}
	
	public static MainFrame getInstance() {
		if (instance == null)
			instance = new MainFrame();
		return instance;
	}
}
