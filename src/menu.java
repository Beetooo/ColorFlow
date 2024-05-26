import javax.swing.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.*;

public class menu implements ActionListener, ItemListener {
    static JFrame menu;
    static String[][] theme = {
        {"#232B2D", "#0D1B1E", "#FFF7FA", "#dc143c", "#228b22", "#cc5500", "#87ceeb", "#c2b280","#6f42c1","#ffd700","#d2691e","#afafe0","#ffffdd"},
        {"#CBC5A8", "#FFF8DC", "#FFDA36", "#e91e63", "#ffc048", "#20c997", "#a6ff00", "#003580","#ff0080","#333333","#f28030","#40e0d0","#ff00ff"},
        {"#5D5882", "#3A3561", "#000000", "#007bff", "#00ff00", "#ff0080", "#ffa500", "#7f3c86","#a6ff00","#000000","#ffff00","#e91e63","#ff0000"}
    };
    static int selectedTheme=0;
    static JComboBox<String> themes;
    // Initializing fonts
    static Font font1 = new Font("Arial", Font.PLAIN, 30);
    static Font font2 = new Font("Arial", Font.PLAIN, 20);
    static Font font3 = new Font("Arial", Font.BOLD, 30);

    public static void main(String[] args) {
        app();
    }
    public static void app(){
        ImageIcon logo = new ImageIcon("lib\\colorFlowLogo.png");
        menu = new JFrame("Color Flow");
        JLabel title = new JLabel("colorFlow");
        title.setIcon(logo);
        JButton easyMode = new JButton("Easy");
        JButton mediumMode = new JButton("Medium");
        JButton hardMode = new JButton("Hard");
        String[] colorOptions = {"Theme 1", "Theme 2", "Theme 3" };
        themes = new JComboBox<String>(colorOptions);
        themes.setSelectedIndex(selectedTheme);

        easyMode.setBounds(250, 280, 100, 40);
        easyMode.setBackground(Color.decode(theme[selectedTheme][3]));
        easyMode.setForeground(Color.decode(theme[selectedTheme][2]));
        mediumMode.setBounds(480, 280, 100, 40);
        mediumMode.setBackground(Color.decode(theme[selectedTheme][3]));
        mediumMode.setForeground(Color.decode(theme[selectedTheme][2]));
        hardMode.setBounds(710, 280, 100, 40);
        hardMode.setBackground(Color.decode(theme[selectedTheme][3]));
        hardMode.setForeground(Color.decode(theme[selectedTheme][2]));
        title.setBounds(280, 10, 505, 200);
        themes.setBounds(950, 620, 100, 40);
        themes.setBackground(Color.decode(theme[selectedTheme][3]));
        themes.setForeground(Color.decode(theme[selectedTheme][2]));

        menu Menu = new menu(); // Create an instance of menu to use non-static methods
        easyMode.addActionListener(Menu);
        mediumMode.addActionListener(Menu);
        hardMode.addActionListener(Menu);
        themes.addItemListener(Menu);

        // Adding components to menu
        menu.add(easyMode);
        menu.add(mediumMode);
        menu.add(hardMode);
        menu.add(themes);
        menu.add(title);

        // Set window properties
        menu.getContentPane().setBackground(Color.decode(theme[selectedTheme][1]));
        menu.setLayout(null);
        menu.setSize(1080, 720);
        menu.setLocationRelativeTo(null);
        menu.setVisible(true);
        menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menu.setResizable(false);
    }

    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton) e.getSource();

        if (button.getText().equals("Easy")) {
            flow.game(5, theme[themes.getSelectedIndex()]);
            menu.dispose();
        } 
        else if (button.getText().equals("Medium")) {
            flow.game(8, theme[themes.getSelectedIndex()]);
        } 
        else if (button.getText().equals("Hard")) {
            flow.game(12, theme[themes.getSelectedIndex()]);
            menu.dispose();
        }
    }
    public void itemStateChanged(ItemEvent e){
        // if the state combobox is changed
        if (e.getSource() == themes) {
            menu.dispose();
            selectedTheme=themes.getSelectedIndex();
            app();
        }
    }
}
