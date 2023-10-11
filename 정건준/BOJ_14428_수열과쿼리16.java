import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class BOJ_14428_수열과쿼리16 {

    static class SegmentTree {
        int n;
        int[] minIndex;
        int[] arr;

        public SegmentTree(int[] arr) {
            n = arr.length;
            minIndex = new int[n * 4];
            this.arr = arr;

            init(0, n-1, 1);
        }

        private int init(int left, int right, int node) {
            if(left == right) {
                return minIndex[node] = left;
            }

            int mid = (left + right) / 2;
            int leftResult = init(left, mid, node * 2);
            int rightResult = init(mid + 1, right, node * 2 + 1);

            minIndex[node] = (arr[leftResult] <= arr[rightResult]) ? leftResult : rightResult;
            return minIndex[node];
        }

        private int query(int left, int right, int node, int nodeLeft, int nodeRight) {
            //두 구간의 교집합이 없는 경우
            if(right < nodeLeft || nodeRight < left) return -1;

            //left..right 구간이 nodeLeft..nodeRight 구간을 완전히 포함하는 경우
            if(left <= nodeLeft && nodeRight <= right) return minIndex[node];

            int mid = (nodeLeft + nodeRight) / 2;
            int leftResult = query(left, right, node * 2, nodeLeft, mid);
            int rightResult = query(left, right, node * 2 + 1, mid + 1, nodeRight);

            if(leftResult == -1) return rightResult;
            if(rightResult == -1) return leftResult;
            return (arr[leftResult] <= arr[rightResult]) ? leftResult : rightResult;
        }
        
        private int update(int index, int newValue, int node, int nodeLeft, int nodeRight) {
            //index가 nodeLeft..nodeRight 구간에 포함하지 않는 경우
            if(index < nodeLeft || nodeRight < index) return minIndex[node];
            
            //리프 노드까지 내려온 경우
            if(nodeLeft == nodeRight) {
                arr[index] = newValue;
                return minIndex[node];
            }

            int mid = (nodeLeft + nodeRight) / 2;
            int leftResult = update(index, newValue, node * 2, nodeLeft, mid);
            int rightResult = update(index, newValue, node * 2 + 1, mid + 1, nodeRight);
            return minIndex[node] = (arr[leftResult] <= arr[rightResult]) ? leftResult : rightResult;
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
                sb.append(segmentTree.query(start, end) + 1).append('\n');
            }
        }
        System.out.print(sb);
    }
}
