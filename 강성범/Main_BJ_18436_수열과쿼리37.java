package kr.ac.lecture.baekjoon.Num10001_20000;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/*
* [문제 요약]
* 세 가지 쿼리 구현
*
* [제약 조건]
* N (1 ≤ N ≤ 100,000)
* (1 ≤ Ai ≤ 10^9)
* (1 ≤ M ≤ 100,000)
* (1 ≤ i ≤ N, 1 ≤ l ≤ r ≤ N, 1 ≤ x ≤ 10^9)
*
* [문제 설명]
* 세그트리를 만든다음에 따로 만들어서 관리하면 될듯?
*
*
* */
public class Main_BJ_18436_수열과쿼리37 {
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
                System.out.println(segmentTree.query(1, 1, n, b, c, a%2==0));
            }
        }



        br.close();
    }

    private static class SegmentTree{
        long[] evenTree; // 각 노드마다 구간의 짝수 개수 저장
        long[] oddTree; // 각 노드마다 구간의 홀수 개수 저장

        SegmentTree(int n){
            evenTree = new long[n*4];
            oddTree = new long[n*4];
        }

        void init(int[] arr, int node, int left, int right){
            if(left == right){
                if(arr[left] % 2 == 0){
                    evenTree[node] = 1;
                }else{
                    oddTree[node] = 1;
                }
                return;
            }

            int mid = (left + right) >> 1;
            init(arr, node << 1, left, mid);
            init(arr, (node << 1) + 1, mid+1, right);

            evenTree[node] = evenTree[node << 1] + evenTree[(node << 1) + 1];
            oddTree[node] = oddTree[node << 1] + oddTree[(node << 1) + 1];
        }

        void update(int[] arr, int node, int left, int right, int index){
            if(index < left || index > right) {
                return;
            }

            if(left == right){
                if(arr[index] % 2 == 0){
                    evenTree[node] = 1;
                    oddTree[node] = 0;
                }else{
                    oddTree[node] = 1;
                    evenTree[node] = 0;
                }
                return;
            }

            int mid = (left + right) >> 1;
            update(arr, node << 1, left, mid, index);
            update(arr, (node << 1) + 1, mid+1, right, index);

            evenTree[node] = evenTree[node << 1] + evenTree[(node << 1) + 1];
            oddTree[node] = oddTree[node << 1] + oddTree[(node << 1) + 1];
        }

        long query(int node, int left, int right, int low, int high, boolean isEven){
            if(right < low || left > high){ // 범위를 벗어난 경우
                return 0;
            }

            if(low <= left && right <= high){ // 범위 내부에 있는 경우
                if(isEven){
                    return evenTree[node];
                }
                return oddTree[node];
            }

            // 범위를 부분적으로만 일치하는 경우
            int mid = (left + right) >> 1;
            long leftCount = query(node << 1, left, mid, low, high, isEven);
            long rightCount = query((node<<1) + 1, mid+1, right, low, high, isEven);

            return leftCount + rightCount;
        }
    }
}

