import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * 풀이 시작 : 5:16
 * 풀이 완료 : 5:35
 * 풀이 시간 :
 *
 * 문제 해석
 * N일간의 가계부 프로그램 작성
 * 생후 p일에 수입, 지출 내용 추가
 * 수입은 양수, 지출은 음수
 * 생후 p일부터 q일까지 잔고가 변화한 양
 * 기능
 * 1 p x : 생후 p일에 x를 추가
 * 2 p q : 생후 p일부터 q일까지 변화한 양을 출력
 *
 * 구해야 하는 것
 * Q번 쿼리 수행 결과
 *
 * 문제 입력
 * 첫째 줄 : N, Q
 * 둘째 줄 ~ Q개 줄 : 쿼리
 *
 * 제한 요소
 * N <= 10^6
 * Q <= 10^5
 * -2*10^9 <= x < 2*10^9
 *
 * 생각나는 풀이
 * 세그먼트 트리
 * 한 번의 입력은 int 내지만 여러 번 더해질 수 있기 때문에 long 사용해야 할듯
 *
 * 구현해야 하는 기능
 * 1. 세그먼트 트리 update 메서드
 * 2. 세그먼트 트리 sum 메서드
 */
public class BOJ_12837_가계부Hard {
    static int N;
    static long[] num;
    static long[] segTree;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        int Q = Integer.parseInt(st.nextToken());
        int exp = (int) Math.ceil(Math.log(N) / Math.log(2)) + 1;
        int treeSize = 1 << exp;
        num = new long[N + 1];
        segTree = new long[treeSize];

        StringBuilder sb = new StringBuilder();

        while (Q-- > 0) {
            st = new StringTokenizer(br.readLine());
            int query = Integer.parseInt(st.nextToken());
            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());

            if (query == 1) {
                update(1, N, 1, a, b);
            } else {
                sb.append(sum(1, N, 1, a, b)).append('\n');
            }
        }
        System.out.println(sb);
    }

    private static long sum(int start, int end, int idx, int left, int right) {
        if (right < start || end < left) return 0;
        if (left <= start && end <= right) return segTree[idx];

        int mid = (start + end) >> 1;

        return sum(start, mid, idx << 1, left, right) + sum(mid + 1, end, (idx << 1) + 1, left, right);
    }

    private static void update(int start, int end, int idx, int arrIdx, int diff) {
        if (arrIdx < start || end < arrIdx) return;
        segTree[idx] += diff;
        if (start == end) return;

        int mid = (start + end) >> 1;
        update(start, mid, idx << 1, arrIdx, diff);
        update(mid + 1, end, (idx << 1) + 1, arrIdx, diff);
    }

}