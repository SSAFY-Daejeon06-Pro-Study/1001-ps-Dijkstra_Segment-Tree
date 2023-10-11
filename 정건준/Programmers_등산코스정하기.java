import java.util.*;
/***
 [문제]
 산은 n개의 지점, 각 지점은 1부터 n까지 번호가 붙어 있으며 각 지점은 출입구, 쉼터, 산봉우리 중 하나
 등산 코스는 방문 지점 번호들을 순서대로 나열해 표현 가능
 쉼터 혹은 산봉우리를 방문할 때마다 휴식을 취할 수 있음
 휴식 없이 이동하는 가장 긴 시간을 해당 등산 코스의 itensity

 출입구 중 하나를 출발하여 산봉우리 중 한 곳만 방문한뒤 다시 원래의 출입구로 돌아오는 등산 코스중
 itensity가 가장 작은 코스의 산봉우리 지점 번호와 itensity를 출력
 (등산 코스에서 출입구는 처음와 끝에 한 번씩(서로 같음), 산봉 우리는 중간에 한번만 포함)
 (itensity가 최소가 되는 등산 코스가 여러개라면 그 중 산봉우리의 번호가 가장 낮은 등산 코스 선택)

 [제약 사항]
 지점 수 (2 <= n <= 50,000)
 간선 수 paths (1 <= paths <= 200,000)
 연결되지 않은 지점은 없음
 간선의 w가 최대 10,000,000
 출발지이면서 산봉우리인 지점은 없음

 출발지에서 산봉우리로 가는 편도 경로만 고려 (돌아오는 경로의 itensity도 같기 때문)
 최단 경로는 거리의 합이 제일 작은 경로가 아닌 경로 중 간선의 최대 가중치가 제일 작은 경로
 이걸 다익스트라로?..

 우선순위 큐에 모든 출발지를 넣으면?
 각 정점(산봉우리 or 쉼터)을 최단 거리로 방문 (여러번 발견할 수 있지만 방문은 한번)
 큐가 빌때까지 수행하면서 경로 중 itensity, 산봉우리 정점의 번호가 가장 작은 경로를 찾음
 */


class Solution {
    static class Node {
        int to;
        int distance;
        Node next;
        Node(int to, int distance, Node next) {
            this.to = to;
            this.distance = distance;
            this.next = next;
        }
    }
    static class PQNode implements Comparable<PQNode> {
        int v;
        int intensity; //v 정점까지 가는 경로의 intensity
        PQNode(int v, int intensity) {
            this.v = v;
            this.intensity = intensity;
        }
        public int compareTo(PQNode o) {
            return Integer.compare(this.intensity, o.intensity);
        }
    }

    public int[] solution(int n, int[][] paths, int[] gates, int[] summits) {
        int[] answer = {};

        //1. 그래프 구축
        Node[] adjList = new Node[n+1];
        for(int i=0; i<paths.length; i++) {
            int to = paths[i][0];
            int from = paths[i][1];
            int distance = paths[i][2];
            adjList[to] = new Node(from, distance, adjList[to]);
            adjList[from] = new Node(to, distance, adjList[from]);
        }

        boolean[] summitsCheck = new boolean[n+1];
        for(int i=0; i<summits.length; i++) {
            summitsCheck[summits[i]] = true;
        }

        //2. 다익스트라 수행
        answer = new int[2];
        answer[0] = Integer.MAX_VALUE;
        answer[1] = Integer.MAX_VALUE;

        int[] intensities = new int[n+1];
        Arrays.fill(intensities, Integer.MAX_VALUE);
        PriorityQueue<PQNode> pq = new PriorityQueue<>();

        for(int i=0; i<gates.length; i++) {
            pq.offer(new PQNode(gates[i], 0));
        }

        while(!pq.isEmpty()) {
            PQNode pqNode = pq.poll();
            int v = pqNode.v;
            int intensity = pqNode.intensity;

            //해당 경로 무시
            if(intensities[v] < intensity) continue;

            //방문한 정점이 산봉우리인 경우
            if(summitsCheck[v]) {
                //intensity보다 낮은 산봉우리는 다 방문했으므로 반복 종료
                if(answer[1] < intensity) {
                    break;
                }
                if(v < answer[0]) {
                    answer[0] = v;
                    answer[1] = intensity;
                }
                //산봉우리는 도착점이므로 continue
                continue;
            }

            for(Node adjNode=adjList[v]; adjNode!=null; adjNode=adjNode.next) {
                int newIntensity = Math.max(intensity, adjNode.distance);
                if(newIntensity < intensities[adjNode.to]) {
                    intensities[adjNode.to] = newIntensity;
                    pq.offer(new PQNode(adjNode.to, newIntensity));
                }
            }
        }
        return answer;
    }
}
