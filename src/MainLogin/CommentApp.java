package MainLogin;

public class CommentApp {
    public static void main(String[] args) {
        CommentDAO commentDAO = new CommentDAO();

        // 댓글 저장 테스트
        commentDAO.saveComment("min", "이것은 INSERT 쿼리 테스트입니다!");
    }

}
