package kr.ac.lecture.baekjoon.Num10001_20000;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/*
* [문제 요약]
* 구간의 변경이 빈번히 발생하는 배열의 부분 합
*
* [제약 조건]
* 수 개수 : N(1 ≤ N ≤ 1,000,000)
* 변경 횟수 : M(1 ≤ M ≤ 10,000)
* 구간합 횟수 : K(1 ≤ K ≤ 10,000)
* long 범위
*
* [문제 설명]
* 변경이 특정 구간을 통틀어서 발생하기 때문에 lazy propagation 세그먼트트리를 이용해야 함
*
* 일반 세그먼트의 시간 복잡도는 O(logN), 구간의 길이만큼 update 하면 O(NlogN)이 발생
* 변경 횟수가 M이기 때문에 최종적으로는 O(MNlogN) 만큼의 시간 복잡도가 발생
*
* lazy를 사용하면 구간의 변화량에 대해 계산을 줄일 수 있음
*
* */
public class Main_BJ_10999_구간합구하기2 {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer stz = new StringTokenizer(br.readLine());

        int n = Integer.parseInt(stz.nextToken());
        int m = Integer.parseInt(stz.nextToken());
        int k = Integer.parseInt(stz.nextToken());

        long[] arr = new long[n+1];
        for(int i=1; i<n+1; i++){
            arr[i] = Long.parseLong(br.readLine());
        }

        SegmentTree segmentTree = new SegmentTree(n);
        segmentTree.init(arr, 1, 1, n);

        for(int i=0; i<m+k; i++){
            stz = new StringTokenizer(br.readLine());
            int a = Integer.parseInt(stz.nextToken());
            int b = Integer.parseInt(stz.nextToken());
            int c = Integer.parseInt(stz.nextToken());

            if(a == 1){
                // b~c에 d를 더함
                long d = Long.parseLong(stz.nextToken());
                segmentTree.update(1, 1, n, b, c, d);
            }else{
                // b~c까지의 합
                System.out.println(segmentTree.query(1, 1, n, b, c));
            }
        }

        br.close();
    }

    private static class SegmentTree{
        long[] tree, lazy;

        SegmentTree(int n){
            tree = new long[4*n];
            lazy = new long[4*n];
        }

        long init(long[] arr, int node, int left, int right){
            if(left == right){
                return tree[node] = arr[left];
            }

            int mid = (left + right) >> 1;
            return tree[node] = init(arr, node << 1, left, mid) +
                    init(arr, (node << 1) + 1, mid+1, right);
        }

        // low~high까지 업데이트
        void update(int node, int left, int right, int low, int high, long value){
            lazyUpdate(node, left, right);

            if(right < low || left > high){
                return;
            }

            if(low <= left && right <= high){
                tree[node] += (right - left + 1) * value;
                if(left != right){
                    lazy[node << 1] += value;
                    lazy[(node << 1) + 1] += value;
                }
                return;
            }

            int mid = (left + right) >> 1;
            update(node << 1, left, mid, low, high, value);
            update((node << 1) + 1, mid + 1, right, low, high, value);

            tree[node] = tree[node << 1] + tree[(node << 1) + 1];
        }

        void lazyUpdate(int node, int left, int right) {
            if(lazy[node] != 0){
                tree[node] += (right - left + 1) * lazy[node];
                if(left != right){
                    lazy[node << 1] += lazy[node]; // 자식에게 lazy값 전파
                    lazy[(node << 1) + 1] += lazy[node];
                }
                lazy[node] = 0;
            }
        }

        long query(int node, int left, int right, int low, int high){
            lazyUpdate(node, left, right);

            if(left > high || right < low){
                return 0;
            }

            if(low <= left && right <= high){
                return tree[node];
            }

            int mid = (left + right) >> 1;
            return query(node<<1, left, mid, low, high) +
                    query((node<<1)+1, mid+1, right, low, high);
        }
    }
}
