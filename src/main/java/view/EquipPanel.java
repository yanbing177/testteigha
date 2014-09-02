package view;

import javax.swing.*;
import java.awt.*;

/**
 * 
 * @author Li Xiang A GLJPanel with background image.
 *
 */
@SuppressWarnings("serial")
public class EquipPanel extends JPanel {

	private Image image;
	
	public EquipPanel() {
		super();
        setLayout(new BorderLayout());
	}
	
	public EquipPanel(Image image) {
		super();
        setLayout(new BorderLayout());
        this.image = image;
	}

	/**
	 * The customized painting process
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (image == null)
			return;

		Dimension d = this.getSize();
		Image image = this.image.getScaledInstance(-1, d.height, Image.SCALE_SMOOTH);

		g.drawImage(image, (d.width  - image.getWidth(null)) / 2, 0, null);
	}
	
	public void setImage(Image image) {
		this.image = image;
		repaint();
	}

}
