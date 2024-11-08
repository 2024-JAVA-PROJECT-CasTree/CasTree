package MainLogin;

import java.util.ArrayList;

public class UsersDS {
    private ArrayList<Users> users;

    public UsersDS() {
        users = new ArrayList<Users>();
    }

    // 회원 추가
    public void addUsers(Users user) {
        users.add(user);
    }
    // 아이디 중복 확인
    public boolean isIdOverlap(String id) {
        return users.contains(new Users(id));
    }
    // 회원 삭제
    public void withdraw(String id) {
        users.remove(getUser(id));
    }
    // 유저 정보 가져오기
    public Users getUser(String id) {
        return users.get(users.indexOf(new Users(id)));
    }
    // 회원인지 아닌지 확인
    public boolean contains(Users user) {
        return users.contains(user);
    }
}
