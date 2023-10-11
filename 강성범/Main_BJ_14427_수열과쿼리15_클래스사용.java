import java.io.*;
import java.util.*;

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
*
*
* */
public class Main_BJ_14427_수열과쿼리15_클래스사용 {
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

                segmentTree.update(1, 1, n, b, c);

            }else{ // 전체 최소 인덱스 출력
                System.out.println(segmentTree.tree[1].index);
            }
        }



        br.close();
    }

    private static class SegmentTree{
        Node[] tree;

        SegmentTree(int n){
            tree = new Node[4*n];
        }

        Node init(int[] arr, int node, int left, int right){
            if(left == right){
                return tree[node] = new Node(left, arr[left]);
            }

            int mid = (left + right) / 2;
            Node leftMin = init(arr, node*2, left, mid);
            Node rightMin = init(arr, node*2+1, mid+1, right);

            return tree[node] = Node.getMin(leftMin, rightMin);
        }

        void update(int node, int left, int right, int index, int value){
            if(index < left || index > right){
                return;
            }

            if(left == right){
                tree[node].value = value;
                return;
            }

            int mid = (left + right) / 2;
            update(node*2, left, mid, index, value);
            update(node*2+1, mid+1, right, index, value);

            tree[node] = Node.getMin(tree[node*2], tree[node*2+1]);
        }
    }

    private static class Node{
        int index, value;

        public Node(int index, int value) {
            this.index = index;
            this.value = value;
        }

        private static Node getMin(Node leftMin, Node rightMin){
            Comparator<Node> comparator = (o1, o2) ->{
                if(o1.value == o2.value){
                    return o1.index - o2.index;
                }
                return o1.value - o2.value;
            };

            int diff = comparator.compare(leftMin, rightMin);
            return (diff < 0) ? leftMin : rightMin;
        }
    }
}