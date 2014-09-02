import controller.EquipController;
import view.EquipGLPanel;
import view.EquipPanel;

import javax.imageio.ImageIO;
import javax.media.opengl.awt.GLJPanel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by youmiss on 8/28/2014.
 */
public class MainFrame extends JFrame {
    private TextPanel textPanel;
    private Toolbar toolbar;
//    private FormPanel formPanel;
    private JFileChooser fileChooser;
    private EquipPanel equipPanel;
    private EquipGLPanel equipGLPanel;

    public MainFrame() {
        super("TeighaDemo");

        setLayout(new BorderLayout());

        Image img = null;
        try {
            img = ImageIO.read(new File("KAQ-0803.png"));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        // Left border is at 230th pixel, the height of the image is 746 pixels
        // the width of the image is 762 - 230 = 532 pixels
        // Diameter of TML 16.0 is 23 pixels
        // 1 unit in the CAD drawing represents 45.8 pixel
        equipPanel = new EquipPanel(img);

        equipPanel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //System.out.println("(" + e.getX() + ", " + e.getY() + ")");
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        EquipController equipController = new EquipController("PIPELINE", "KAQ-0803");
        equipGLPanel = new EquipGLPanel(equipController);
        equipGLPanel.setOpaque(false);

//        if (!Renderer.getAnimator().isAnimating()) {
//            Renderer.getAnimator().resume();
//        }
        //Renderer.getAnimator().start();
        Renderer.getAnimator().add(equipGLPanel);

        toolbar = new Toolbar();
//        textPanel = new TextPanel();
//        formPanel = new FormPanel();
//
        fileChooser = new JFileChooser();
//
//        fileChooser.addChoosableFileFilter(new PersonFileFilter());
//
//        setJMenuBar(createMenuBar());
//
//        toolbar.setStringListener(new StringListener() {
//            public void textEmitted(String text) {
//                textPanel.appendText(text);
//            }
//        });
//
//        formPanel.setFormListener(new FormListener() {
//            public void formEventOccurred(FormEvent e) {
//                String name = e.getName();
//                String occupation = e.getOccupation();
//                int ageCat = e.getAgeCategory();
//                String empCat = e.getEmploymentCategory();
//
//                textPanel.appendText(name + ": " + occupation + ": " + ageCat
//                        + ", " + empCat + "\n");
//                System.out.println(e.getGender());
//            }
//        });
//
//        add(formPanel, BorderLayout.WEST);
//        add(toolbar, BorderLayout.NORTH);
//        add(textPanel, BorderLayout.CENTER);

        setJMenuBar(createMenuBar());
        equipPanel.add(equipGLPanel, BorderLayout.CENTER);
        add(equipPanel, BorderLayout.CENTER);
        //setMinimumSize(new Dimension(500,400));
        setSize(1000, 800);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem openDrawing = new JMenuItem("Open Drawing");
//        JMenuItem importDataItem = new JMenuItem("Import Data...");
        JMenuItem exitItem = new JMenuItem("Exit");

        fileMenu.add(openDrawing);
//        fileMenu.add(importDataItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

//        JMenu windowMenu = new JMenu("Window");
//        JMenu showMenu = new JMenu("Show");

        JCheckBoxMenuItem showFormItem = new JCheckBoxMenuItem("Person From");
        showFormItem.setSelected(true);

//        showMenu.add(showFormItem);
//        windowMenu.add(showMenu);
//
        menuBar.add(fileMenu);
//        menuBar.add(windowMenu);

//        showFormItem.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent ev) {
//                JCheckBoxMenuItem menuItem = (JCheckBoxMenuItem) ev.getSource();
//                formPanel.setVisible(menuItem.isSelected());
//            }
//        });

        fileMenu.setMnemonic(KeyEvent.VK_F);
        exitItem.setMnemonic(KeyEvent.VK_X);

        exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,
                ActionEvent.CTRL_MASK));

//        importDataItem.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                if (fileChooser.showOpenDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION) {
//                    System.out.println(fileChooser.getSelectedFile());
//                }
//            }
//        });

        openDrawing.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (fileChooser.showSaveDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION) {
                    System.out.println(fileChooser.getSelectedFile());
                }
            }
        });

        exitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                int action = JOptionPane.showConfirmDialog(MainFrame.this,
                        "Do you really want to exit the application?",
                        "Confirm Exit", JOptionPane.OK_CANCEL_OPTION);
                if (action == JOptionPane.OK_OPTION) {
                    System.exit(0);
                }
            }
        });

        return menuBar;
    }

    public GLJPanel getCADGLPanel() {
        return equipGLPanel;
    }

}
