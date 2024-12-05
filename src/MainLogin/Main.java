package MainLogin;

import javax.swing.*;

public class Main{
    public static void main(String[] args){

        SwingUtilities.invokeLater(() -> {
            new MainScreen();
        });

// 지우지 마라
//        // 테스트용 사용자 생성
//        Users loggedInUser= new Users("jo", "subin");
//        Users loggedInUser2= new Users("min", "minsun");
//
//        // 댓글 작성 화면 실행
//        new CommentScreen(loggedInUser);
//        new CommentScreen(loggedInUser2);
    }
}