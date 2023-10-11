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
* 수열의 크기 N이 주어진다. (1 ≤ N ≤ 100,000)
* 1, A2, ..., AN이 주어진다. (1 ≤ Ai ≤ 109)
* 쿼리의 개수 M이 주어진다. (1 ≤ M ≤ 100,000)
*
* [문제 설명]
* 세그먼트트리
* 인덱스를 저장하고, 인덱스 값 비교를 통해 세그먼트트리 생성
*
* */
public class Main_BJ_14428_수열과쿼리16 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer stz;


        int n = Integer.parseInt(br.readLine());
        int[] arr = new int[n+1];

        stz = new StringTokenizer(br.readLine());
        for(int i=1; i<n+1; i++){
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
                segmentTree.update(arr, 1, 1, n, b);
            }else{
                System.out.println(segmentTree.query(arr, 1, 1, n, b, c));
            }
        }

        br.close();
    }


    private static class SegmentTree{
        int[] tree;

        SegmentTree(int n){
            tree = new int[n*4];
        }

        int init(int[] arr, int node, int left, int right){
            if(left == right){
                return tree[node] = left; // 인덱스 자체를 저장
            }

            int mid = (left + right) >> 1;
            int leftMidIndex = init(arr, node << 1, left, mid);
            int rightMinIndex = init(arr, (node << 1)+1, mid+1, right);

            return tree[node] = arr[leftMidIndex] <= arr[rightMinIndex] ? leftMidIndex : rightMinIndex;
        }

        int update(int[] arr, int node, int left, int right, int index){
            if(index < left || index > right) {
                return tree[node];
            }

            if(left == right){
                return tree[node] = left;
            }

            int mid = (left + right) >> 1;
            int leftMinIndex = update(arr, node << 1, left, mid, index);
            int rightMinIndex = update(arr, (node << 1)+1, mid+1, right, index);

            return tree[node] = arr[leftMinIndex] <= arr[rightMinIndex] ? leftMinIndex : rightMinIndex;
        }

        int query(int[] arr, int node, int left, int right, int low, int high){
            if(left > high || right < low){
                return -1; // 범위를 벗어나면 쓰레기 값 반환
            }

            if(low <= left && right <= high){ // 범위 내부에 있다면
                return tree[node];
            }

            int mid = (left + right) >> 1;

            int leftMinIndex = query(arr, node<< 1, left, mid, low, high);
            int rightMinIndex = query(arr, (node << 1)+1, mid+1, right, low, high);

            // 쓰레기 값이 최종 정답으로 반환되지 않게 함
            // 정답을 도출할 수 없는 경우는 없으므로 결국에는 쓰레기는 출력되지 않을 것임
            if(leftMinIndex == -1 && rightMinIndex == -1){
                return -1;
            }else if(leftMinIndex == -1){
                return rightMinIndex;
            }else if(rightMinIndex == -1){
                return leftMinIndex;
            }

            return arr[leftMinIndex] <= arr[rightMinIndex] ? leftMinIndex : rightMinIndex;
        }
    }

}

