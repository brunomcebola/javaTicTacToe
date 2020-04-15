import java.io.*;
import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.imageio.*;
import java.awt.event.*;
import javax.swing.border.*;

class Global {
    public static JLayeredPane layeredPane;
    public static JPanel gameLayout;
    public static JPanel msg = new JPanel();
    public static JPanel controlLayout;
    public static int size = 400;
}

class Tabuleiro extends JPanel {
    //value or structure will nver be changed
    final private int[][] pos ={{0, 0, 0},{0, 0, 0},{0, 0, 0}};
    final private String imgCrossFilename = "images/cross.gif";
    final private String imgBallFilename = "images/ball.gif";
    final private ArrayList<JButton> buttons = new ArrayList<JButton>();
    //changes over the execution of the program
    private String atual = imgCrossFilename;

    //constructor
    public Tabuleiro() {
        setLayout(new GridLayout(0,3));
        setPreferredSize(new Dimension(Global.size, Global.size));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(Color.white);
        setBounds( 0, 0,  Global.size, Global.size );
        grid(this);
    }

    //blocks game layout
    private void end_game() {
        for(int i=0;i<9;i++) buttons.get(i).setEnabled(false);
	for(int i=0;i<3;i++){
		for(int j=0;j<3;j++){
			pos[i][j] = 0;		
		}	
	}

        JLabel text = new JLabel("VENCEDOR: ");
        text.setFont (text.getFont ().deriveFont (50.0f));

        ImageIcon icon = new ImageIcon(atual);
        JLabel img = new JLabel("", icon, JLabel.CENTER);

        Global.msg = new JPanel();
        Global.msg.setLayout(new BoxLayout(Global.msg, BoxLayout.X_AXIS));
        Global.msg.add(text);
        Global.msg.add(img);
        Global.msg.setOpaque(true);

        int width = (int)Global.msg.getPreferredSize().getWidth();
        int height = (int)Global.msg.getPreferredSize().getHeight() + 50;

        int x = (Global.size-width)/(int)2;
        int y = (Global.size-height)/(int)2;

        Global.layeredPane.add(Global.msg, Integer.valueOf(20));
        Global.msg.setBounds(x, y, width, height);

    }

    //verifies if exists a winner
    private void check() {
        int count = 0;

        for(int i=0; i<3; i++){
            if(pos[i][0] == pos[i][1] && pos[i][1] == pos[i][2] && pos[i][0] != 0)
                end_game();
            else if(pos[0][i] == pos[1][i] && pos[1][i] == pos[2][i] && pos[0][i] != 0)
                end_game();
            else if(pos[0][0] == pos[1][1] && pos[1][1] == pos[2][2] && pos[0][0] != 0)
                end_game();
            else if(pos[0][2] == pos[1][1] && pos[1][1] == pos[2][0] && pos[0][2] != 0)
                end_game();
        }
    }

    //creates the buttons
    private void button(int u, int l, int d, int r, Color color, int x, int y) {
        JButton button = new JButton();
        button.setBackground(Color.white);
        button.setBorder(BorderFactory.createMatteBorder(u, l, d, r, color));
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    Image img = ImageIO.read(getClass().getResource(atual));
                    button.setIcon(new ImageIcon(img));
                }
                catch(IOException i) {}
                button.setFocusPainted(false);
                button.setContentAreaFilled(false);
                if(atual == imgCrossFilename) {
                    pos[x][y] = 1;
                    check();
                    atual = imgBallFilename;
                }
                else {
                    pos[x][y] = -1;
                    check();
                    atual = imgCrossFilename;
                }
                button.removeActionListener(this);
            }
        });
        buttons.add(button);
    }

    //creates the layout
    private void grid(JPanel pane1) {
        button(0, 0, 2, 2, Color.black, 0, 0);
        button(0, 2, 2, 2, Color.black, 0, 1);
        button(0, 2, 2, 0, Color.black, 0, 2);
        button(2, 0, 2, 2, Color.black, 1, 0);
        button(2, 2, 2, 2, Color.black, 1, 1);
        button(2, 2, 2, 0, Color.black, 1, 2);
        button(2, 0, 0, 2, Color.black, 2, 0);
        button(2, 2, 0, 2, Color.black, 2, 1);
        button(2, 2, 0, 0, Color.black, 2, 2);

        for(int i=0;i<9;i++){pane1.add((JButton)buttons.get(i));}
    }
}

class Controller extends JPanel {
    public Controller() {
        setLayout(new GridLayout(0,1));
        setPreferredSize(new Dimension(Global.size, Global.size / 4));
        setBorder(new EmptyBorder(1, 1, 1, 1));
        setBackground(Color.black);

        JButton button = new JButton();
        button.setText("Recomeçar");
        button.setFont(new Font("Arial", Font.PLAIN, 40));
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Global.layeredPane.remove(Global.gameLayout);
                Global.layeredPane.remove(Global.msg);
                Global.gameLayout = new Tabuleiro();
                Global.layeredPane.add(Global.gameLayout);
                Global.layeredPane.revalidate();
                Global.layeredPane.repaint();
            }
        });

        add(button);
    }
}

public class Test extends JFrame {
    public Test(String name) {
        super(name);
        setResizable(false);
    }

    private void addComponentsToPane(final Container pane) {

        Global.gameLayout = new Tabuleiro();
        Global.controlLayout = new Controller();


        Global.layeredPane = new JLayeredPane();
        Global.layeredPane.setPreferredSize(new Dimension(Global.size, Global.size));
        Global.layeredPane.add(Global.gameLayout, Integer.valueOf(1));
        Global.layeredPane.add(Global.msg, Integer.valueOf(20));


        //Add to pane
        final JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.add(Global.layeredPane);
        container.add(Global.controlLayout);

        pane.add(container);
    }

    private static void createAndShowGUI() {
        //Create and set up the window.
        Test frame = new Test("TicTacToe");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Set up the content pane.
        frame.addComponentsToPane(frame.getContentPane());
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        /* Use an appropriate Look and Feel */
        try {
            //UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        /* Turn off metal's use of bold fonts */
        UIManager.put("swing.boldMetal", Boolean.FALSE);

        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
