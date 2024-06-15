import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class WinPanel extends JPanel {

    public WinPanel(JFrame parent, int stars,String[]theme) {
        setPreferredSize(new Dimension(400, 300));
        setBackground(Color.decode(theme[1]));
        setLayout(null);

        double avrScore=stars;
        int scores=0;
        //update score
        try {
            FileReader reader = new FileReader("src\\score.txt");
            BufferedReader bufferedReader = new BufferedReader(reader);
            avrScore=Double.parseDouble(bufferedReader.readLine());
            scores=Integer.valueOf(bufferedReader.readLine());
            reader.close();
            avrScore=Math.round((avrScore*scores+stars)/(++scores) * 100.0) / 100.0;
            
            FileWriter writer = new FileWriter("src\\score.txt");
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
            bufferedWriter.write(avrScore+"");
            bufferedWriter.newLine();
            bufferedWriter.write(String.valueOf(scores));
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //set up conponents
        JLabel winMessage = new JLabel("Congratulations, You Won!");
        winMessage.setFont(new Font("Arial", Font.BOLD, 18));
        winMessage.setBounds(80,30,250,30);
        winMessage.setForeground(Color.decode(theme[2]));
        JLabel score = new JLabel("Current Score: "+stars+"/5 stars");
        score.setFont(new Font("Arial", Font.BOLD, 12));
        score.setBounds(20,150,160,20);
        score.setForeground(Color.decode(theme[2]));
        JLabel avrScoreLabel = new JLabel("Average Score: "+avrScore+"/5 stars");
        avrScoreLabel.setFont(new Font("Arial", Font.BOLD, 12));
        avrScoreLabel.setBounds(220,150,160,20);
        avrScoreLabel.setForeground(Color.decode(theme[2]));

        // Load star icons
        ImageIcon filledStar = new ImageIcon("lib\\starFilled.png");
        ImageIcon emptyStar = new ImageIcon("lib\\starEmpty.png");

        // Create a panel to hold the stars with FlowLayout
        JPanel starsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        starsPanel.setBackground(Color.decode(theme[1]));
        starsPanel.setBounds(25, 80, 350, 60);
        for (int i = 0; i < 5; i++) {
            JLabel starLabel = new JLabel(i < stars ? filledStar : emptyStar);
            starsPanel.add(starLabel);
        }

        JButton newGameButton = new JButton("New Game");
        newGameButton.setBounds(150,230,100,30);
        newGameButton.addActionListener(e -> {
            parent.dispose();
            flow.game(flow.size, flow.theme);
        });
        add(score);
        add(avrScoreLabel);
        add(winMessage);
        add(starsPanel);
        add(newGameButton);
    }
}