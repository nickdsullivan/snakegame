package snake;

import javax.swing.JFrame;

public class GameFrame extends JFrame {

    GameFrame() {
        
        this.add(new Display());
        this.setTitle("Snake");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}
