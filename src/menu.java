import javax.swing.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.*;

public class menu implements ActionListener, ItemListener {
    static JFrame menu;
    static String[][] theme = {
        {"#232B2D", "#0D1B1E", "#FFF7FA", "#dc143c", "#228b22", "#d2691e", "#87ceeb", "#c2b280","#6f42c1","#ffd700","#90ee90","#afafe0","#ffffdd"},
        {"#CBC5A8", "#FFF8DC", "#FFDA36", "#e91e63", "#ffc048", "#20c997", "#a6ff00", "#003580","#ff0080","#333333","#f28030","#40e0d0","#ff00ff"},
        {"#5D5882", "#3A3561", "#000000", "#007bff", "#00ff00", "#ff0080", "#ffa500", "#7f3c86","#a6ff00","#000000","#ffff00","#e91e63","#ff0000"},
        {"#c4c0c0", "#e0e0e0", "#000000", "#007bff", "#ffc107", "#20c997", "#c8a2c8", "#ff0080","#fd7e14","#4ca3ff","#00ff00","#343a40","#ffffff"},
        {"#f5f5dc", "#FFFFF0", "#000000", "#4169e1", "#ffff00", "#ff00ff", "#20c997", "#00ff00","#fd7e14","#800080","#00ffff","#343a40","#ffffff"},
        {"#f5f5dc", "#FFFFF0", "#000000", "#228b22", "#ff0080", "#900C3F", "#fa8072", "#20c997","#c8a2c8","#fd7e14","#ffc107","#343a40","#ffffff"}
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
        String[] colorOptions = {"Dark", "Light", "Cool", "Deuteranopia", "Protanopia","Tritanopia"};
        themes = new JComboBox<String>(colorOptions);
        themes.setSelectedIndex(selectedTheme);

        JTextArea description=new JTextArea("How To Play: The goal of ColorFlow is to connect all the colored pairs of dots with each other without crossing over any other paths or leaving any spots uncovered. To connect the paths, click on any of the colored cricles and drag your mouse to connect it to its respective pair. If you want to undo a path, simply right click on the path you want to remove. If you ever get stuck, you can ask for a hint to reveal one of the correct paths. There are three levels of difficulty each with their own size of the board, Easy(5x5), Medium(8x8), and Hard(12x12). In order to get the best score you have to fill the board and connect all paths in a short amount of time, with no undos, and no hints. Good Luck!");
        description.setLineWrap(true);
        description.setWrapStyleWord(true);
        description.setFont(font2);
        description.setMargin(new Insets(5,5,5,5));
        description.setEditable(false);
        description.setBounds(140, 330, 800, 200);
        description.setBackground(Color.decode(theme[selectedTheme][3]));
        description.setForeground(Color.decode(theme[selectedTheme][2]));
        easyMode.setBounds(250, 230, 100, 40);
        easyMode.setBackground(Color.decode(theme[selectedTheme][3]));
        easyMode.setForeground(Color.decode(theme[selectedTheme][2]));
        mediumMode.setBounds(480, 230, 100, 40);
        mediumMode.setBackground(Color.decode(theme[selectedTheme][3]));
        mediumMode.setForeground(Color.decode(theme[selectedTheme][2]));
        hardMode.setBounds(710, 230, 100, 40);
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
        menu.add(description);
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
            menu.dispose();
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
