package segmentTree_with_lazy_propagation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * 풀이 시작 : 9:30
 * 풀이 완료 : 10:24
 * 풀이 시간 :
 *
 * 문제 해석
 * 수 N개, 수 변경 횟수 M번, 구간합 쿼리 K번
 * 쿼리 결과 출력하기
 *
 * 구해야 하는 것
 * 쿼리 결과 출력하기
 *
 * 문제 입력
 * 첫째 줄 : N, M, K
 * 둘째 줄 ~ N개 줄 : 초기값 A[i]
 * 다음 줄 ~ M + K줄 : 쿼리
 *  - 1 i j v : i ~ j구간에 v 더하기
 *  - 2 i j : i ~ j 구간합 구하기
 *
 * 제한 요소
 * 1 <= N <= 1_000_000
 * 1 <= M <= 10000
 * 1 <= K <= 10000
 * Long.MIN_VALUE <=모든 입력 수 <= Long.MAX_VALUE
 *
 * 생각나는 풀이
 * 세그먼트 트리, 일정 구간이 변경되므로 lazy propagation
 *
 * 구현해야 하는 기능
 * 1. 세그먼트 트리
 * 2. lazy 배열
 * init()
 * update()
 * sum()
 *
 */
public class BOJ_10999_구간합구하기2 {
    static int N;
    static long[] arr;
    static long[] segTree;
    static long[] lazy;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        int Q = Integer.parseInt(st.nextToken()) + Integer.parseInt(st.nextToken());
        arr = new long[N + 1];
        int exp = (int) Math.ceil(Math.log(N) / Math.log(2)) + 1;
        int treeSize = 1 << exp;
        segTree = new long[treeSize];
        lazy = new long[treeSize];

        for (int i = 1; i <= N; i++) arr[i] = Long.parseLong(br.readLine());
        init(1, N, 1);
        StringBuilder sb = new StringBuilder();
        while (Q-- > 0) {
            st = new StringTokenizer(br.readLine());
            if (st.nextToken().charAt(0) == '1') {
                int a = Integer.parseInt(st.nextToken());
                int b = Integer.parseInt(st.nextToken());
                long v = Long.parseLong(st.nextToken());
                update(1, N, 1, a, b, v);
            } else {
                int a = Integer.parseInt(st.nextToken());
                int b = Integer.parseInt(st.nextToken());

                sb.append(sum(1, N, 1, a, b)).append('\n');
            }
        }
        System.out.println(sb);
    }

    private static long sum(int start, int end, int node, int left, int right) {
        updateLazy(start, end, node);
        if (right < start || end < left) return 0;
        if (left <= start && end <= right) return segTree[node];
        int mid = (start + end) >> 1;
        return sum(start, mid, node << 1, left, right) + sum(mid + 1, end, (node << 1) + 1, left, right);
    }


    private static void update(int start, int end, int node, int left, int right, long value) {
        updateLazy(start, end, node);
        if (right < start || end < left) return;
        // 현재 node의 구간이 전부 바뀌어야 하는 값이라면
        // lazy[node]에 저장할 수 있음
        if (left <= start && end <= right) {
            segTree[node] += (end - start + 1) * value; // 바뀌어야 하는 노드 수만큼 곱해서 저장
            // 리프 노드가 아닐 때에는 자식에게 lazy값을 넘겨야 함
            if (start != end) {
                lazy[node << 1] += value;
                lazy[(node << 1) + 1] += value;
            }
            return;
        }
        int mid = (start + end) >> 1;
        update(start, mid, node << 1, left, right, value);
        update(mid + 1, end, (node << 1) + 1, left, right, value);
        segTree[node] = segTree[node << 1] + segTree[(node << 1) + 1];
    }

    // lazy 배열에 값이 있다면 현재 노드를 업데이트 해주는 메서드
    private static void updateLazy(int start, int end, int node) {
        if (lazy[node] != 0) {
            // lazy[node] = 현재 구간의 모든 자식이 바뀌어야 하는 값
            // 그렇다면 segTree[node]는 구간의 크기 * lazy[node] 만큼 바뀌어야 함
            segTree[node] += (end - start + 1) * lazy[node];
            // 리프노드가 아닐 때 자식에게 lazy 값 넘겨줌
            if (start != end) {
                lazy[node << 1] += lazy[node];
                lazy[(node << 1) + 1] += lazy[node];
            }
            // lazy값 사용했으니 0으로
            lazy[node] = 0;
        }
    }

    private static void init(int start, int end, int node) {
        if (start == end) {
            segTree[node] = arr[start];
            return;
        }
        int mid = (start + end) >> 1;
        init(start, mid, node << 1);
        init(mid + 1, end, (node << 1) + 1);
        segTree[node] = segTree[node << 1] + segTree[(node << 1) + 1];
    }

}