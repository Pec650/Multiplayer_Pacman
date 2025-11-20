import java.util.*;

public class Coordination {
    int x, y;

    Coordination() {
        x = 0;
        y = 0;
    }

    class Node implements Comparable<Node> {
        int row;
        int col;
        double g;
        double h;
        double f;
        Node parent;

        Node (int row, int col) {
            this.row = row;
            this.col = col;
            this.g = Double.MAX_VALUE;
            this.h = Double.MAX_VALUE;
            this.parent = null;
        }

        @Override
        public int compareTo(Node o) {
            return Double.compare(this.f, o.f);
        }

        @Override
        public int hashCode() {
            return 31 * row + col;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Node node = (Node) obj;
            return row == node.row && col == node.col;
        }
    }

    private double calculatH(Node current, Node end) {
        return Math.sqrt(Math.pow(current.row - end.row, 2) + Math.pow(current.col - end.col, 2));
    }

    public void FirstPathFinder (boolean[][] grid, int curX, int curY, int destX, int destY) {
        curX = curX / 32;
        curY = curY / 32;
        destX = destX / 32;
        destY = destY / 32;
        List<Node> shortestPath =  AStarPathFinder(grid, curX, curY, destX, destY);
        if (!shortestPath.isEmpty()) {
            x = shortestPath.getFirst().row * 32;
            y = shortestPath.getFirst().col * 32;
            System.out.println("X "+ x + " Y " + y);
        } else {
            System.out.println("No path found");
        }
    }

    public List<Node> AStarPathFinder(boolean[][] grid, int startX, int startY, int endX, int endY) {
        int rows = grid.length;
        int cols = grid[0].length;

        PriorityQueue<Node> openList = new PriorityQueue<>();

        HashSet<Node> closedSet = new HashSet<>();

        Node[][] nodeGrid = new Node[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                nodeGrid[i][j] = new Node(i, j);
            }
        }

        Node startNode = nodeGrid[startX][startY];
        Node endNode = nodeGrid[endX][endY];

        startNode.g = 0;
        startNode.h = calculatH(startNode, endNode);
        startNode.f = startNode.g + startNode.h;
        openList.add(startNode);

        while (!openList.isEmpty()) {
            Node current = openList.poll();

            if (current.equals(endNode)) {
                return reconstructPath(current);
            }

            closedSet.add(current);

            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (i != 0 || j != 0) continue;
                    int newR = current.row + i;
                    int newC = current.col + j;

                    if (newR < 0 || newR >= rows || newC < 0 || newC >= cols) continue;

                    Node neighbor = nodeGrid[newR][newC];
                    if (closedSet.contains(neighbor)) continue;

                    double cost = (i == 0 || j == 0) ? 1.0 : Math.sqrt(2);
                    double tentativeG = current.g + cost;

                    if (tentativeG < neighbor.g) {
                        neighbor.parent = current;
                        neighbor.g = tentativeG;
                        neighbor.h = calculatH(neighbor, endNode);
                        neighbor.f = neighbor.g + neighbor.h;

                        if (!openList.contains(neighbor)) {
                            openList.add(neighbor);
                        }
                    }
                }
            }
        }
        return Collections.emptyList();
    }

    private List<Node> reconstructPath(Node end) {
        List<Node> path = new ArrayList<>();
        Node current = end;
        while (current != null) {
            path.add(current);
            current = current.parent;
        }
        Collections.reverse(path);
        return path;
    }
}
