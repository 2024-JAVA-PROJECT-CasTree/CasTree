package MainLogin;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CommentScreen extends JFrame {
    private Users loggedInUser;

    public CommentScreen(Users loggedInUser) {
        this.loggedInUser = loggedInUser;

        setTitle("댓글 작성 화면");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // 메인 패널 생성
        JPanel mainPanel = new JPanel(new BorderLayout());
        getContentPane().add(mainPanel);

        // 제목 레이블
        JLabel titleLabel = new JLabel("댓글 작성", JLabel.CENTER);
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // 댓글 목록 패널
        JPanel commentListPanel = new JPanel();
        commentListPanel.setLayout(new BoxLayout(commentListPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(commentListPanel);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // 댓글 입력 패널
        JPanel commentInputPanel = new JPanel(new FlowLayout());
        JTextField commentField = new JTextField(30);
        JButton postButton = new JButton("댓글 작성");
        commentInputPanel.add(commentField);
        commentInputPanel.add(postButton);
        mainPanel.add(commentInputPanel, BorderLayout.SOUTH);

        // 버튼 클릭 이벤트
        postButton.addActionListener(e -> {
            String comment = commentField.getText().trim(); // 댓글 내용 가져오기
            if (!comment.isEmpty()) {
                // 댓글 저장
                CommentDAO commentDAO = new CommentDAO();
                commentDAO.saveComment(loggedInUser.getId(), comment);
                JOptionPane.showMessageDialog(this, "댓글 작성 성공!");
                commentField.setText("");

                // 댓글 목록 갱신
                refreshComments(commentListPanel);
            } else {
                JOptionPane.showMessageDialog(this, "댓글 내용을 입력하세요.");
            }
        });

        // 처음 실행 시 댓글 목록 표시
        refreshComments(commentListPanel);

        // 화면 표시
        setVisible(true);
    }

    private void refreshComments(JPanel commentListPanel) {
        // 기존 댓글 목록 초기화
        commentListPanel.removeAll();

        // 댓글 목록 가져오기
        CommentDAO commentDAO = new CommentDAO();
        List<String> com = commentDAO.getCommentsOrderedByCreatedAt();

        // 현재 로그인한 사용자 ID 가져오기
        String loggedInUserId = loggedInUser.getId();

        // 댓글을 하나씩 JLabel로 추가
//        for (String comment : com) {
//            JLabel commentLabel = new JLabel(comment);
//            commentLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
//            commentListPanel.add(commentLabel);
//        }

        for (String comment : com) {
            // 댓글 내용과 작성자 ID 분리
            String[] parts = comment.split(": ", 2); // "userId: content" 형식으로 가정
            String userId = parts[0]; // 작성자 ID
            String content = parts[1]; // 댓글 내용

            // 각 댓글을 담을 패널 생성
            JPanel commentPanel = new JPanel(new FlowLayout(userId.equals(loggedInUserId) ? FlowLayout.RIGHT : FlowLayout.LEFT));

            JLabel commentLabel = new JLabel(content);
            commentLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

            // 패널에 댓글 추가
            commentPanel.add(commentLabel);

            // 전체 댓글 목록 패널에 추가
            commentListPanel.add(commentPanel);
        }

        // 패널 갱신
        commentListPanel.revalidate();
        commentListPanel.repaint();
    }
}