import javax.swing.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.*;

public class menu implements ActionListener, ItemListener {
    static JFrame menu;
    static String[][] theme = {
        {"#0D1B1E","#394D52","#FF595E", "#FFCA3A", "#8AC926", "#1982C4", "#6A4C93"},
        {"#FFF8DC","#CBC5A8", "#FF595E", "#FFCA3A", "#8AC926", "#1982C4", "#6A4C93"},
        {"#3A3561","#5D5882","#123456", "#654321", "#FEDCBA", "#ABCDEF", "#111111"}
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

        easyMode.setBounds(250, 300, 100, 30);
        easyMode.setBackground(Color.decode(theme[selectedTheme][6]));
        mediumMode.setBounds(480, 300, 100, 30);
        mediumMode.setBackground(Color.decode(theme[selectedTheme][6]));
        hardMode.setBounds(710, 300, 100, 30);
        hardMode.setBackground(Color.decode(theme[selectedTheme][6]));
        title.setBounds(280, 10, 505, 200);
        themes.setBounds(950, 620, 100, 30);
        themes.setBackground(Color.decode(theme[selectedTheme][6]));

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
        menu.getContentPane().setBackground(Color.decode(theme[selectedTheme][0]));
        menu.setLayout(null);
        menu.setSize(1080, 720);
        menu.setLocationRelativeTo(null);
        menu.setVisible(true);
        menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton) e.getSource();

        if (button.getText().equals("Easy")) {
            flow.game(5, theme[themes.getSelectedIndex()]);
            menu.dispose();
        } 
        else if (button.getText().equals("Medium")) {
            flow.game(10, theme[themes.getSelectedIndex()]);
        } 
        else if (button.getText().equals("Hard")) {
            flow.game(15, theme[themes.getSelectedIndex()]);
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
