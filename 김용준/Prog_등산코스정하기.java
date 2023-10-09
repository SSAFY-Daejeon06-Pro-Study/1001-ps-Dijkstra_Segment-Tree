import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;

/**
 * 풀이 시작 : 7:35
 * 풀이 완료 :
 * 풀이 시간 :
 *
 * 문제 해석
 * n개의 지점으로 이루어진 산이 있음
 * 지점은 출입구, 쉼터, 산봉우리 중 하나임.
 * 각 지점은 양방향 통행이 가능한 등산로로 연결되어 있음
 * 쉼터 or 산봉우리를 방문할 때마다 휴식 취할 수 있음
 * 휴식 없이 이동해야 하는 시간 중 가장 긴 시간을 intensity라고 함
 * 출입구 중 한 곳에서 출발해 산봉우리 중 한 곳만 방문하고
 * 원래 출입구로 돌아오는 등산코스를 정하려고 할 때 intensity가 최소가 되도록 해야 함
 *
 * 구해야 하는 것
 * 산봉우리 찍고 원래 출입구로 도착하기까지 intensity가 가장 작은 경로
 * 산봉우리 번호와 intensity를 리턴해야 함
 * 만약 intensity가 같다면 산봉우리 가장 작은 순
 *
 * 문제 입력
 * 지점 수 N
 * 등산로 정보 담은 paths[][]
 * 출입구 번호 담긴 정수 배열 gates[]
 * 산봉우리 번호 담긴 summits[]
 *
 * 제한 요소
 * 2 <= N <= 50000
 * N - 1 <= paths.length <= 200000
 * paths[i] = {a, b, c} a <-> b 거리 c
 * 1 <= c <= 10_000_000
 * 서로 다른 지점을 연결하는 등산로는 1개
 *
 * 생각나는 풀이
 * 다익스트라
 * 무향그래프이므로 최소로 산봉우리 찍은 경로 그대로 내려오면 그게 최소임
 * 최소 거리가 아니라 최대 비용이 가장 작은 경로 순이므로 dist에 저장하는 값은 현재까지 경로 중 최대 비용
 *
 * 구현해야 하는 기능
 *
 */
class Solution {
    static int N;
    static ArrayList<Node>[] graph;
    static boolean[] isSummit;

    static class Node implements Comparable<Node> {
        int end, cost;

        public Node(int end, int cost) {
            this.end = end;
            this.cost = cost;
        }

        @Override
        public int compareTo(Node o) {
            return this.cost - o.cost;
        }
    }

    public int[] solution(int n, int[][] paths, int[] gates, int[] summits) {
        N = n;
        isSummit = new boolean[N + 1];
        for (int s : summits) isSummit[s] = true;
        makeGraph(paths);
        return dijkstra(gates, summits);
    }

    private int[] dijkstra(int[] gates, int[] summits) {
        PriorityQueue<Node> pq = new PriorityQueue<>();
        int[] dist = new int[N + 1];
        int INF = Integer.MAX_VALUE - 10_000_001;
        Arrays.fill(dist, INF);
        // 모든 시작점을 넣고 시작
        for (int g : gates) {
            pq.offer(new Node(g, 0));
            dist[g] = 0;
        }

        while (!pq.isEmpty()) {
            Node now = pq.poll();
            if (now.cost > dist[now.end]) continue;
            // 만약 산봉우리라면 거리 갱신하고 continue
            if (isSummit[now.end]) {
                dist[now.end] = now.cost;
                continue;
            }

            for (Node next : graph[now.end]) {
                int maxCost = Math.max(next.cost, now.cost);
                if (dist[next.end] > maxCost) {
                    dist[next.end] = maxCost;
                    pq.offer(new Node(next.end, maxCost));
                }
            }
        }
        Arrays.sort(summits); // 입력이 오름차순으로 주어지지 않는 경우도 있는듯함
        int min = Integer.MAX_VALUE;
        int idx = -1;
        for (int i = 0; i < summits.length; i++) {
            if (min > dist[summits[i]]) {
                min = dist[summits[i]];
                idx = summits[i];
            }
        }
        return new int[]{idx, min};
    }

    private void makeGraph(int[][] paths) {
        graph = new ArrayList[N + 1];
        for (int i = 1; i <= N; i++) graph[i] = new ArrayList<>();

        for (int i = 0; i < paths.length; i++) {
            int a = paths[i][0];
            int b = paths[i][1];
            int c = paths[i][2];
            graph[a].add(new Node(b, c));
            graph[b].add(new Node(a, c));
        }
    }
}
