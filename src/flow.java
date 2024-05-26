import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.HashSet;

public class flow implements ActionListener, MouseListener, MouseMotionListener {
    static JFrame window;
    static int[][] puzzle;
    static int[][] endPoints;
    static GridPanel board;
    static int size;
    static String[] theme;
    static int prevGridX, prevGridY;
    static HashSet<String> filledCells = new HashSet<>();
    static ArrayList<ArrayList<int[]>> paths = new ArrayList<>();

    public static void game(int s, String[] t) {
        flow Flow = new flow();
        size = s;
        theme = t;
        window = new JFrame("Color Flow");

        board = new GridPanel();
        ((GridPanel) board).updateBoard();
        board.setBounds(300, 100, 500, 500);
        board.setBackground(Color.decode(theme[0]));
        board.addMouseListener(Flow);
        board.addMouseMotionListener(Flow);

        JButton home = new JButton("Home");
        JButton newGame = new JButton("New Game");
        JButton hint = new JButton("Hint");
        home.addActionListener(Flow);
        newGame.addActionListener(Flow);
        hint.addActionListener(Flow);

        home.setBounds(100, 230, 100, 40);
        home.setBackground(Color.decode(theme[3]));
        home.setForeground(Color.decode(theme[2]));
        newGame.setBounds(100, 330, 100, 40);
        newGame.setBackground(Color.decode(theme[3]));
        newGame.setForeground(Color.decode(theme[2]));
        hint.setBounds(100, 430, 100, 40);
        hint.setBackground(Color.decode(theme[3]));
        hint.setForeground(Color.decode(theme[2]));

        window.add(board);
        window.add(home);
        window.add(newGame);
        window.add(hint);

        window.getContentPane().setBackground(Color.decode(theme[1]));
        window.setLayout(null);
        window.setSize(1080, 720);
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
    }

    public static int[][] generatePuzzle(int size) {
        int n;
        int[] position;
        puzzle = new int[size][size];
        int[][] ends = new int[size][size];
        int[] next;
        int[][] options = new int[4][2];
        int color=3;
        int filled=0;
        int[]directionX={-1,1,0,0};
        int[]directionY={0,0,-1,1};
        while(filled<size*size&&color<13) {
            int[][]spots=new int[size*size][2];
            int z=0;
            for(int r=0;r<size;r++){
                for(int c=0;c<size;c++){
                    if(puzzle[r][c]==0&&fillable(c, r)<2){
                        spots[z][0]=r;
                        spots[z][1]=c;
                        z++;
                    }
                }
            }
            position=spots[(int)(Math.random()*z)];
            puzzle[position[0]][position[1]] = color;
            filled++;
            ends[position[0]][position[1]] = color;
            int i = 0;
            while (i < size*2-4||fillable(position[1], position[0])!=0) {
                n = 0;
                for (int j = 0; j < 4; j++) {
                    int newX = position[1] + directionX[j];
                    int newY = position[0] + directionY[j];
                    if(inBounds(newX,newY)&&puzzle[newY][newX]==0&&fillable(newX, newY)<2){
                        options[n][0]=directionX[j];
                        options[n][1]=directionY[j];
                        n++;
                    }
                }
                if(n==0)break;
                next = options[(int) (Math.random() * n)];
                position[1] += next[0];
                position[0] += next[1];
                puzzle[position[0]][position[1]] = color;
                filled++;
                i++;
            }
            ends[position[0]][position[1]] = color;
            color++;
        }
        return ends;
    }

    public static int fillable(int positionX, int positionY) {
        int[] directionX = {-1, 1, 0, 0};
        int[] directionY = {0, 0, -1, 1};
        int[][]visited=new int[size][size];
        visited[positionY][positionX]=2;
        int adjAloneZeros = 0;
    
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                if (visited[r][c]!=0) continue;
                if (puzzle[r][c] == 0) {
    
                    for (int i = 0; i < 4; i++) {
                        int newX = c + directionX[i];
                        int newY = r + directionY[i];
                        if (inBounds(newX, newY)&&puzzle[newY][newX]==0) {
                            if (visited[newY][newX]>1) continue;
                            visited[newY][newX]++;
                            visited[r][c]++;
                            break;
                            
                        }
                    }
    
                    if (visited[r][c]==0) {
                        boolean isAdj=false;
                        for (int i = 0; i < 4; i++) {
                            if (c  == positionX + directionX[i] && r  == positionY + directionY[i]) {
                                adjAloneZeros++;
                                isAdj=true;
                            }
                        }
                        if (!isAdj) return 2;
                    }
                }
            }
        }
        return adjAloneZeros;
    }
    public static boolean inBounds(int x,int y) {
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
            // Implement hint logic
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
            endPoints = generatePuzzle(size);
            printPuzzle();
            paths.clear();
            filledCells.clear();
            repaint();
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            int unitSize = 500 / size;
            for (int i = 0; i < size; i++) {
                g2.setColor(Color.decode(theme[1]));
                g2.drawLine(i * unitSize, 0, i * unitSize, 500);
                g2.drawLine(0, i * unitSize, 500, i * unitSize);
                for (int j = 0; j < size; j++) {
                    g2.setColor(Color.decode(theme[endPoints[i][j]]));
                    g2.fillOval((int) ((j + 0.25) * unitSize), (int) ((i + 0.25) * unitSize), unitSize / 2, unitSize / 2);
                }
            }

            g2.setStroke(new BasicStroke(unitSize/4));
            for (ArrayList<int[]> p : paths) {
                g2.setColor(Color.decode(theme[p.get(0)[0]]));
                for (int i = 1; i < p.size() - 1; i++) {
                    g2.drawLine(p.get(i)[0] * unitSize + unitSize / 2, p.get(i)[1] * unitSize + unitSize / 2,
                            p.get(i + 1)[0] * unitSize + unitSize / 2, p.get(i + 1)[1] * unitSize + unitSize / 2);
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
            prevGridX = currentGridX = x / (500 / size);
            prevGridY = currentGridY = y / (500 / size);
            path.add(new int[]{currentGridX, currentGridY});
            cell=currentGridX+","+currentGridY;
            filledCells.add(cell);
            drawing = true;
            repaint();
        }

        public void updateDrawing(int tempGridX, int tempGridY) {
            if (endPoints[tempGridY][tempGridX] == 0) {
                currentGridX = tempGridX;
                currentGridY = tempGridY;
                path.add(new int[]{currentGridX, currentGridY});
                repaint();
                try {
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                cell = path.get(path.size()-1)[0] + "," + path.get(path.size()-1)[1];
                filledCells.add(cell);
                prevGridX = currentGridX;
                prevGridY = currentGridY;
            } else if (endPoints[tempGridY][tempGridX] == path.get(0)[0]) {
                currentGridX = tempGridX;
                currentGridY = tempGridY;
                cell=currentGridX+","+currentGridY;
                filledCells.add(cell);
                pathComplete = true;
                path.add(new int[]{currentGridX, currentGridY});
                paths.add(new ArrayList<>(path));

                
                System.out.println(filledCells.size());
                if (filledCells.size() == size * size) {
                    gameWon();
                }
                repaint();
                try {
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                stopDrawing();
            }
        }

        public void stopDrawing() {
            drawing = false;
            if (!pathComplete) {
                for (int p = 1;p<path.size();p++) {
                    cell = path.get(p)[0] + "," + path.get(p)[1];
                    filledCells.remove(cell);
                }
                path.clear();
            }
            repaint();
        }

        public void clearPathAndResetDrawing(ArrayList<int[]> pathToRemove) {
            
            for (int point=1; point<pathToRemove.size();point++) {
                cell = pathToRemove.get(point)[0] + "," + pathToRemove.get(point)[1];
                filledCells.remove(cell);
                
            }
            paths.remove(pathToRemove);
            path.clear();
            drawing = false;
            repaint();
        }
    }

    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            int x = e.getX();
            int y = e.getY();
            int unitSize = 500 / size;
            for (int a = 0; a < size; a++) {
                for (int i = 0; i < size; i++) {
                    if (endPoints[a][i] != 0 && x > i * unitSize && x < i * unitSize + unitSize &&
                            y > a * unitSize && y < a * unitSize + unitSize) {
                        ((GridPanel) board).startDrawing(x, y, endPoints[a][i]);
                        return;
                    }
                }
            }
        } else if (e.getButton() == MouseEvent.BUTTON3) {
            int gridX = e.getX() / (500 / size);
            int gridY = e.getY() / (500 / size);
            for (ArrayList<int[]> p : paths) {
                for (int point=1;point<p.size();point++) {
                    if (p.get(point)[0] == gridX && p.get(point)[1] == gridY) {
                        ((GridPanel) board).clearPathAndResetDrawing(p);
                        return;
                    }
                }
            }
        }
    }

    public void mouseDragged(MouseEvent e) {
        int tempGridX = e.getX() / (500 / size);
        int tempGridY = e.getY() / (500 / size);

        if (((Math.abs(tempGridX - prevGridX) == 1 && Math.abs(tempGridY - prevGridY) == 0) || (Math.abs(tempGridY - prevGridY) == 1 && Math.abs(tempGridX - prevGridX) == 0)) && inBounds(tempGridX,tempGridY)&& !filledCells.contains(tempGridX + "," + tempGridY)) {
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

    public static void gameWon() {
        System.out.println("you won");
    }
    public static void printPuzzle() {
        // Determine the maximum width required
        int maxWidth = 0;
        for (int[] row : puzzle) {
            for (int cell : row) {
                int width = String.valueOf(cell).length();
                if (width > maxWidth) {
                    maxWidth = width;
                }
            }
        }
    
        // Print each row with proper padding
        for (int[] row : puzzle) {
            for (int cell : row) {
                System.out.print(String.format("%" + maxWidth + "d ", cell));
            }
            System.out.println();
        }
        System.out.println();
    }
}
