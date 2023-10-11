package kr.ac.lecture.baekjoon.Num10001_20000;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/*
* [문제 요약]
* 두 가지 쿼리 수행
*
* [제약 조건]
* 수열의 크기 (1 ≤ N ≤ 100,000)
* 값 범위 (1 ≤ Ai ≤ 10^9)
* 쿼리 개수 (1 ≤ M ≤ 100,000)
*
* [문제 설명]
* 가장 작은 값이니 min을 구하는 세그트리 구현
*
* */
public class Main_BJ_14438_수열과쿼리17 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer stz;

        int n = Integer.parseInt(br.readLine());
        int[] arr = new int[n+1];

        stz =new StringTokenizer(br.readLine());
        for (int i = 1; i < n+1; i++) {
            arr[i] = Integer.parseInt(stz.nextToken());
        }

        SegmentTree segmentTree = new SegmentTree(n);
        segmentTree.init(arr, 1, 1, n);

        int m = Integer.parseInt(br.readLine());
        while (m-- > 0){
            stz = new StringTokenizer(br.readLine());
            int a = Integer.parseInt(stz.nextToken());
            int b = Integer.parseInt(stz.nextToken());
            int c = Integer.parseInt(stz.nextToken());

            if(a == 1){
                arr[b] = c;
                segmentTree.update(1, 1, n, b, c);
            }else{
                System.out.println(segmentTree.query(1, 1, n, b, c));
            }
        }

        br.close();
    }

    private static class SegmentTree{
        int[] tree;

        SegmentTree(int n){
            tree = new int[4*n];
        }

        int init(int[] arr, int node, int left, int right){
            if(left == right){
                return tree[node] = arr[left];
            }

            int mid = (left + right) >> 1;
            int leftMin = init(arr, node << 1, left, mid);
            int rightMin = init(arr, (node << 1)+1, mid + 1, right);

            return tree[node] = Math.min(leftMin, rightMin);
        }

        int update(int node, int left, int right, int index, int value){
            if(index < left || index > right){
                return tree[node];
            }

            if(left == right){
                return tree[node] = value;
            }


            int mid = (left + right) >> 1;
            int leftMin = update(node << 1, left, mid, index, value);
            int rightMin = update((node << 1)+1, mid + 1, right, index, value);

            return tree[node] = Math.min(leftMin, rightMin);
        }

        int query(int node, int left, int right, int low, int high){
            if(right < low || left > high){
                return Integer.MAX_VALUE;
            }

            if(low <= left && right <= high){
                return tree[node];
            }

            int mid = (left + right) >> 1;
            int leftMin = query(node << 1, left, mid, low, high);
            int rightMin = query((node << 1)+1, mid + 1, right, low, high);

            return Math.min(leftMin, rightMin);
        }
    }
}
