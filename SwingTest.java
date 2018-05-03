package feremkae;
import javax.swing.*;

public class SwingTest {
    private static final int BOARD_SIZE = 20;
    private static final int CELL_SIZE = 40;
    private static final int SQUARE_WIND = BOARD_SIZE * CELL_SIZE;
    private static final String PCOLOR = "blue";
    
    private static void buildGUI(){
        JFrame frame = new JFrame("Testing");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        Board b1 = new Board(CELL_SIZE, BOARD_SIZE);
        frame.getContentPane().add(b1);
        frame.setSize(SQUARE_WIND + 32,SQUARE_WIND + 62);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
    }
    
    public static void main(String [] args){
        javax.swing.SwingUtilities.invokeLater(new Runnable(){
            public void run() {
                buildGUI();
            }
        });
    }
    
}
