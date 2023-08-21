import javax.swing.*;
import java.awt.*;

public class GUI {
    private Board board = new Board();

    private JFrame frame = new JFrame("Minesweeper");
    private JTextField textField[][] = new JTextField[Minesweeper.rows][Minesweeper.columns];
    private GridLayout gridLayout = new GridLayout(Minesweeper.rows, Minesweeper.columns);

    GUI() {
        for (int x = 0; x<Minesweeper.rows; x++) {
            for (int y = 0; y<Minesweeper.columns; y++) {
                textField[x][y] = new JTextField();
                gridLayout.addLayoutComponent("", textField[x][y]);
            }
        }
    }

    public void createGUI() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gridBagConstraints = new GridBagConstraints();

        gridBagConstraints.anchor = GridBagConstraints.NORTH;

        JLabel windowName = new JLabel("Minesweeper", JLabel.CENTER);
        windowName.setOpaque(true);

        panel.add(windowName, gridBagConstraints);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(panel);
        frame.setLocationRelativeTo(null);
        frame.setSize(300, 300);
        frame.setResizable(false);
        frame.setVisible(true);
    }
}
