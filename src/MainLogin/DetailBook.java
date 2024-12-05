package MainLogin;

import javax.swing.*;
import java.awt.*;

public class DetailBook extends JFrame {
    private AladinResponse.Item book;
    private JTabbedPane tabbedPane;

    public DetailBook(AladinResponse.Item book) {
        this.book = book;

        // 창 설정
        setTitle("책 상세 정보");
        setSize(1920, 1024);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // 책 정보 HTML 생성
        String htmlContent = generateHtmlContent(book);

        // 책 정보 JPanel 설정 (탭이 아닌 위쪽에 위치)
        JPanel bookInfoPanel = new JPanel(new BorderLayout());
        JTextArea bookInfoTextArea = new JTextArea(htmlContent);
        bookInfoTextArea.setEditable(false);
        bookInfoPanel.add(new JScrollPane(bookInfoTextArea), BorderLayout.CENTER);
        bookInfoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // 여백 추가

        // 탭 패널 설정
        tabbedPane = new JTabbedPane();

        // 줄거리 탭
        JPanel plotPanel = new JPanel(new BorderLayout());
        JTextArea plotText = new JTextArea(book.getDescription());
        plotText.setEditable(false);
        plotPanel.add(new JScrollPane(plotText), BorderLayout.CENTER);
        tabbedPane.addTab("줄거리", plotPanel);

        // 인물 관계도 탭
        JPanel characterPanel = new JPanel();
        JLabel characterImage = new JLabel(new ImageIcon("img/people.png"));
        characterPanel.add(characterImage);
        tabbedPane.addTab("인물 관계도", characterPanel);

        // 댓글 보기 탭
        JPanel commentsPanel = new JPanel(new BorderLayout());
        JTextArea commentsText = new JTextArea();
        commentsText.setEditable(false);
        commentsPanel.add(new JScrollPane(commentsText), BorderLayout.CENTER);
        tabbedPane.addTab("댓글 보기", commentsPanel);

        // 책 정보 패널을 탭 아래에 추가
        add(bookInfoPanel, BorderLayout.NORTH); // 책 정보는 위에 배치
        add(tabbedPane, BorderLayout.CENTER); // 탭은 아래에 배치

        setVisible(true);
    }

    // 책 정보를 HTML 형식으로 변환하는 메서드
    private String generateHtmlContent(AladinResponse.Item book) {
        StringBuilder sb = new StringBuilder();

        sb.append("<html><body style='font-family: Inter;'>");

        sb.append("<b style='font-size: 25px'>").append(book.getTitle()).append("</b><br>");
        sb.append("<p style='font-size: 20px'>저자 ").append(book.getAuthor()).append("</p>");
        sb.append("<p style='font-size: 20px'>출판사 ").append(book.getPublisher()).append("</p><br><br>");
        sb.append("<p style='font-size: 20px'>").append(book.getDescription()).append("</p><br>");

        sb.append("</body></html>");

        return sb.toString();
    }
}
