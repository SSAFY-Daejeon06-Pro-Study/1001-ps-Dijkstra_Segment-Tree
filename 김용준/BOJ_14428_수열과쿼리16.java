import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * 풀이 시작 : 5:37
 * 풀이 완료 : 5:55
 * 풀이 시간 :
 *
 * 문제 해석
 * 수열과 쿼리
 * 길이 N인 수열 A1 ~ AN이 주어짐
 * 쿼리 M개
 * 쿼리 수행 프로그램 작성
 * 1 i v : Ai를 v로 바꿈
 * 2 i j : Ai ~ Aj에서 크기가 가장 작은 값의 인덱스 출력, 같으면 작은 인덱스
 *
 * 구해야 하는 것
 * 쿼리 수행 결과
 *
 * 문제 입력
 * 첫째 줄 : N
 * 둘째 줄 : A1 ~ AN
 * 셋째 줄 : M
 * 넷째 줄 ~ M개 줄 : 쿼리
 *
 * 제한 요소
 * 1 <= N <= 100000
 * 1 <= Ai <= 10^9
 * 1 <= M <= 100000
 *
 * 생각나는 풀이
 * 세그먼트 트리
 *
 * 구현해야 하는 기능
 * 1. init 메서드
 * 2. update 메서드
 * 3. getMinIndex 메서드
 *
 */
public class BOJ_14428_수열과쿼리16 {
    static int N;
    static int[] arr;
    static int[] segTree;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        N = Integer.parseInt(br.readLine());
        // 세그먼트 트리의 높이
        int exp = (int) Math.ceil(Math.log(N) / Math.log(2)) + 1;
        // 세그먼트 트리의 크기
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
                arr[a] = b;
                update(1, N, 1, a);
            } else {
                sb.append(getMinIndex(1, N, 1, a, b)).append('\n');
            }
        }

        System.out.println(sb);
    }

    private static void update(int start, int end, int idx, int arrIdx) {
        if (end < arrIdx || arrIdx < start) return;
        if (start == end) return;

        int mid = (start + end) >> 1;
        update(start, mid, idx << 1, arrIdx);
        update(mid + 1, end, (idx << 1) + 1, arrIdx);

        int l = segTree[idx << 1];
        int r = segTree[(idx << 1) + 1];

        segTree[idx] = arr[l] <= arr[r] ? l : r;
    }

    private static int getMinIndex(int start, int end, int idx, int left, int right) {
        if (right < start || end < left) return -1;
        if (left <= start && end <= right) return segTree[idx];

        int mid = (start + end) >> 1;
        int l = getMinIndex(start, mid, idx << 1, left, right);
        int r = getMinIndex(mid + 1, end, (idx << 1) + 1, left, right);

        if (l == -1) return r;
        else if (r == -1) return l;
        else return arr[l] <= arr[r] ? l : r;
    }

    private static int init(int start, int end, int idx) {
        if (start == end) {
            segTree[idx] = start;
            return start;
        }
        int mid = (start + end) >> 1;
        int l = init(start, mid, idx << 1);
        int r = init(mid + 1, end, (idx << 1) + 1);
        return segTree[idx] = arr[l] <= arr[r] ? l : r;
    }

}