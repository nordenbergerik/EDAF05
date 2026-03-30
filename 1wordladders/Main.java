import java.util.*;

public class Main {

    public static void main(String[] args) {
        // Scanner to read input from standard input
        double t1 = System.nanoTime();
        Scanner sc = new Scanner(System.in);

        // N: number of words in the dictionary
        int N = sc.nextInt();
        // Q: number of queries to process
        int Q = sc.nextInt();

        // Array to store the list of words
        String[] words = new String[N];

        // Read N words from input
        for (int i = 0; i < N; i++) {
            words[i] = sc.next();
        }

        // Build the graph as adjacency list
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            graph.add(new ArrayList<>());
        }

        // Add directed edges: from word i to word j if canGo condition holds
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (i != j && canGo(words[i], words[j])) {
                    graph.get(i).add(j);
                }
            }
        }

        // Map to associate each word with its index for quick lookup
        Map<String, Integer> index = new HashMap<>();
        for (int i = 0; i < N; i++) {
            index.put(words[i], i);
        }

       
        // Process each query
        for (int i = 0; i < Q; i++) {
            // Read start and end words for the query
            String start = sc.next();
            String end = sc.next();

            // Get indices of start and end words
            int s = index.get(start);
            int t = index.get(end);

            // Compute shortest path using BFS
            int answer = bfs(graph, s, t);

            // Output the result
            if (answer == -1) {
                System.out.println("Impossible");
            } else {
                System.out.println(answer);
            }
        }
        double t2 = System.nanoTime();
        System.out.println((t2-t1));
        sc.close();
    }

    // Check if we can move from word a to word b
    // Condition: the last 4 letters of a must be covered by the letters in b (multiset subset)
    static boolean canGo(String a, String b) {
        // countA: frequency of letters in the last 4 positions of a
        int[] countA = new int[26];
        // countB: frequency of letters in all positions of b
        int[] countB = new int[26];

        // Count letters in b
        for (char c : b.toCharArray()) {
            countB[c - 'a']++;
        }

        // Count letters in the last 4 positions of a (indices 1 to 4)
        for (int i = 1; i < 5; i++) {
            char c = a.charAt(i);
            countA[c - 'a']++;
        }

        // Check if every letter in countA has sufficient count in countB
        for (int i = 0; i < 26; i++) {
            if (countA[i] > countB[i]) {
                return false;
            }
        }

        return true;
    }

    // BFS to find the shortest path from start node to target node in the graph
    // Returns the number of edges in the path, or -1 if no path exists
    static int bfs(List<List<Integer>> graph, int start, int target) {
        // Queue for BFS traversal (FIFO)
        Queue<Integer> queue = new LinkedList<>();
        // visited: tracks which nodes have been visited to avoid cycles
        boolean[] visited = new boolean[graph.size()];
        // dist: stores the distance (number of edges) from start to each node
        int[] dist = new int[graph.size()];

        // Initialize BFS from start node
        queue.add(start);
        visited[start] = true;
        dist[start] = 0;

        // Process nodes level by level
        while (!queue.isEmpty()) {
            // Dequeue the next node to process
            int u = queue.poll();

            // If we reached the target, return the distance
            if (u == target) {
                return dist[u];
            }

            // Explore all neighbors of u
            for (int v : graph.get(u)) {
                if (!visited[v]) {
                    visited[v] = true;
                    dist[v] = dist[u] + 1;  // Distance increases by 1
                    queue.add(v);  // Add to queue for later processing
                }
            }
        }

        // No path found
        return -1;
    }
}