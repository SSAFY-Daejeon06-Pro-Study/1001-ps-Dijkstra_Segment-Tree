import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * 풀이 시작 : 10:46
 * 풀이 완료 :
 * 풀이 시간 :
 *
 * 문제 해석
 * 수열과 쿼리
 * 쿼리 종류
 * 1 i v : Ai를 v로 바꿈
 * 2 : 수열에서 크기가 가장 작은 값의 인덱스 출력, 작은 값이 여러개면 가장 작은 인덱스 출력
 *
 * 구해야 하는 것
 * 쿼리 결과
 *
 * 문제 입력
 * 첫째 줄 : 수열 크기 N
 * 둘째 줄 : 수열의 원소
 * 셋째 줄 : 쿼리 개수 M
 * 넷째 줄 ~ M개 줄 : 쿼리
 *
 * 제한 요소
 * 1 <= N <= 100000
 * 1 <= M <= 100000
 * 1 <= v <= 10^9
 *
 * 생각나는 풀이
 * 세그먼트 트리
 * pq도 가능할듯
 *
 * 구현해야 하는 기능
 * 1. 세그먼트 트리
 * 2. pq
 *
 */
public class BOJ_14427_수열과쿼리15 {
    static int N;
    static int[] arr;
    static int[] segTree;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;
        N = Integer.parseInt(br.readLine());
        arr = new int[N + 1];
        // 세그먼트 트리의 높이
        int exp = (int) (Math.ceil(Math.log(N) / Math.log(2)) + 1);
        // 세그먼트 트리의 크기
        int treeSize = (int) Math.pow(2, exp);
        segTree = new int[treeSize];

        st = new StringTokenizer(br.readLine());
        for (int i = 1; i <= N; i++) {
            arr[i] = Integer.parseInt(st.nextToken());
        }
        init(1, N, 1);

        int M = Integer.parseInt(br.readLine());
        StringBuilder sb = new StringBuilder();
        while (M-- > 0) {
            st = new StringTokenizer(br.readLine());
            if (Integer.parseInt(st.nextToken()) == 2) {
                sb.append(segTree[1]).append('\n');
            } else {
                int idx = Integer.parseInt(st.nextToken());
                int value = Integer.parseInt(st.nextToken());
                arr[idx] = value;
                update(1, N, 1, idx);
            }
        }
        System.out.println(sb);
    }

    private static int update(int start, int end, int idx, int arrIdx) {
        if (arrIdx < start || end < arrIdx) return segTree[idx];
        if (start == end) return segTree[idx] = arrIdx;

        int mid = (start + end) >> 1;
        int l = update(start, mid, idx << 1, arrIdx);
        int r = update(mid + 1, end, (idx << 1) + 1, arrIdx);

        // 수열에 저장된 값으로 비교하고 세그먼트 트리에 저장하는건 인덱스
        return segTree[idx] = arr[l] <= arr[r] ? l : r;
    }

    private static void init(int start, int end, int idx) {
        if (start == end) {
            segTree[idx] = start;
            return;
        }

        int mid = (start + end) >> 1;
        init(start, mid, idx << 1);
        init(mid + 1, end, (idx << 1) + 1);

        int l = segTree[idx << 1];
        int r = segTree[(idx << 1) + 1];

        // 수열에 저장된 값으로 비교하고 세그먼트 트리에 저장하는건 인덱스
        segTree[idx] = arr[l] <= arr[r] ? l : r;
    }
}