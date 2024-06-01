import javax.swing.*;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashSet;

public class flow implements ActionListener, MouseListener, MouseMotionListener {
    static JFrame window;
    static int[][] puzzle;
    static int[][] endPoints;
    static GridPanel board;
    static int size;
    static String[] theme;
    static int undos;
    static Timer timer = null;
    static JLabel undosLabel;
    static JLabel timerLabel;
    static int stars;
    static int hints;
    static int elaTime;
    static int prevGridX, prevGridY;
    static HashSet<String> filledCells = new HashSet<>();
    static ArrayList<ArrayList<int[]>> solutionPaths = new ArrayList<>();
    static ArrayList<ArrayList<int[]>> paths = new ArrayList<>();
    static TransparentGlassPane glassPane;

    public static void game(int s, String[] t) {
        flow Flow = new flow();
        size = s;
        theme = t;
        window = new JFrame("Color Flow");

        timerLabel = new JLabel();
        undosLabel = new JLabel();
        JButton home = new JButton("Home");
        JButton newGame = new JButton("New Game");
        JButton hint = new JButton("Hint");
        home.addActionListener(Flow);
        newGame.addActionListener(Flow);
        hint.addActionListener(Flow);

        timerLabel.setBounds(950, 130, 100, 40);
        timerLabel.setForeground(Color.decode(theme[2]));
        undosLabel.setBounds(950, 230, 100, 40);
        undosLabel.setForeground(Color.decode(theme[2]));
        home.setBounds(100, 230, 100, 40);
        home.setBackground(Color.decode(theme[3]));
        home.setForeground(Color.decode(theme[2]));
        newGame.setBounds(100, 330, 100, 40);
        newGame.setBackground(Color.decode(theme[3]));
        newGame.setForeground(Color.decode(theme[2]));
        hint.setBounds(100, 430, 100, 40);
        hint.setBackground(Color.decode(theme[3]));
        hint.setForeground(Color.decode(theme[2]));

        board = new GridPanel();
        ((GridPanel) board).updateBoard();
        board.setBounds(300, 100, 504, 504);
        board.setBackground(Color.decode(theme[0]));
        board.addMouseListener(Flow);
        board.addMouseMotionListener(Flow);

        window.add(board);
        window.add(home);
        window.add(newGame);
        window.add(hint);
        window.add(timerLabel);
        window.add(undosLabel);

        window.getContentPane().setBackground(Color.decode(theme[1]));
        window.setLayout(null);
        window.setSize(1080, 720);
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
    }

    public static void gameWon() {
        timer.stop();
        if (elaTime < size * 15) {
            stars++;
            if (elaTime < size * 5) stars++;
        }
        if (undos < 3) {
            stars++;
            if (undos == 0) stars++;
        }
        if(hints!=0)stars=0;

        WinPanel winPanel = new WinPanel(window, stars,theme);
        glassPane = new TransparentGlassPane(winPanel);
        window.setGlassPane(glassPane);

        // Show the win screen
        glassPane.showWinPanel();
    }

    private static int[][] generatePuzzle(int size) {
        int n;
        ArrayList<int[]> path;
        int[] position = new int[2];
        puzzle = new int[size][size];
        int[][] ends = new int[size][size];
        int[] next = new int[2];
        int[][] options = new int[16][2];
        int color = 3;
        int filled = 0;
        int[] directionX = {-1, 1, 0, 0};
        int[] directionY = {0, 0, -1, 1};
        while (filled < size * size && color < 13) {
            int[][] spots = new int[size * size][2];
            int z = 0;
            for (int r = 0; r < size; r++) {
                for (int c = 0; c < size; c++) {
                    if (puzzle[r][c] == 0 && fillable(c, r) < 2) {
                        spots[z][0] = r;
                        spots[z][1] = c;
                        z++;
                    }
                }
            }
            int r = (int) (Math.random() * z);
            position[0] = spots[r][0];
            position[1] = spots[r][1];
            puzzle[position[0]][position[1]] = color;
            filled++;
            ends[position[0]][position[1]] = color;
            int i = 0;
            boolean restart = false;
            path = new ArrayList<>();
            path.add(new int[]{color});
            path.add(new int[]{position[1], position[0]});
            int index=2;
            int d=1;
            while (i < size * 3 - 10 || fillable(position[1], position[0]) != 0) {
                n = 0;
                for (int j = 0; j < 4; j++) {
                    int newX = position[1] + directionX[j];
                    int newY = position[0] + directionY[j];
                    if (inBounds(newX, newY) && puzzle[newY][newX] == 0 && fillable(newX, newY) < 2) {
                        options[n][0] = directionX[j];
                        options[n][1] = directionY[j];
                        n++;
                        if (directionX[j] == next[0] && directionY[j] == next[1]) {
                            options[n][0] = directionX[j];
                            options[n][1] = directionY[j];
                            n++;
                            options[n][0] = directionX[j];
                            options[n][1] = directionY[j];
                            n++;
                            options[n][0] = directionX[j];
                            options[n][1] = directionY[j];
                            n++;
                        }
                    }
                }
                if (n == 0) {
                    ends[position[0]][position[1]] = color;
                    if (restart) {
                        break;
                    } else {
                        ends[spots[r][0]][spots[r][1]] = 0;
                        position[0] = spots[r][0];
                        position[1] = spots[r][1];
                        restart = true;
                        index=1;
                        d=0;
                        continue;
                    }
                }
                next = options[(int) (Math.random() * n)];
                position[1] += next[0];
                position[0] += next[1];
                path.add(index,new int[]{position[1], position[0]});
                puzzle[position[0]][position[1]] = color;
                filled++;
                index+=d;
                i++;
            }
            solutionPaths.add(path);
            ends[position[0]][position[1]] = color;
            color++;
        }
        return ends;
    }

    private static int fillable(int positionX, int positionY) {
        int[] directionX = {-1, 1, 0, 0};
        int[] directionY = {0, 0, -1, 1};
        int[][] visited = new int[size][size];
        visited[positionY][positionX] = 2;
        int adjAloneZeros = 0;

        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                if (visited[r][c] != 0) continue;
                if (puzzle[r][c] == 0) {

                    for (int i = 0; i < 4; i++) {
                        int newX = c + directionX[i];
                        int newY = r + directionY[i];
                        if (inBounds(newX, newY) && puzzle[newY][newX] == 0) {
                            if (visited[newY][newX] > 1) continue;
                            visited[newY][newX]++;
                            visited[r][c]++;
                            break;

                        }
                    }

                    if (visited[r][c] == 0) {
                        boolean isAdj = false;
                        for (int i = 0; i < 4; i++) {
                            if (c == positionX + directionX[i] && r == positionY + directionY[i]) {
                                adjAloneZeros++;
                                isAdj = true;
                            }
                        }
                        if (!isAdj) return 2;
                    }
                }
            }
        }
        return adjAloneZeros;
    }

    private static boolean inBounds(int x, int y) {
        return x >= 0 && x < size && y >= 0 && y < size;
    }

    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton) e.getSource();

        if (button.getText().equals("Home")) {
            menu.app();
            window.dispose();
        } else if (button.getText().equals("New Game")) {
            ((GridPanel) board).updateBoard();
        } else if (button.getText().equals("Hint")) {
            hintMethod();
        }
    }

    static class GridPanel extends JPanel {
        int currentGridX, currentGridY;
        String cell;
        ArrayList<int[]> path = new ArrayList<>();
        boolean drawing, pathComplete;

        GridPanel() {
            drawing = false;
        }

        public void updateBoard() {
            solutionPaths.clear();
            endPoints = generatePuzzle(size);
            printPuzzle();
            paths.clear();
            undos=0;
            elaTime=0;
            hints=0;
            stars=1;
            undosLabel.setText("Undos: "+String.valueOf(undos));
            filledCells.clear();
            if (timer != null && timer.isRunning()) {
                timer.stop();
            }
            startTimer();
            repaint();
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            int unitSize = 504 / size;
            for (int i = 0; i < size; i++) {
                g2.setColor(Color.decode(theme[1]));
                g2.drawLine(i * unitSize, 0, i * unitSize, 504);
                g2.drawLine(0, i * unitSize, 504, i * unitSize);
                for (int j = 0; j < size; j++) {
                    if (puzzle[i][j] == 0) {
                        g2.setColor(Color.decode(theme[1]));
                        g2.fillRect(j * unitSize, i * unitSize, unitSize, unitSize);
                        cell = j + "," + i;
                        filledCells.add(cell);
                    } else {
                        g2.setColor(Color.decode(theme[endPoints[i][j]]));
                        g2.fillOval((int) ((j + 0.25) * unitSize), (int) ((i + 0.25) * unitSize), unitSize / 2, unitSize / 2);
                    }
                }
            }

            g2.setStroke(new BasicStroke(unitSize / 4));
            synchronized (paths) {
                for (ArrayList<int[]> p : paths) {
                    g2.setColor(Color.decode(theme[p.get(0)[0]]));
                    for (int i = 1; i < p.size() - 1; i++) {
                        g2.drawLine(p.get(i)[0] * unitSize + unitSize / 2, p.get(i)[1] * unitSize + unitSize / 2, p.get(i + 1)[0] * unitSize + unitSize / 2, p.get(i + 1)[1] * unitSize + unitSize / 2);
                    }
                }
            }

            if (drawing) {
                g2.setColor(Color.decode(theme[path.get(0)[0]]));
                for (int i = 1; i < path.size() - 1; i++) {
                    g2.drawLine(path.get(i)[0] * unitSize + unitSize / 2, path.get(i)[1] * unitSize + unitSize / 2,
                            path.get(i + 1)[0] * unitSize + unitSize / 2, path.get(i + 1)[1] * unitSize + unitSize / 2);
                }
            }
        }

        public void startDrawing(int x, int y, int c) {
            path.clear();
            pathComplete = false;
            path.add(new int[]{c});
            prevGridX = currentGridX = x / (504 / size);
            prevGridY = currentGridY = y / (504 / size);
            path.add(new int[]{currentGridX, currentGridY});
            cell = currentGridX + "," + currentGridY;
            filledCells.add(cell);
            drawing = true;
            repaint();
        }

        public void updateDrawing(int tempGridX, int tempGridY) {
            if (path.isEmpty()) {
                return; // Exit if the path is empty
            }
            if (filledCells.contains(tempGridX + "," + tempGridY)) {
                // Check if backtracking
                if (tempGridX == path.get(path.size() - 2)[0] && tempGridY == path.get(path.size() - 2)[1]) {
                    // Remove the last point from the path
                    path.remove(path.size() - 1);
                    filledCells.remove(prevGridX + "," + prevGridY);
        
                    // Update the previous point to the new last point in the path
                    prevGridX = path.get(path.size() - 1)[0];
                    prevGridY = path.get(path.size() - 1)[1];
                    repaint();
                    return;
                }
            } else {
                // Forward drawing logic
                if (endPoints[tempGridY][tempGridX] == 0 || endPoints[tempGridY][tempGridX] == path.get(0)[0]) {
                    currentGridX = tempGridX;
                    currentGridY = tempGridY;
                    path.add(new int[]{currentGridX, currentGridY});
                    repaint();
                    cell = currentGridX + "," + currentGridY;
                    filledCells.add(cell);
                    prevGridX = currentGridX;
                    prevGridY = currentGridY;
        
                    if (endPoints[tempGridY][tempGridX] == path.get(0)[0]) {
                        pathComplete = true;
                        synchronized (paths) {
                            paths.add(new ArrayList<>(path));
                        }
        
                        if (filledCells.size() == size * size) {
                            gameWon();
                        }
                        repaint();
                        stopDrawing();
                    }
                }
            }
        }
        

        public void stopDrawing() {
            drawing = false;
            if (!pathComplete) {
                for (int p = 1; p < path.size(); p++) {
                    cell = path.get(p)[0] + "," + path.get(p)[1];
                    filledCells.remove(cell);
                }
                path.clear();
            }
            System.out.println(filledCells.size());
            repaint();
        }

        public void clearPathAndResetDrawing(ArrayList<int[]> pathToRemove) {
            synchronized (paths) {
                for (int point = 1; point < pathToRemove.size(); point++) {
                    cell = pathToRemove.get(point)[0] + "," + pathToRemove.get(point)[1];
                    filledCells.remove(cell);
                }
                paths.remove(pathToRemove);
            }
            undos++;
            undosLabel.setText(String.valueOf("Undos "+undos));
            path.clear();
            drawing = false;
            repaint();
        }
    }

    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            int x = e.getX();
            int y = e.getY();
            int unitSize = 504 / size;
            for (int a = 0; a < size; a++) {
                for (int i = 0; i < size; i++) {
                    if (endPoints[a][i] != 0 && x > i * unitSize && x < i * unitSize + unitSize && y > a * unitSize && y < a * unitSize + unitSize) {
                        boolean pathFound = false;
                        synchronized (paths) {
                            for (ArrayList<int[]> p : paths) {
                                for (int[] point : p) {
                                    if (point.length > 1 && point[0] == i && point[1] == a) {
                                        pathFound = true;
                                        break;
                                    }
                                }
                                if (pathFound) break;
                            }
                        }
                        if (!pathFound) {
                            ((GridPanel) board).startDrawing(x, y, endPoints[a][i]);
                            return;
                        }
                    }
                }
            }
        } else if (e.getButton() == MouseEvent.BUTTON3) {
            int gridX = e.getX() / (504 / size);
            int gridY = e.getY() / (504 / size);
            synchronized (paths) {
                for (ArrayList<int[]> p : paths) {
                    for (int point = 1; point < p.size(); point++) {
                        if (p.get(point)[0] == gridX && p.get(point)[1] == gridY) {
                            ((GridPanel) board).clearPathAndResetDrawing(p);
                            return;
                        }
                    }
                }
            }
        }
    }

    public void mouseDragged(MouseEvent e) {
        int tempGridX = e.getX() / (504 / size);
        int tempGridY = e.getY() / (504 / size);
        if (((Math.abs(tempGridX - prevGridX) == 1 && Math.abs(tempGridY - prevGridY) == 0) || (Math.abs(tempGridY - prevGridY) == 1 && Math.abs(tempGridX - prevGridX) == 0)) && inBounds(tempGridX, tempGridY)) {
            ((GridPanel) board).updateDrawing(tempGridX, tempGridY);
        }
    }

    public void mouseReleased(MouseEvent e) {
        ((GridPanel) board).stopDrawing();
    }

    public void mouseClicked(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseMoved(MouseEvent e) {}

    public static void startTimer() {
        timerLabel.setText("Time: 0");
        timer = new Timer(1000, new TimerListener());
        timer.start();
    }
    static class TimerListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            elaTime++;
            timerLabel.setText("Time: "+String.valueOf(elaTime));
        }
    }

    private static void hintMethod() {
        String cell;
        hints++;
        for (ArrayList<int[]> p : solutionPaths) {
            cell = p.get(1)[0] + "," + p.get(1)[1];
            if (!filledCells.contains(cell)) {
                ArrayList<ArrayList<int[]>> pathsToRemove = new ArrayList<>();
                for (int point = 1; point < p.size(); point++) {
                    for (ArrayList<int[]> p2 : paths) {
                        if (pathIntersects(p2, p.get(point))) {
                            pathsToRemove.add(p2);
                            for (int index = 1; index < p2.size(); index++) {
                                cell=p2.get(index)[0]+","+p2.get(index)[1];
                                filledCells.remove(cell);
                            }
                        }
                    }
                    cell = p.get(point)[0] + "," + p.get(point)[1];
                    filledCells.add(cell);
                }
                paths.removeAll(pathsToRemove);
                paths.add(p);
                board.repaint();
                if(filledCells.size()==size*size)gameWon();
                break;
            }
        }
    }
    
    private static boolean pathIntersects(ArrayList<int[]> path, int[] point) {
        for (int coor=1;coor<path.size();coor++) {
            if (path.get(coor)[0] == point[0] && path.get(coor)[1] == point[1]) {
                return true;
            }
        }
        return false;
    }

    public static void printPuzzle() {
        // Print each row with proper padding
        for (int[] row : puzzle) {
            for (int cell : row) {
                System.out.print(String.format("%" + 2 + "d ", cell));
            }
            System.out.println();
        }
        System.out.println();
    }
}
