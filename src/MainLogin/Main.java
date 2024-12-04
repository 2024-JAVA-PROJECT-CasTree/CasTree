package MainLogin;

public class Main{
    public static void main(String[] args){
        // 테스트용 사용자 생성
//        Users loggedInUser= new Users("jo", "subin");
        Users loggedInUser= new Users("min", "minsun");

        // 댓글 작성 화면 실행
        new CommentScreen(loggedInUser);
    }
}