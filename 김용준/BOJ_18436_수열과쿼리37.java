import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * 풀이 시작 : 7:51
 * 풀이 완료 : 8:14
 * 풀이 시간 :
 *
 * 문제 해석
 * 수열과 쿼리
 * 길이 N 수열과 쿼리 M개가 주어질 때
 * 쿼리 수행 결과를 출력
 * 1 i x : Ai를 x로 바꿈
 * 2 l r : l <= i <= r 인 모든 Ai 중에서 짝수 개수 출력
 * 3 l r : l <= i <= r 인 모든 Ai 중에서 홀수 개수 출력
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
 * 조금의 변형
 * 원본 수를 저장하는 게 아니라 2로 나눈 나머지만 저장
 *  - 짝수 = 0, 홀수 = 1
 * 그러면 구간합이 홀수의 개수가 됨
 * 홀수의 개수를 알면 짝수의 개수를 구간에서 홀수를 뺀 것
 *
 * 구현해야 하는 기능
 * 1. init
 * 2. update
 * 3. getOdd()
 *
 */
public class BOJ_18436_수열과쿼리37 {
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
        // 배열에 저장하는 값 = 2로 나눈 나머지
        for (int i = 1; i <= N; i++) arr[i] = Integer.parseInt(st.nextToken()) & 1;
        init(1, N, 1);

        int M = Integer.parseInt(br.readLine());
        StringBuilder sb = new StringBuilder();
        while (M-- > 0) {
            st = new StringTokenizer(br.readLine());
            int query = Integer.parseInt(st.nextToken());
            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());

            if (query == 1) {
                b = b & 1; // 2로 나눈 나머지
                if (arr[a] == b) continue; // 나머지가 같으면 업데이트 불필요
                update(1, N, 1, a, b);
            } else if (query == 2) {
                // 구간 총 요소 개수 - 홀수의 개수 = 짝수 개수
                sb.append(b - a + 1 - findOdd(1, N, 1, a, b)).append('\n');
            } else {
                sb.append(findOdd(1, N, 1, a, b)).append('\n');
            }
        }
        System.out.println(sb);
    }

    private static int update(int start, int end, int idx, int arrIdx, int value) {
        if (arrIdx < start || end < arrIdx) return segTree[idx];
        if (start == end) return segTree[idx] = arr[arrIdx] = value;
        int mid = (start + end) >> 1;
        return segTree[idx] = update(start, mid, idx << 1, arrIdx, value) + update(mid + 1, end, (idx << 1) + 1, arrIdx, value);
    }

    private static int findOdd(int start, int end, int idx, int left, int right) {
        if (right < start || end < left) return 0;
        if (left <= start && end <= right) return segTree[idx];
        int mid = (start + end) >> 1;
        return findOdd(start, mid, idx << 1, left, right) + findOdd(mid + 1, end, (idx << 1) + 1, left, right);
    }

    private static int init(int start, int end, int idx) {
        if (start == end) return segTree[idx] = arr[start];
        int mid = (start + end) >> 1;
        return segTree[idx] = init(start, mid, idx << 1) + init(mid + 1, end, (idx << 1) + 1);
    }

}