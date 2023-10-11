import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/***
 * 구간 합을 빠르게 구하는 문제
 */

public class BOJ_12837_가계부Hard {
    static class SegmentTree {
        int n;
        long[] nodeArr;

        public SegmentTree(int n) {
            this.n = n;
            this.nodeArr = new long[n * 4];
        }

        private long query(int left, int right, int nodeIdx, int nodeLeft, int nodeRight) {
            if(right < nodeLeft || nodeRight < left) return 0;
            if(left <= nodeLeft && nodeRight <= right) return nodeArr[nodeIdx];

            int mid = (nodeLeft + nodeRight) / 2;
            long leftNode = query(left, right, nodeIdx * 2, nodeLeft, mid);
            long rightNode = query(left, right, nodeIdx * 2 + 1, mid + 1, nodeRight);
            return leftNode + rightNode;
        }

        private long update(int index, int newValue, int nodeIdx, int nodeLeft, int nodeRight) {
            if(index < nodeLeft || nodeRight < index) return nodeArr[nodeIdx];
            if(nodeLeft == nodeRight) return nodeArr[nodeIdx] += newValue;

            int mid = (nodeLeft + nodeRight) / 2;
            long leftNode = update(index, newValue, nodeIdx * 2, nodeLeft, mid);
            long rightNode = update(index, newValue, nodeIdx * 2 + 1, mid + 1, nodeRight);
            return nodeArr[nodeIdx] = leftNode + rightNode;
        }

        public long query(int left, int right) {
            return query(left, right, 1, 0, n-1);
        }

        public void update(int index, int newValue) {
            update(index, newValue, 1, 0, n-1);
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;
        StringBuilder sb = new StringBuilder();

        st = new StringTokenizer(br.readLine());
        int N = Integer.parseInt(st.nextToken());
        int M = Integer.parseInt(st.nextToken());

        SegmentTree segmentTree = new SegmentTree(N);

        for(int i=0; i<M; i++) {
            st = new StringTokenizer(br.readLine());
            int type = Integer.parseInt(st.nextToken());

            if(type == 1) {
                int index = Integer.parseInt(st.nextToken()) - 1;
                int value = Integer.parseInt(st.nextToken());
                segmentTree.update(index, value);
            }
            else {
                int start = Integer.parseInt(st.nextToken()) - 1;
                int end = Integer.parseInt(st.nextToken()) - 1;
                sb.append(segmentTree.query(start, end)).append('\n');
            }
        }
        System.out.print(sb);
    }
}
