import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

public class TransparentGlassPane extends JPanel {

    private JPanel winPanel;

    public TransparentGlassPane(JPanel winPanel) {
        setOpaque(false);
        setLayout(new GridBagLayout());
        this.winPanel = winPanel;
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                e.consume();
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                e.consume();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(new Color(0, 0, 0, 180)); // lowering opacity
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }

    public void showWinPanel() {
        removeAll(); // Clear any existing components
        add(winPanel, new GridBagConstraints());
        winPanel.setVisible(true);
        setVisible(true);
        revalidate();
        repaint();
    }

    public void hideWinPanel() {
        setVisible(false);
        remove(winPanel);
    }
}