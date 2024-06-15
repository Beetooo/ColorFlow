import javax.swing.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.*;

public class menu implements ActionListener, ItemListener {
    // GUI components and properties
    static JFrame menu;
    static int selectedTheme = 0;
    static JComboBox<String> themesComboBox;

    // Initializing fonts
    static Font font1 = new Font("Arial", Font.PLAIN, 30);
    static Font font2 = new Font("Arial", Font.PLAIN, 20);
    static Font font3 = new Font("Arial", Font.BOLD, 30);

    public static void main(String[] args) {
        app(); // Start the application
    }

    public static void app() {
        // Load theme and logo
        String[][] theme = themes.colorThemes;
        ImageIcon logo = new ImageIcon("lib\\colorFlowLogo.png");
        
        // Create main menu frame
        menu = new JFrame("Color Flow");
        JLabel title = new JLabel("colorFlow");
        title.setIcon(logo);

        // Create difficulty buttons
        JButton easyMode = new JButton("Easy");
        JButton mediumMode = new JButton("Medium");
        JButton hardMode = new JButton("Hard");

        // Theme selection combo box
        String[] colorOptions = {"Dark", "Light", "Cool", "Deuteranopia", "Protanopia", "Tritanopia"};
        themesComboBox = new JComboBox<>(colorOptions);
        themesComboBox.setSelectedIndex(selectedTheme);

        // Game description
        JTextArea description = new JTextArea("How To Play: The goal of ColorFlow is to connect all the colored pairs of dots with each other without crossing over any other paths or leaving any spots uncovered. To connect the paths, click on any of the colored cricles and drag your mouse to connect it to its respective pair. If you want to undo a path, simply right click on the path you want to remove. If you ever get stuck, you can ask for a hint to reveal one of the correct paths. There are three levels of difficulty each with their own size of the board, Easy(5x5), Medium(8x8), and Hard(12x12). In order to get the best score you have to fill the board and connect all paths in a short amount of time, with no undos, and no hints. Good Luck!");
        description.setLineWrap(true);
        description.setWrapStyleWord(true);
        description.setFont(font2);
        description.setMargin(new Insets(5, 5, 5, 5));
        description.setEditable(false);
        description.setBounds(140, 330, 800, 200);
        description.setBackground(Color.decode(theme[selectedTheme][3]));
        description.setForeground(Color.decode(theme[selectedTheme][2]));

        // Set properties for buttons
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
        themesComboBox.setBounds(950, 620, 100, 40);
        themesComboBox.setBackground(Color.decode(theme[selectedTheme][3]));
        themesComboBox.setForeground(Color.decode(theme[selectedTheme][2]));

        // Create an instance of menu to use non-static methods
        menu Menu = new menu();
        easyMode.addActionListener(Menu);
        mediumMode.addActionListener(Menu);
        hardMode.addActionListener(Menu);
        themesComboBox.addItemListener(Menu);

        // Add components to menu
        menu.add(easyMode);
        menu.add(mediumMode);
        menu.add(hardMode);
        menu.add(themesComboBox);
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

    // Handle button actions
    public void actionPerformed(ActionEvent e) {
        String[][] theme = themes.colorThemes;
        JButton button = (JButton) e.getSource();

        // Start game with selected difficulty
        if (button.getText().equals("Easy")) {
            flow.game(5, theme[themesComboBox.getSelectedIndex()]);
            menu.dispose();
        } else if (button.getText().equals("Medium")) {
            flow.game(8, theme[themesComboBox.getSelectedIndex()]);
            menu.dispose();
        } else if (button.getText().equals("Hard")) {
            flow.game(12, theme[themesComboBox.getSelectedIndex()]);
            menu.dispose();
        }
    }

    // Handle theme selection change
    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            menu.dispose(); // Close current menu
            selectedTheme = themesComboBox.getSelectedIndex(); // Update selected theme
            app(); // Restart app with new theme
        }
    }
}
