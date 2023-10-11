import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class BOJ_14438_수열과쿼리17 {
    static class SegmentTree {
        int n;
        int[] nodeArr;

        public SegmentTree(int[] arr) {
            n = arr.length;
            nodeArr = new int[n * 4];
            init(0, n-1, 1, arr);
        }

        private int init(int left, int right, int nodeIdx, int[] arr) {
            if(left == right) return nodeArr[nodeIdx] = arr[left];

            int mid = (left + right) / 2;
            int leftMin = init(left, mid, nodeIdx * 2, arr);
            int rightMin = init(mid + 1, right, nodeIdx * 2 + 1, arr);
            return nodeArr[nodeIdx] = Math.min(leftMin, rightMin);
        }

        private int query(int left, int right, int nodeIdx, int nodeLeft, int nodeRight) {
            //두 구간의 교집합이 없는 경우
            if(right < nodeLeft || nodeRight < left) return Integer.MAX_VALUE;

            //left..right 구간이 nodeLeft..nodeRight 구간을 완전히 포함하는 경우
            if(left <= nodeLeft && nodeRight <= right) return nodeArr[nodeIdx];

            int mid = (nodeLeft + nodeRight) / 2;
            int leftMin = query(left, right, nodeIdx * 2, nodeLeft, mid);
            int rightMin = query(left, right, nodeIdx * 2 + 1, mid + 1, nodeRight);
            return Math.min(leftMin, rightMin);
        }

        private int update(int index, int newValue, int nodeIdx, int nodeLeft, int nodeRight) {
            //index가 nodeLeft..nodeRight 구간에 포함하지 않는 경우
            if(index < nodeLeft || nodeRight < index) return nodeArr[nodeIdx];

            //리프 노드까지 내려온 경우
            if(nodeLeft == nodeRight) return nodeArr[nodeIdx] = newValue;

            int mid = (nodeLeft + nodeRight) / 2;
            int leftMin = update(index, newValue, nodeIdx * 2, nodeLeft, mid);
            int rightMin = update(index, newValue, nodeIdx * 2 + 1, mid + 1, nodeRight);
            return nodeArr[nodeIdx] = Math.min(leftMin, rightMin);
        }

        public int query(int left, int right) {
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

        int N, M;
        N = Integer.parseInt(br.readLine());
        int[] arr = new int[N];

        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < N; i++) arr[i] = Integer.parseInt(st.nextToken());

        SegmentTree segmentTree = new SegmentTree(arr);

        M = Integer.parseInt(br.readLine());
        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());
            int type = Integer.parseInt(st.nextToken());

            if(type == 1) {
                int index = Integer.parseInt(st.nextToken()) - 1;
                int newValue = Integer.parseInt(st.nextToken());
                segmentTree.update(index, newValue);
            }
            else {
                int start = Integer.parseInt(st.nextToken()) - 1;
                int end = Integer.parseInt(st.nextToken()) - 1;
                if(start > end) {
                    int temp = start;
                    start = end;
                    end = temp;
                }
                sb.append(segmentTree.query(start, end)).append('\n');
            }
        }
        System.out.print(sb);
    }
}