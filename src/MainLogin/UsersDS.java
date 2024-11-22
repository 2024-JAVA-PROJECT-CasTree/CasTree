package MainLogin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsersDS {

    //회원 추가
    public void addUsers(Users user){
        String sql = "INSERT INTO userlist (ID, Name, Password, Retry) VALUES (?, ?, ?, ?)";

        try(Connection conn = DBConnectionEx.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setString(1, user.getId());
            pstmt.setString(2, user.getName());
            pstmt.setString(3, user.getPw());
            pstmt.setString(4, user.getPw());
            pstmt.executeUpdate();

        }catch (SQLException e){
            System.out.println("회원 추가 중 오류 발생 : " + e.getMessage());
            e.printStackTrace();
        }
    }

    // 아이디 중복 확인
    public boolean idOverlap(String id){
        String sql = "SELECT COUNT(*) FROM userlist WHERE ID = ?";
        try(Connection conn = DBConnectionEx.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setString(1,id);
            ResultSet rs = pstmt.executeQuery();
            rs.next();

            return rs.getInt(1) > 0; //중복된 아이디가 있으면 true 반환
        }catch (SQLException e){
            System.out.println("아이디 중복 확인 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // 회원 삭제
    public void delmember(String id){
        String sql = "DELETE FROM userlist WHERE ID = ?";

        try(Connection conn = DBConnectionEx.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setString(1, id);
            pstmt.executeUpdate();
        }catch (SQLException e) {
            System.out.println("회원 삭제 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // User정보 가져오기
    public Users getUser(String id){
            String sql = "SELECT * FROM userlist WHERE ID = ?";
            try (Connection conn = DBConnectionEx.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, id);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    return new Users(
                            rs.getString("ID"),
                            rs.getString("Password"),
                            rs.getString("Name")
                    );
                }
            } catch (SQLException e) {
                System.out.println("유저 정보 가져오는 중 오류 발생: " + e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        // 회원인지 아닌지 확인
        public boolean contains(Users user) {
            return idOverlap(user.getId());
         }
}
