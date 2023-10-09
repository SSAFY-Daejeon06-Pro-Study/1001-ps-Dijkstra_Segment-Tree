package kr.ac.lecture.baekjoon.Num10001_20000;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/*
 * [문제 요약]
 * 부분 합을 구하여라
 *
 * [제약 조건]
 * N ≤ 10^6
 * Q ≤ 10^5
 *
 * 1 ≤ p ≤ N
 * -2×10^9 ≤ x ≤ 2×10^9
 *
 * [문제 풀이]
 * 세그먼트트리로 부분 합 구하기
 * init은 없음
 *
 *
 * */
public class Main_BJ_12837_가계부_hard {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer stz = new StringTokenizer(br.readLine());

        int n = Integer.parseInt(stz.nextToken());
        int q = Integer.parseInt(stz.nextToken());

        SegmentTree segmentTree = new SegmentTree(n);
        while (q-- > 0) {
            stz = new StringTokenizer(br.readLine());
            int a = Integer.parseInt(stz.nextToken());
            int b = Integer.parseInt(stz.nextToken());
            int c = Integer.parseInt(stz.nextToken());

            if (a == 1) {
                segmentTree.update(1, 1, n, b, c);
            } else {
                System.out.println(segmentTree.query(1, 1, n, b, c));
            }
        }

        br.close();
    }

    private static class SegmentTree {
        long[] tree;

        SegmentTree(int n) {
            tree = new long[4 * n];
        }

        void update(int node, int left, int right, int p, int x) { // p일 후에 x 추가
            if (p < left || p > right) {
                return;
            }

            if (left == right) {
                tree[node] += x;
                return;
            }

            int mid = (left + right) >> 1;
            update(node << 1, left, mid, p, x);
            update((node << 1) + 1, mid + 1, right, p, x);

            tree[node] = tree[node << 1] + tree[(node << 1) + 1];
        }

        long query(int node, int left, int right, int low, int high) {
            if (right < low || left > high) {
                return 0;
            }

            if (low <= left && right <= high) {
                return tree[node];
            }

            // 부분적으로 겹쳤을 때
            int mid = (left + right) >> 1;
            long leftSum = query(node << 1, left, mid, low, high);
            long rightSum = query((node << 1) + 1, mid + 1, right, low, high);

            return leftSum + rightSum;
        }
    }
}



























