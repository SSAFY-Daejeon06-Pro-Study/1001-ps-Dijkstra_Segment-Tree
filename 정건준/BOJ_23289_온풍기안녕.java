import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

/***
 * [문제]
 * R, C (격자 크기, 2 <= R, C <= 20)
 * 방의 정보(0-빈칸, 1-오른쪽 온풍기, 2-왼쪽 온풍기, 3-위쪽 온풍기, 4-아래쪽 온풍기, 5-테스트 칸)
 * W (벽의 개수)
 * 벽 정보(x, y, t)
 * t가 0인 경우 (x, y)와 (x-1, y) 사이에 벽이 존재
 * t가 1인 경우 (x, y)와 (x, y+1) 사이에 벽이 존재
 *
 * 1. 집에 있는 모든 온풍기에서 바람이 나옴
 * 2. 온도가 조절됨
 * 3. 온도가 1 이상인 가장 바깥쪽 칸의 온도가 1씩 감소
 * 4. 초콜릿 하나 맜있게 섭취
 * 5. 조사하는 모든 칸의 온도가 K 이상이 되었는지 검사, 모든 칸의 온도가 K 이상이면 테스트를 중단하고 아니면 1부터 다시 시작
 *
 * 5번이 만족했을 때 초콜릿 출력, 100개가 넘으면 그냥 101를 출력
 *
 * [변수]
 * class Heater {
 *     int r, c, dir
 * }
 * class TestBlock {
 *     int r, int c
 * }
 * int[4][3] rPos = { {-1, -1, -1}, {-1, 0, 1}, {1, 1, 1}, {1, 0, -1} } //상우하좌
 * int[4][3] yPos = { {-1, 0, 1}, {1, 1, 1}, {1, 0, -1}, {-1 -1, -1} }
 * int[] rPos4 = {-1, 0, 1, 0}
 * int[] cPos4 = {0, 1, 0, -1}
 *
 * int R, C
 * int[][] matrix (R, C 크기) - 온도 저장
 * boolean[][][] walls (R, C, 4 크기) - r,c 칸 기준 상우하좌 벽 유무 저장
 * List<heater> heaters
 * List<testBlock> testBlocks
 * int chocolate
 *
 * [풀이]
 * 1. 변수 초기화 
 *    matrix 생성 (초기 0)
 *    온풍기 또는 테스트 칸을 heaters, testBlocks 저장
 *    walls 생성 및 초기화
 *
 * 2. void runAllHeater() - 집에 있는 모든 온풍기에서 바람이 나옴
 *    for(Heater heater : heaters)
 *      boolean[][] visit = new boolean[R][C]
 *      int nr = heater.r + rPos[dir][1]
 *      int nc = heater.c + cPos[dir][1]
 *      runHeater(nr, nc, 5, dir)
 *
 * runHeater(int r, int c, int temp, int dir)
 * r,c칸에 temp를 더하고 (r+rPos[dir][0], c+cPos[dir][0]), (r+rPos[dir][1], c+cPos[dir][1]), (r+rPos[dir][2], c+cPos[dir][2]) 세개의 칸을 재귀로 설정
 *
 * if(!isRange(r,c)) return
 * if(temp == 0) return
 * if(visit[r][c]) return
 * if(walls[r][c][(dir+2) % 4]) return //dir 반대 방향에 벽이 있는 경우
 *
 * matrix[r][c] += temp
 * visit[r][c] = true
 * for(int i=0; i<3; i++)
 *      if(i == 0 && walls[r][c][(dir+3) % 4]) continue //0번 칸은 기준 칸(r,c)로부터 반시계 방향에 있음
 *      if(i == 1 && walls[r][c][(dir+2) % 4]) continue
 *      if(i == 2 && walls[r][c][(dir+1) % 4]) continue //2번 칸은 기준 칸(r,c)로부터 시계 방향에 있음
 *      runHeater(r+rPos[dir][i], c+cPos[dir][i], temp-1, dir)
 *
 * 3. void AlltempSpread() - 온도가 조절됨
 *    int[][] copyMatrix, matrix 복사
 *    for(int i=0; i<R; i++)
 *       for(int j=0; j<C; j++)
 *              teepSpread(i, j, copyMatrix)
 *    matrix = copyMatrix
 *
 * tempSpread(int r, int c, int[][] copyMatrix)
 * r, c칸이 인접한 칸보다 온도가 높은 경우 온도를 조절시킴
 *
 * if(!isRange(r,c)) return
 *
 * for(int i=0; i<4; i++)
 *      int nr = r + rPos4[i]
 *      int nc = c + cPos4[i]
 *      if(walls[r][c][i]) continue
 *      if(matrix[r][c] <= matrix[nr][nc]) continue
 *
 *      int temp = matrix[r][c] / 4
 *      copyMatrix[nr][nc] += temp
 *      copyMatrix[r][c] -= temp
 *
 * 4. 온도가 1 이상인 가장 바깥쪽 칸의 온도가 1씩 감소
 * 5. 초콜릿 섭취, chocolate++
 * 6. 조사하는 모든 칸의 온도가 K 이상이 되었는지 검사
 */

public class BOJ_23289_온풍기안녕 {
    static class Heater {
        int r, c, dir;
        Heater(int r, int c, int dir) {
            this.r = r;
            this.c = c;
            this.dir = dir;
        }
    }
    static class TestBlock {
        int r, c;
        TestBlock(int r, int c) {
            this.r = r;
            this.c = c;
        }
    }

    static int[][] rPos = { {-1, -1, -1}, {-1, 0, 1}, {1, 1, 1}, {1, 0, -1} };
    static int[][] cPos = { {-1, 0, 1}, {1, 1, 1}, {1, 0, -1}, {-1, -1, -1} };
    static int[] rPos4 = {-1, 0, 1, 0};
    static int[] cPos4 = {0, 1, 0, -1};

    static int R, C;
    static int[][] matrix;
    static boolean[][][] walls;
    static List<Heater> heaters;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        R = Integer.parseInt(st.nextToken());
        C = Integer.parseInt(st.nextToken());
        int K = Integer.parseInt(st.nextToken());
        int chocolate = 0;

        matrix = new int[R][C];
        walls = new boolean[R][C][4];
        heaters = new ArrayList<>();
        List<TestBlock> testBlocks = new ArrayList<>();

        for(int i=0; i<R; i++) {
            st = new StringTokenizer(br.readLine());
            for(int j=0; j<C; j++) {
                int type = Integer.parseInt(st.nextToken());
                if(type == 0) continue;
                if(type == 5) {
                    testBlocks.add(new TestBlock(i, j));
                    continue;
                }

                Heater heater = null;
                if(type == 1) heater = new Heater(i, j, 1);
                else if(type == 2) heater = new Heater(i, j, 3);
                else if(type == 3) heater = new Heater(i, j, 0);
                else heater = new Heater(i, j, 2);
                heaters.add(heater);
            }
        }

        int W = Integer.parseInt(br.readLine());
        for(int i=0; i<W; i++) {
            st = new StringTokenizer(br.readLine());
            int r = Integer.parseInt(st.nextToken()) - 1;
            int c = Integer.parseInt(st.nextToken()) - 1;
            int t = Integer.parseInt(st.nextToken());
            if (t == 0) {
                walls[r][c][0] = true;
                walls[r-1][c][2] = true;
            } else {
                walls[r][c][1] = true;
                walls[r][c+1][3] = true;
            }
        }

        while(true) {
            runAllHeater();
            AlltempSpread();

            matrix[0][0] += 1;
            matrix[0][C-1] += 1;
            matrix[R-1][0] += 1;
            matrix[R-1][C-1] += 1;
            for(int i=0; i<R; i++) {
                if(matrix[i][0] > 0) matrix[i][0] -= 1;
                if(matrix[i][C-1] > 0) matrix[i][C-1] -= 1;
            }
            for(int i=0; i<C; i++) {
                if(matrix[0][i] > 0) matrix[0][i] -= 1;
                if(matrix[R-1][i] > 0) matrix[R-1][i] -= 1;
            }

            chocolate++;
            if(chocolate > 100) break;

            boolean endFlag = true;
            for(TestBlock testBlock : testBlocks) {
                if (matrix[testBlock.r][testBlock.c] < K) {
                    endFlag = false;
                    break;
                }
            }
            if(endFlag) break;
        }

        System.out.println(chocolate);
    }

    static void runAllHeater() {
        for(Heater heater : heaters) {
            boolean[][] visit = new boolean[R][C];
            int nr = heater.r + rPos[heater.dir][1];
            int nc = heater.c + cPos[heater.dir][1];
            runHeater(nr, nc, 5, heater.dir, visit);
        }
    }

    static void runHeater(int r, int c, int temp, int dir, boolean[][] visit) {
        if(!isRange(r, c)) return;
        if(temp == 0) return;
        if(visit[r][c]) return;
        if(walls[r][c][(dir+2) % 4]) return; //dir 반대 방향에 벽이 있는 경우

        matrix[r][c] += temp;
        visit[r][c] = true;

        for(int i=0; i<3; i++) {
            if(i == 0 && walls[r][c][(dir+3) % 4]) continue;
            if(i == 2 && walls[r][c][(dir+1) % 4]) continue;

            runHeater(r + rPos[dir][i], c + cPos[dir][i], temp - 1, dir, visit);
        }
    }

    static void AlltempSpread() {
        int[][] copyMatrix = new int[R][];
        for(int i=0; i<copyMatrix.length; i++) {
            copyMatrix[i] = Arrays.copyOf(matrix[i], C);
        }

        for(int i=0; i<R; i++) {
            for(int j=0; j<C; j++) {
                tempSpread(i, j, copyMatrix);
            }
        }
        matrix = copyMatrix;
    }

    static void tempSpread(int r, int c, int[][] copyMatrix) {
        for(int i=0; i<4; i++) {
            int nr = r + rPos4[i];
            int nc = c + cPos4[i];
            if(!isRange(nr, nc) || matrix[r][c] <= matrix[nr][nc]) continue;
            if(walls[r][c][i]) continue;

            int temp = (matrix[r][c] - matrix[nr][nc]) / 4;
            copyMatrix[nr][nc] += temp;
            copyMatrix[r][c] -= temp;
        }
    }

    static boolean isRange(int r, int c) {
        return (r >= 0 && r < R && c >= 0 && c < C);
    }
}