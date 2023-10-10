import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class BOJ_18436_수열과쿼리37 {

    //구간은 짝수의 개수와 홀수의 개수를 저장
    static class Segment {
        int odd;
        int even;
        Segment(int odd, int even) {
            this.odd = odd;
            this.even = even;
        }
    }

    static class SegmentTree {
        int n;
        Segment[] segments;

        public SegmentTree(int[] arr) {
            n = arr.length;
            segments = new Segment[n * 4];
            init(0, n-1, 1, arr);
        }

        private Segment merge(Segment a, Segment b) {
            return new Segment(a.odd + b.odd, a.even + b.even);
        }

        private Segment init(int left, int right, int node, int[] arr) {
            if(left == right) {
                if(arr[left] % 2 == 0) segments[node] = new Segment(0, 1);
                else segments[node] = new Segment(1, 0);
                return segments[node];
            }

            int mid = (left + right) / 2;
            Segment leftSegment = init(left, mid, node * 2, arr);
            Segment rightSegment = init(mid + 1, right, node * 2 + 1, arr);
            return segments[node] = merge(leftSegment, rightSegment);
        }

        private Segment query(int left, int right, int node, int nodeLeft, int nodeRight) {
            //두 구간의 교집합이 없는 경우
            if(right < nodeLeft || nodeRight < left) return null;

            //left..right 구간이 nodeLeft..nodeRight 구간을 완전히 포함하는 경우
            if(left <= nodeLeft && nodeRight <= right) return segments[node];

            int mid = (nodeLeft + nodeRight) / 2;
            Segment leftSegment = query(left, right, node * 2, nodeLeft, mid);
            Segment rightSegment = query(left, right, node * 2 + 1, mid + 1, nodeRight);

            if(leftSegment == null) return rightSegment;
            if(rightSegment == null) return leftSegment;
            return merge(leftSegment, rightSegment);
        }

        private Segment update(int index, int newValue, int node, int nodeLeft, int nodeRight) {
            //index가 nodeLeft..nodeRight 구간에 포함하지 않는 경우
            if(index < nodeLeft || nodeRight < index) return segments[node];

            //리프 노드까지 내려온 경우
            if(nodeLeft == nodeRight) {
                if(newValue % 2 == 0) {
                    segments[node].odd = 0;
                    segments[node].even = 1;
                }
                else {
                    segments[node].odd = 1;
                    segments[node].even = 0;
                }
                return segments[node];
            }

            int mid = (nodeLeft + nodeRight) / 2;
            Segment leftSegment = update(index, newValue, node * 2, nodeLeft, mid);
            Segment rightSegment = update(index, newValue, node * 2 + 1, mid + 1, nodeRight);
            return segments[node] = merge(leftSegment, rightSegment);
        }

        public Segment query(int left, int right) {
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

                Segment segment = segmentTree.query(start, end);

                if(type == 2) {
                    sb.append(segment.even).append('\n');
                }
                else {
                    sb.append(segment.odd).append('\n');
                }
            }
        }
        System.out.print(sb);
    }
}
