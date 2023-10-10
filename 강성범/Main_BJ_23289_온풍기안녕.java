package kr.ac.lecture.baekjoon.Num20001_30000;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/*
 * [문제 요약]
 * 특정 위치들의 온도가 k 이상일 까지의 depth
 * 
 * [제약 조건]
 * 2 ≤ R, C ≤ 20
 * 1 ≤ K ≤ 1,000
 * 온풍기는 하나 이상 있고, 온도를 조사해야 하는 칸도 하나 이상 있다.
 * 0 ≤ W ≤ R×C
 * 1 < x ≤ R, 1 ≤ y ≤ C (t = 0)
 * 1 ≤ x ≤ R, 1 ≤ y < C (t = 1)
 * 온풍기가 있는 칸과 바람이 나오는 방향에 있는 칸 사이에는 벽이 없다.
 * 온풍기의 바람이 나오는 방향에 있는 칸은 항상 존재한다.
 * 같은 벽이 두 번 이상 주어지는 경우는 없다.
 * 
 * [문제 설명]
 * (r,c) 격자판
 * 처음 모든 칸의 온도는 0
 * 1. 집에 있는 모든 온풍기에서 바람이 한 번 나옴
 * 2. 온도가 조절됨
 * 3. 온도가 1 이상인 가장 바깥쪽 칸의 온도가 1씩 감소
 * 4. 초콜릿을 하나 먹음
 * 5. 조사하는 칸의 모든 온도가 k 이상이 되었는지 검사
 *     - 만족하면 테스트 중단
 * 
 * [바람]
 * 온풍기의 바람이 나오는 방향은 한 쪽 방향으로 온도 증가
 *     - 오, 왼, 상, 하 중 하나
 * 온도 증가는 피라미드 형식이고, 5에서 부터 1까지 
 * 
 * 오른쪽 바람이면서 (x,y)의 온도가 증가했다면(k>1)
 * (x-1, y+1) (x, y+1) (x+1, y+1)의 온도도 k-1만큼 상승
 *     - 범위를 벗어나면 증가 안됨
 * 
 * 왼쪽 바람이면서 (x,y)의 온도가 증가했다면(k>1)
 * (x-1, y-1) (x, y-1) (x+1, y-1)
 * 
 * 위 바람이면서 (x,y)의 온도가 증가했다면(k>1)
 * (x-1, y-1) (x-1, y) (x-1, y+1)
 * 
 * 아래 바람이면서 (x,y)의 온도가 증가했다면(k>1)
 * (x+1, y-1) (x+1, y) (x+1, y+1)
 * 
 * 특정 칸에서 같은 온풍기의 바람을 여러번 만난다고 중첩되서 증가되지 않음 
 * 온풍기가 있는 칸도 온도가 증가됨
 *     -> 온풍기 정보를 리스트에 담고 0으로 초기화
 * 
 * 벽을 만나면 바람이 이동할 수 없음
 *     - 벽이 있는 곳 까지는 이동할 수 있으나 해당 방향으로 이동할 수 없음
 *     - 각 칸마다 가지고 있는 벽의 개수는 최대 한 개 
 * 
 * 벽의 
 * t가 0이면 윗 벽
 * t가 1이면 우측 벽
 * 
 * [온도 조절]
 * 모든 인접한 칸에 대해서, 온도가 높은 칸에서 낮은 칸으로 (온도차)/4만큼 온도가 조절
 * 인접한 곳에 벽이 있으면 온도가 조절되지 않음
 * 
 * [가장 바깥 쪽 칸]
 * 첫, 마지막 행과 열
 * 0은 감소되지 않음
 * 
 * [초콜릿]
 * depth가 100을 넘어가면 101을 출력하고 종료
 * 
 * [조사하는 칸]
 * 5 인것 리스트에 저장 후 0으로 초기화
 * 
 * */
public class Main_BJ_23289_온풍기안녕 {
    
    private static final int[] DX = {0, 0, -1, 1}; // 우, 왼, 상, 하
    private static final int[] DY = {1, -1, 0, 0};
    
    private static final int[][][] WIND = {
            { // 우
                {-1, 1}, {0, 1}, {1, 1}
            },
            { // 왼 
                {-1, -1}, {0, -1}, {1, -1}
            },
            { // 상
                {-1, -1}, {-1, 0}, {-1, 1}
            },
            { // 하
                {1, -1}, {1, 0}, {1, 1}
            }
    };
    
    private static int n, m, checkK;
    private static int[][] map;
    private static boolean[][][] wall;
    
    private static final List<Heater> heaters = new ArrayList<>();
    private static final List<Point> checkPoint = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer stz = new StringTokenizer(br.readLine());
        
        n = Integer.parseInt(stz.nextToken());
        m = Integer.parseInt(stz.nextToken());
        checkK = Integer.parseInt(stz.nextToken()); // 특정칸이 K인지 확인
        
        map = new int[n][m];
        wall = new boolean[n][m][4]; // [][][] : 우, 왼, 상, 하
        
        for(int i=0; i<n; i++) {
            stz = new StringTokenizer(br.readLine());
            for(int j=0; j<m; j++) {
                map[i][j] = Integer.parseInt(stz.nextToken()) - 1;
                
                if(map[i][j] >= 0 && map[i][j] < 4) {

                    // 문제 조건에 의해 온풍기의 바람이 나오는 방향에 있는 칸은 항상 존재한다. -> 범위체크X
                    int nx = i + DX[map[i][j]];
                    int ny = j + DY[map[i][j]];
                    
                    heaters.add(new Heater(nx, ny, map[i][j], 5));

                }else if(map[i][j] == 4) {
                    checkPoint.add(new Point(i, j));
                }
                map[i][j] = 0;
            }
        }
        
        int w = Integer.parseInt(br.readLine());
        while(w-- > 0) {
            stz = new StringTokenizer(br.readLine());
            int a = Integer.parseInt(stz.nextToken()) - 1;
            int b = Integer.parseInt(stz.nextToken()) - 1;
            int c = Integer.parseInt(stz.nextToken());
            
            if(c == 0) { // 윗 벽
                wall[a][b][2] = true;
                if(a-1 >= 0) {
                    wall[a-1][b][3] = true; // 이어진 곳 아래에 벽 추가
                }
            }else { // 우측 벽
                wall[a][b][0] = true;
                if(b+1 < m) {
                    wall[a][b+1][1] = true; // 이어진 곳 왼쪽에 벽 추가
                }
            }
        }
        
        System.out.println(solution());
        
//        for(int i=0; i<n; i++) {
//            for(int j=0; j<m; j++) {
//                System.out.print(map[i][j] + " ");
//            }
//            System.out.println();
//        }
        
        br.close();
    }
    
    // 동작
    private static int solution() {
        
        int chocolate = 0;
        
        // 5. 온도 체크
        while(!allPointCheck() && chocolate <= 100) {
            
            // 1. 바람이 나옴
            wind();
            
            // 2. 온도가 조절
            adjust();
            
            // 3. 온도 감소
            decrease();
            
            // 4. 초콜릿
            chocolate++;
        }
        
        return chocolate;
    }

    private static void wind() {
        
        // 온풍기 개수만큼 반복
        for(Heater h : heaters) {
            Queue<Heater> qu = new ArrayDeque<>();
            boolean[][] visited = new boolean[n][m]; // 같은 온풍기로 인해 동일 위치가 중첩되게 증가하지 않게 함
            
            qu.add(h);
            visited[h.x][h.y] = true;
            map[h.x][h.y] += h.k;
            
            while(!qu.isEmpty()) {
                Heater cn = qu.poll();
                
                if(cn.k == 1) continue; // 더이상 바람이 퍼질 수 없다면
                
                for(int d = 0; d<3; d++) { // 현재를 기준으로 세 방향 이동 가능
                    int nx = cn.x + WIND[cn.d][d][0];
                    int ny = cn.y + WIND[cn.d][d][1];
                    
                    if(isOutOfRange(nx, ny) || visited[nx][ny]) continue;

                    // 벽에 의해 갈 수 없는 위치
                    if(cn.d == 0) {
                        if(wall[nx][ny][1]) continue; // 다음 칸에서 벽에 막힌다면
                        
                        if(d == 0 && wall[cn.x][cn.y][2]) continue; // 현재칸의 벽으로 다음칸에 갈 수 없다면
                        if(d == 1 && wall[cn.x][cn.y][0]) continue;
                        if(d == 2 && wall[cn.x][cn.y][3]) continue;
                        
                    }else if(cn.d == 1) {
                        if(wall[nx][ny][0]) continue;
                        
                        if(d == 0 && wall[cn.x][cn.y][2]) continue;
                        if(d == 1 && wall[cn.x][cn.y][1]) continue;
                        if(d == 2 && wall[cn.x][cn.y][3]) continue;
                        
                    }else if(cn.d == 2) {
                        if(wall[nx][ny][3]) continue;
                        
                        if(d == 0 && wall[cn.x][cn.y][1]) continue;
                        if(d == 1 && wall[cn.x][cn.y][2]) continue;
                        if(d == 2 && wall[cn.x][cn.y][0]) continue;

                    }else {
                        if(wall[nx][ny][2]) continue;
                        
                        if(d == 0 && wall[cn.x][cn.y][1]) continue;
                        if(d == 1 && wall[cn.x][cn.y][3]) continue;
                        if(d == 2 && wall[cn.x][cn.y][0]) continue;
                    }
                    
                    visited[nx][ny] = true;
                    map[nx][ny] += (cn.k - 1);
                    qu.add(new Heater(nx, ny, cn.d, cn.k-1));
                }
            }
            
//            for(int i=0; i<n; i++) {
//                for(int j=0; j<m; j++) {
//                    System.out.print(map[i][j] + " ");
//                }
//                System.out.println();
//            }
//            System.out.println("-------------------------");
        }
        
    }

    private static void adjust() {
        int[][] change = new int[n][m];
        
        for(int i=0; i<n; i++) {
            for(int j=0; j<m; j++) {
                for(int d=0; d<DX.length; d++) {
                    int nx = i + DX[d];
                    int ny = j + DY[d];
                    
                    if(isOutOfRange(nx, ny) || map[nx][ny] >= map[i][j]) continue;
                    
                    // 벽 판단
                    int nd = (d % 2 == 0) ? d+1 : d-1; // 짝수일 때 1 증가, 홀수일 때 1 감소
                    if(wall[i][j][d] || wall[nx][ny][nd]) continue; // 전파되는 곳에 벽이 있다면
                    
                    // 벽이 아니라면
                    int diff = (map[i][j] - map[nx][ny]) / 4 ;
                    change[nx][ny] += diff;
                    change[i][j] -= diff;
                }
            }
        }
        
        // 갱신
        for(int i=0; i<n; i++) {
            for(int j=0; j<m; j++) {
                map[i][j] += change[i][j];
            }
        }
    }

    private static void decrease() {
        for(int i=0; i<n; i++) {
            if(map[i][0] > 0) {
                map[i][0]--;
            }
            
            if(map[i][m-1] > 0) {
                map[i][m-1]--;
            }
        }
        
        for(int j=1; j<m-1; j++) { // 위에서 양 끝을 제거했으므로 1~m-2까지만 반복
            if(map[0][j] > 0) {
                map[0][j]--;
            }
            
            if(map[n-1][j] > 0) {
                map[n-1][j]--;
            }
        }
        
    }

    private static boolean allPointCheck() {
        for(Point p : checkPoint) {
            if(map[p.x][p.y] < checkK) {
                return false;
            }
        }
        return true;
    }

    private static boolean isOutOfRange(int x, int y) {
        return x < 0 || y < 0 || x >= n || y >= m;
    }
    
    private static class Point{
        int x, y;

        Point(){}
        
        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
    
    // 온풍기
    private static class Heater extends Point{
        int d, k;
        
        public Heater(int x, int y, int d, int k) {
            this.x = x;
            this.y = y;
            this.d = d;
            this.k = k;
        }
    }
}
