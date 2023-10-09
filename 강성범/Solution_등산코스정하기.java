package kr.ac.lecture.programmers;

import java.io.IOException;
import java.util.*;


/*
* [문제 요약]
* 출발지점에서 다시 돌아올 때 최소 가중치
*
* [제약 조건]
* 지점 (2 ≤ n ≤ 50,000)
* path 길이 (n - 1 ≤ paths의 길이 ≤ 200,000)
*   - path 형태 [i, j, w]
*       - i번과 j번의 지점 무방향 가중치
*       - 1 ≤ w ≤ 10,000,000
*   - 서로 다른 두 지점을 이동하는데 걸리는 시간
* 출입구 gate 는 여러 개일 수 있음
*   - 1 ≤ gates의 길이 ≤ n
* 산봉우리 summit는 여러 개일 수 있음
*   - 1 ≤ summits의 원소 ≤ n
*
* 출입구 != 산봉우리
* 임의의 두 지점 사이에 이동 가능한 경로가 항상 존재
* return 하는 배열은 [산봉우리의 번호, intensity의 최솟값] 순서
*
* [문제 설명]
* 갈때 최소이면, 올 때도 최소임
* gate에서 시작지점으로 하고, summit에서 도착하는 경로만 구하면 됨
*
* gate와 summit은 단방향, 나머지 지점은 양방향
* gate는 출발만 가능
* summit은 도착만 가능
*
* 모든 gate에서 출발하는 다익스트라를 만들고,
* summit에 도착하면 필요시 정답 갱신
*
* 한 gate에서 여러 summit으로 갈 수 있으니 다익스트라가 적합
*
* 다익스트라 가중치는 누적되는 것이 이동 경로중 최대만을 저장
* if(intensive[nn] <= max(intensive[cn], w)) continue; -> 가중치가 증가하는 방향으로만 이동
*
*
* */
public class Solution_등산코스정하기 {

    private static class Solution {

        private static List<Node>[] graph;

        public static int[] solution(int n, int[][] paths, int[] gates, int[] summits) {
            graph = new List[n+1];
            for(int i=0; i<n+1; i++){
                graph[i] = new ArrayList<>();
            }

            Set<Integer> gateSet = new HashSet<>();
            Set<Integer> summitSet = new HashSet<>();

            for(int g : gates){
                gateSet.add(g);
            }
            for(int s : summits){
                summitSet.add(s);
            }

            for(int[] path : paths){
                int a = path[0];
                int b = path[1];
                int w = path[2];

                if(gateSet.contains(a) || summitSet.contains(b)){ // gate에서 출발하거나 summit에 도착
                    graph[a].add(new Node(b, w));
                }else if(gateSet.contains(b) || summitSet.contains(a)){ // 그 반대
                    graph[b].add(new Node(a, w));
                }else{ // 양방향
                    graph[a].add(new Node(b, w));
                    graph[b].add(new Node(a, w));
                }
            }

            // 각 지점마다 intensive 구하기
            int[] intensive = dijkstra(n, gateSet);


            // 정답 도출
            Arrays.sort(summits);
            int minIntensive = Integer.MAX_VALUE;
            int minSummit = Integer.MAX_VALUE;
            for(int s : summits){
                if(intensive[s] < minIntensive){
                    minIntensive = intensive[s];
                    minSummit = s;
                }
            }

            return new int[]{minSummit, minIntensive};
        }

        private static int[] dijkstra(int n, Set<Integer> gateSet) {
            Queue<Node> pq = new LinkedList<>();
            int[] intensive = new int[n+1];
            Arrays.fill(intensive, Integer.MAX_VALUE);

            for(int gate : gateSet){
                pq.add(new Node(gate, 0));
                intensive[gate] = 0;
            }

            while (!pq.isEmpty()){
                Node cn = pq.poll();

                if(intensive[cn.to] < cn.w) continue; // 최소를 갱신할 수 없을 때

                for(Node nn : graph[cn.to]){
                    int max = Math.max(intensive[cn.to], nn.w);
                    if(intensive[nn.to] > max){
                        intensive[nn.to] = max;
                        pq.add(new Node(nn.to, max));
                    }
                }
            }

            return intensive;
        }

        private static class Node{
            int to, w;

            public Node(int to, int w) {
                this.to = to;
                this.w = w;
            }
        }
    }

    public static void main(String[] args) throws IOException {
//        int n = 6;
//        int[][] paths = {
//                {1, 2, 3}, {2, 3, 5}, {2, 4, 2}, {2, 5, 4},
//                {3, 4, 4}, {4, 5, 3}, {4, 6, 1}, {5, 6, 1}
//        };
//        int[] gates = {1, 3};
//        int[] summits = {5};
        int n = 7;
        int[][] paths = {
                {1, 4, 4}, {1, 6, 1}, {1, 7, 3}, {2, 5, 2}, {3, 7, 4}, {5, 6, 6}
        };
        int[] gates = {2};
        int[] summits = {3, 4};

        System.out.println(Arrays.toString(Solution.solution(n, paths, gates, summits)));
    }
}
