package kr.ac.lecture.baekjoon.Num10001_20000;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/*
* [문제 요약]
* 두 가지 쿼리를 동작
*
* [제약 조건]
* 수열의 크기 (1 ≤ N ≤ 100,000)
* 배열 값 (1 ≤ Ai ≤ 10^9)
* 쿼리 개수 (1 ≤ M ≤ 100,000)
*
* [문제 설명]
* 세그먼트트로리 할 수 있을 듯
* 세그먼트 트리에 ai를 update로 바꿈
* 전체에서 가장 작은 인덱스 값은 세그먼트트리의 루트 노드임
*
*
* */
public class Main_BJ_14427_수열과쿼리15 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer stz;

        int n = Integer.parseInt(br.readLine());
        int[] arr = new int[n+1];

        stz = new StringTokenizer(br.readLine());
        for (int i = 1; i < n+1; i++) {
            arr[i] = Integer.parseInt(stz.nextToken());
        }

        SegmentTree segmentTree = new SegmentTree(n);
        segmentTree.init(arr, 1, 1, n);

        int m = Integer.parseInt(br.readLine());
        while (m-- > 0){
            stz = new StringTokenizer(br.readLine());
            int a = Integer.parseInt(stz.nextToken());

            if(a == 1){ // update
                int b = Integer.parseInt(stz.nextToken());
                int c = Integer.parseInt(stz.nextToken());

                arr[b] = c;
                segmentTree.update(arr, 1, 1, n, b);

            }else{ // 전체 최소 인덱스 출력 -> 루트
                System.out.println(segmentTree.tree[1]);
            }
        }

        br.close();
    }

    private static class SegmentTree{
        int[] tree;

        SegmentTree(int n){
            tree = new int[n << 2];
        }

        void init(int[] arr, int node, int left, int right){
            if(left == right){
                tree[node] = left;
                return;
            }

            int mid = (left + right) >> 1;
            init(arr, node << 1, left, mid);
            init(arr, (node << 1)+1, mid+1, right);

            int leftIndex = tree[node << 1];
            int rightIndex = tree[(node << 1) + 1];

            tree[node] = (arr[leftIndex] <= arr[rightIndex]) ? leftIndex : rightIndex;
        }

        int update(int[] arr, int node, int left, int right, int index){
            if(index < left || index > right){
                return tree[node];
            }

            if(left == right){
                return tree[node] = index;
            }

            int mid = (left + right) >> 1;
            int leftIndex = update(arr, node << 1, left, mid, index);
            int rightIndex = update(arr, (node << 1)+1, mid+1, right, index);

            return tree[node] = arr[leftIndex] <= arr[rightIndex] ? leftIndex : rightIndex;
        }
    }
}
