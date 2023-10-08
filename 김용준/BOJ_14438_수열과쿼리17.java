import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * 풀이 시작 : 6:21
 * 풀이 완료 :
 * 풀이 시간 :
 *
 * 문제 해석
 * 수열과 쿼리
 * 길이 N 수열과 쿼리 M개가 주어질 때
 * 쿼리 수행 결과를 출력
 * 1 i v : Ai를 v로 바꿈
 * 2 i j : Ai ~ Aj 에서 크기가 가장 작은 값 출력
 *
 * 구해야 하는 것
 * 쿼리 수행 결과를 출력
 *
 * 문제 입력
 * 첫째 줄 : N
 * 둘째 줄 : Ai
 * 셋째 줄 : M
 * 넷째 줄 ~ M개 줄 : 쿼리
 *
 * 제한 요소
 * 1 <= N <= 100000
 * 1 <= M <= 100000
 * 1 <= Ai <= 10^9
 *
 * 생각나는 풀이
 * 세그먼트 트리
 *
 * 구현해야 하는 기능
 * 1. init
 * 2. update
 * 3. getMinValue
 *
 */
public class BOJ_14438_수열과쿼리17 {
    static int N;
    static int[] arr;
    static int[] segTree;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        N = Integer.parseInt(br.readLine());
        int exp = (int) Math.ceil(Math.log(N) / Math.log(2)) + 1;
        int treeSize = 1 << exp;

        arr = new int[N + 1];
        segTree = new int[treeSize];
        StringTokenizer st = new StringTokenizer(br.readLine());
        for (int i = 1; i <= N; i++) arr[i] = Integer.parseInt(st.nextToken());
        init(1, N, 1);
        int M = Integer.parseInt(br.readLine());

        StringBuilder sb = new StringBuilder();

        while (M-- > 0) {
            st = new StringTokenizer(br.readLine());
            int query = Integer.parseInt(st.nextToken());
            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());

            if (query == 1) {
                update(1, N, 1, a, b);
            } else {
                sb.append(getMinValue(1, N, 1, a, b)).append('\n');
            }
        }
        System.out.println(sb);
    }

    private static int update(int start, int end, int node, int idx, int value) {
        if (idx < start || end < idx) return segTree[node];
        if (start == end) return segTree[node] = arr[idx] = value;
        int mid = (start + end) >> 1;
        return segTree[node] = Math.min(update(start, mid, node << 1, idx, value), update(mid + 1, end, (node << 1) + 1, idx, value));
    }

    private static int getMinValue(int start, int end, int node, int left, int right) {
        if (right < start || end < left) return Integer.MAX_VALUE;
        if (left <= start && end <= right) return segTree[node];
        int mid = (start + end) >> 1;
        return Math.min(getMinValue(start, mid, node << 1, left, right), getMinValue(mid + 1, end, (node << 1) + 1, left, right));
    }

    private static int init(int start, int end, int node) {
        if (start == end) return segTree[node] = arr[start];
        int mid = (start + end) >> 1;
        return segTree[node] = Math.min(init(start, mid, node << 1), init(mid + 1, end, (node << 1) + 1));
    }

}