import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

public class BOJ_14427_수열과쿼리15_PQ {
    static int N;
    static int[] arr;
    static class Pair implements Comparable<Pair> {
        int value, idx;

        public Pair(int value, int idx) {
            this.value = value;
            this.idx = idx;
        }

        @Override
        public int compareTo(Pair o) {
            if (this.value == o.value) return this.idx - o.idx;
            return this.value - o.value;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;
        N = Integer.parseInt(br.readLine());
        arr = new int[N + 1];

        PriorityQueue<Pair> pq = new PriorityQueue();
        st = new StringTokenizer(br.readLine());
        for (int i = 1; i <= N; i++) {
            arr[i] = Integer.parseInt(st.nextToken());
            pq.offer(new Pair(arr[i], i));
        }

        int M = Integer.parseInt(br.readLine());
        StringBuilder sb = new StringBuilder();
        while (M-- > 0) {
            st = new StringTokenizer(br.readLine());
            if (Integer.parseInt(st.nextToken()) == 2) {
                // 꺼내는 값이 최신값이 아니면 버림
                while (pq.peek().value != arr[pq.peek().idx]) pq.poll();
                sb.append(pq.peek().idx).append('\n');
            } else {
                int idx = Integer.parseInt(st.nextToken());
                int value = Integer.parseInt(st.nextToken());
                // 값을 업데이트하고 pq에 넣음
                arr[idx] = value;
                pq.offer(new Pair(value, idx));
            }
        }
        System.out.println(sb);
    }
}