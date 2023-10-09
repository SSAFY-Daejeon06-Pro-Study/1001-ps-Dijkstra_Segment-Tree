import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * 풀이 시작 : 2:28
 * 풀이 완료 : 5:28
 * 풀이 시간 :
 *
 * 문제 해석
 * R * C 크기의 격자판이 있음
 * 온풍기 시뮬레이션 과정 (격자의 초기값은 모두 0)
 * 1. 집에 있는 모든 온풍기에서 바람이 나옴
 * 2. 온도가 조절됨
 * 3. 온도가 1 이상인 가장 바깥쪽 칸의 온도가 1 감소
 * 4. 타이머 카운트 증가
 * 5. 조사하는 모든 칸의 온도가 K 이상이 되었는지 검사, K 이상이면 테스트 종료, 아니면 1번부터 다시 수행
 *
 * 온풍기에서 바람이 나오는 과정
 * 1. 온풍기의 바람이 나오는 방향에 있는 칸은 항상 온도가 5 올라감
 * 2. 어떤 칸 (x, y)에 온풍기 바람이 도착해 온도가 k( > 1)만큼 상승했다면 (x - 1, y + 1), (x, y + 1), (x + 1, y + 1) 칸의 온도는 k - 1만큼 상승
 *  - 같은 온풍기에서 나온 바람은 한 번만 영향을 줌
 * 3. 만약 벽이 있다면 다르게 진행됨
 *  - 바람이 오른쪽으로 불 때
 *  - (x, y)의 오른쪽 위로 바람이 이동하려면 (x, y)와 (x - 1, y), (x - 1, y)와 (x - 1, y + 1) 사이에 벽이 없어야 함.
 *      즉 기준 칸과 바로 윗칸 사이, 기준칸 윗칸과 대각선 윗칸 사이에 둘 다 벽이 없어야 함.
 *  - (x, y)의 오른쪽으로 바람이 이동하려면  (x, y)와 (x, y + 1) 사이에 벽이 없어야 함.
 *  - (x, y)의 오른쪽 아래로 바람이 이동하려면 (x, y)와 (x + 1, y), (x + 1, y)와 (x + 1, y + 1) 사이에 벽이 없어야 함.
 *      즉 기준 칸과 바로 아래칸 사이, 기준칸 아래칸과 대각선 아래칸 사이에 둘 다 벽이 없어야 함.
 *
 * 온도가 조절되는 과정
 * 1. 모든 인접한 칸에 대해 온도가 높은 칸 => 온도가 낮은 칸으로 ⌊(두 칸 온도의 차이) / 4⌋만큼 온도가 조절됨
 *  - 벽이 있으면 조절되지 않음
 * 2. 가장 바깥쪽 칸은 온도가 1씩 감소함 (최소 0)
 *
 * 구해야 하는 것
 * 몇 초가 지나야 방의 온도가 K 이상이 되는지 출력
 * 만약 100초가 지나도 끝나지 않으면 101 출력
 *
 * 문제 입력
 * 첫째 줄 : R, C, K
 * 둘째 줄 ~ R개 줄 : 방의 정보
 *  - '0' : 빈 칸
 *  - '1' : 방향 오른쪽 온풍기
 *  - '2' : 방향 왼쪽
 *  - '3' : 방향 위쪽
 *  - '4' : 방향 아래쪽
 *  - '5' : 온도 조사 칸
 * 다음 줄 : 벽의 개수 W
 * 다음 줄 ~ W개 줄 : x, y, t
 *  - (x,y) 기준으로 t = 0이라면 위쪽에 벽, t = 1이라면 오른쪽에 벽
 *
 * 제한 요소
 * 2 <= R, C <= 20
 * 1 <= K <= 1000
 * 1 <= 온풍기, 온도 조사 칸
 * 0 <= W <= R * C
 * 벽은 항상 집 내부에만 있음
 * 온풍기가 있는 칸과 바람이 나오는 칸 사이에는 벽이 없음
 * 온풍기 바람이 나오는 방향에 있는 칸은 항상 존재
 * 같은 벽이 2번 이상 주어지는 경우 없음
 *
 * 생각나는 풀이
 * 구현
 * 미리 퍼지는 칸에 대해 델타 배열 생성
 * 방향 : 우좌상하
 * 벽 정보 : 맵에 비트마스킹으로 표현
 * 우좌상하순 i번째 비트가 1이면 벽 있음 => 벽은 두 칸 사이이므로 두 칸에 대해 모두 비트마스킹
 * 시뮬레이션할 때 우측으로 바람 이동한다면
 *  - 오른쪽 위
 *      - 만약 오른쪽 윗칸이 방문처리 되었다면 continue
 *      - 현재 칸 윗칸 기준 아래칸이나 오른쪽칸이 벽이라면 continue
 *      - 그렇지 않다면 오른쪽 윗칸을 true로 변경하고 온도를 5 - (depth + 1)만큼 올려줌
 *  - 오른쪽
 *      - 만약 오른쪽 칸이 방문처리 되었다면 continue
 *      - 현재 칸 오른쪽 칸이 벽이라면 continue
 *      - 그렇지 않다면 오른쪽 칸을 true로 변경하고 온도를 5 - (depth + 1)만큼 올려줌
 *  - 오른쪽 아래
 *      - 만약 오른쪽 아래칸이 방문처리 되었다면 continue
 *      - 현재 칸 아래칸 기준 윗칸이나 오른쪽칸이 벽이라면 continue
 *      - 그렇지 않다면 오른쪽 아래칸을 true로 변경하고 온도를 5 - (depth + 1)만큼 올려줌
 * => Queue에 집어넣으면 반복 가능하지 않을까
 *
 * 온도 조절
 * 왼쪽 위부터 우하단만 검사하면 될 듯
 * temp 배열에 저장해야 함
 * 현재칸 : ((현재 - 인접) / 4)만큼 빼기
 * 인접칸 : ((현재 - 인접) / 4)만큼 더하기
 *
 * 구현해야 하는 기능
 * 1. 입력에 따른 맵 배열
 * 2. 델타 배열 : 온풍기 4방향에 대한 배열
 * 3. 온풍기 위치 저장할 리스트
 * 4. 조사할 칸 위치 저장할 리스트
 * 5. 벽 비트마스킹
 * 6. 시뮬레이션
 *  6-1. 온풍기 온도 상승
 *  6-2. 온도 조절
 *  6-3. 가장 바깥쪽 -1
 *  6-4. 온도 조사 칸 조사
 */
public class BOJ_23289_온풍기안녕 {
    static int R, C, K;
    static int[][] map; // 벽이 있는지 여부를 비트마스킹으로 표현, 우좌상하순
    static int[][] temperature; // 현재 칸의 온도
    static int[][] ttemp; // 온도 변화량 저장하는 임시 배열
    static boolean[][] visited; // 한 온풍기에서 온도 상승 중복 처리하지 않기 위한 방문 배열
    static ArrayList<Heater> heaters = new ArrayList<>(); // 온풍기 저장하는 리스트
    static ArrayList<Node> checkPoints = new ArrayList<>(); // 온도 체크할 좌표 저장하는 리스트
    static Queue<Node> q = new ArrayDeque<>(); // 온풍기 바람 bfs용 Queue
    static int[][] dx = {
            {-1, 0, 1}, // 우
            {1, 0, -1}, // 좌
            {-1, -1, -1}, // 상
            {1, 1, 1} // 하
    };
    static int[][] dy = {
            {1, 1, 1}, // 우
            {-1, -1, -1}, // 좌
            {-1, 0, 1}, // 상
            {1, 0, -1} // 하
    };

    static class Node {
        int x, y;

        public Node(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    static class Heater extends Node {
        int dir;

        public Heater(int x, int y, int dir) {
            super(x, y);
            this.dir = dir;
            // 온도 올라가는 칸은 바로 앞칸이므로 한칸씩 당겨줌
            if (dir == 0) this.y++;
            else if (dir == 1) this.y--;
            else if (dir == 2) this.x--;
            else this.x++;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        R = Integer.parseInt(st.nextToken());
        C = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());
        map = new int[R + 1][C + 1];
        temperature = new int[R + 1][C + 1];
        ttemp = new int[R + 1][C + 1];
        visited = new boolean[R + 1][C + 1];

        for (int i = 1; i <= R; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 1; j <= C; j++) {
                char now = st.nextToken().charAt(0);
                if (now == '5') {
                    checkPoints.add(new Node(i, j));
                } else if ('1' <= now) {
                    heaters.add(new Heater(i, j, now - '1'));
                }
            }
        }

        int W = Integer.parseInt(br.readLine());
        while (W-- > 0) {
            st = new StringTokenizer(br.readLine());
            int x = Integer.parseInt(st.nextToken());
            int y = Integer.parseInt(st.nextToken());
            int t = Integer.parseInt(st.nextToken());

            makeWall(x, y, t);
        }

        System.out.println(simultaion());
    }


    // 전체 과정 시뮬레이션
    private static int simultaion() {
        for (int time = 1; time <= 100; time++) {
            heatRoom(); // 바람 내보냄
            controlTemperature(); // 온도 조절
            temperatureDown(); // 외곽 온도 낮춤
            if (checkTemperature()) return time; // 조건 만족하면 현재 시간 반환
        }
        return 101; // 100회 넘어가면 101 반환
    }

    // 온풍기 바람 내보내는 과정 수행 메서드
    private static void heatRoom() {
        for (Heater heater : heaters) {
            initVisited(); // 방문 배열 초기화
            q.clear(); // 큐 초기화
            q.offer(heater); // 큐에 초기 위치 추가
            visited[heater.x][heater.y] = true;
            int dir = heater.dir; // 확산 방향

            for (int hot = 5; hot >= 1; hot--) { // +5도 ~ +1도까지 확산
                int qSize = q.size(); // 레벨별 bfs
                while (qSize-- > 0) {
                    Node now = q.poll();
                    temperature[now.x][now.y] += hot; // 현재 거리에서 올라가는 온도

                    // 확산 가능한 3방향 = 왼쪽 앞, 앞, 오른쪽 앞
                    for (int i = 0; i < 3; i++) {
                        int nextX = now.x + dx[dir][i];
                        int nextY = now.y + dy[dir][i];
                        // 방 내부가 아니거나 이미 큐에 넣었거나 벽이 있다면 큐에 넣지 않음
                        if (!isInRange(nextX, nextY) || visited[nextX][nextY] || isWall(now, dir, i)) continue;
                        q.offer(new Node(nextX, nextY));
                        visited[nextX][nextY] = true;
                    }
                }
            }
        }
    }

    // 현재 바람 이동하는 경로에 벽이 있는지 체크하는 메서드
    private static boolean isWall(Node now, int dir, int lmr) {
        // 현재 칸의 왼쪽칸 기준 정면, 오른쪽 체크
        if (lmr == 0) {
            int leftSide = left(dir);
            int nextX = now.x + dx[leftSide][1];
            int nextY = now.y + dy[leftSide][1];
            if ((map[nextX][nextY] & (1 << reverse(leftSide))) == 0 && (map[nextX][nextY] & (1 << dir)) == 0) return false;
            return true;
        }
        // 현재 칸의 정면 체크
        else if (lmr == 1) {
            if ((map[now.x][now.y] & (1 << dir)) == 0) return false;
            return true;
        }
        // 현재 칸의 오른쪽칸 기준 정면, 왼쪽 체크
        else {
            int rightSide = right(dir);
            int nextX = now.x + dx[rightSide][1];
            int nextY = now.y + dy[rightSide][1];
            if ((map[nextX][nextY] & (1 << reverse(rightSide))) == 0 && (map[nextX][nextY] & (1 << dir)) == 0) return false;
            return true;
        }
    }

    // 온도 조절하는 메서드
    private static void controlTemperature() {
        int dir;
        for (int i = 1; i <= R; i++) {
            for (int j = 1; j <= C; j++) {
                for (int k = 0; k <= 1; k++) {
                    int nextX = i + (1 - k);
                    int nextY = j + k;
                    if (!isInRange(nextX, nextY)) continue;
                    dir = k == 0 ? 3 : 0; // k = 0이면 아래쪽, k = 1이면 오른쪽 칸과 비교
                    if ((map[i][j] & (1 << dir)) != 0) continue; // 벽 쳐져있다면 온도 교환 안됨
                    int diff = (temperature[i][j] - temperature[nextX][nextY]) / 4;
                    ttemp[i][j] -= diff; // 온도 변화 배열에 저장
                    ttemp[nextX][nextY] += diff; // 온도 변화 배열에 저장
                }
            }
        }

        for (int i = 1; i <= R; i++) {
            for (int j = 1; j <= C; j++) {
                temperature[i][j] += ttemp[i][j]; // 이번 온풍기로 변화된 온도 갱신
                ttemp[i][j] = 0; // 온도 변화 배열 0으로 초기화
            }
        }
    }

    // 가장 바깥쪽 온도 -1 해주는 메서드
    private static void temperatureDown() {
        // 세로로 양쪽 줄 -1
        for (int i = 1; i <= R; i++) {
            temperature[i][1] = Math.max(0, temperature[i][1] - 1);
            temperature[i][C] = Math.max(0, temperature[i][C] - 1);
        }

        // 가로로 양쪽 줄 -1
        // 꼭짓점 중복 안되게 양쪽 끝은 포함 X
        for (int i = 2; i < C; i++) {
            temperature[1][i] = Math.max(0, temperature[1][i] - 1);
            temperature[R][i] = Math.max(0, temperature[R][i] - 1);
        }
    }

    // 종료조건 체크 메서드
    private static boolean checkTemperature() {
        int cnt = 0;
        for (Node node : checkPoints) if (temperature[node.x][node.y] >= K) cnt++;
        return cnt == checkPoints.size(); // 모든 칸이 온도 충족했으면 true, 아니면 false
    }

    // 벽을 비트마스킹으로 표현해주는 메서드
    private static void makeWall(int x, int y, int t) {
        // 우좌상하
        // 위쪽에 벽
        // 내 위 칸 = 아래쪽에 벽
        if (t == 0) {
            map[x][y] |= 1 << 2;
            map[x - 1][y] |= 1 << reverse(2);
        }
        // 오른쪽에 벽
        // 내 오른쪽 칸 = 왼쪽에 벽
        else {
            map[x][y] |= 1 << 0;
            map[x][y + 1] |= 1 << reverse(0);
        }
    }

    // 현재 방향 기준 왼쪽 90도 방향 구하는 메서드
    private static int left(int dir) {
        // 우좌상하
        switch (dir) {
            case 0: return 2;
            case 1: return 3;
            case 2: return 1;
            case 3: return 0;
        }
        return -1;
    }
    // 현재 방향 기준 오른쪽 90도 방향 구하는 메서드
    private static int right(int dir) {
        return reverse(left(dir));
    }

    // 방문 배열 초기화 메서드
    private static void initVisited() {
        for (int i = 1; i <= R; i++) Arrays.fill(visited[i], false);
    }

    // 우 <-> 좌, 상 <-> 하 변경 메서드
    private static int reverse(int idx) {
        if ((idx & 1) == 1) return idx - 1;
        else return idx + 1;
    }

    private static boolean isInRange(int x, int y) {
        return x >= 1 && x <= R && y >= 1 && y <= C;
    }

}