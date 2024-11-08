package MainLogin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

class DBConnectionEx {
    public static Connection getConnection() {
        Connection conn = null;
        String username = "root"; // DBMS 접속 아이디
        String password = "1234"; // DBMS 접속 비밀번호
        String url = "jdbc:mysql://localhost/db?serverTimezone=UTC"; // 타임존 추가
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // 새로운 드라이버 클래스 사용
            conn = DriverManager.getConnection(url, username, password);
            System.out.println("DB 연결 완료");
        } catch (ClassNotFoundException e) {
            System.out.println("드라이버 오류");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("데이터베이스 접속 정보 오류");
            e.printStackTrace();
        }
        return conn;
    }

    public static void main(String[] args) {
        getConnection(); // DB 연결 테스트
    }
}
