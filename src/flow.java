import javax.swing.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.*;

public class flow {
    static JFrame window;
    public static void game(int size,String[] theme){




        window=new JFrame("Color Flow");
        JPanel board=new JPanel();
        board.setBounds(300,200,500,400);
        board.setBackground(Color.decode(theme[1]));
        window.add(board);
        /*window.add(mediumMode);
        window.add(hardMode);
        window.add(themes);
        window.add(title);*/

        window.getContentPane().setBackground(Color.decode(theme[0]));
        window.setLayout(null);
        window.setSize(1080,720);
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
