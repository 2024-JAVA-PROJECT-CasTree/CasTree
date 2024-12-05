package MainLogin;

import javax.swing.*;
import java.awt.*;

public class DetailBook extends JFrame {
    private AladinResponse.Item book;
    private String bookHtmlContent;
    private JEditorPane editorPane;
    private JTabbedPane tabbedPane; // 탭 패널 추가

    // 책 정보만 받는 생성자
    public DetailBook(AladinResponse.Item book) {
        this(book, generateHtmlContent(book)); // 다른 생성자를 호출하여 HTML 내용을 자동으로 생성
    }

    public DetailBook(AladinResponse.Item book, String bookHtmlContent) {
        this.book = book;
        this.bookHtmlContent = bookHtmlContent;

        // 창 설정
        setTitle("책 상세 정보");
        setSize(1920, 1024);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // 탭 패널 초기화
        tabbedPane = new JTabbedPane();

        // 책 정보 패널 설정
        setUpBookInfoPanel();

        // 줄거리 탭
        JPanel plotPanel = new JPanel(new BorderLayout());
        JTextArea plotText = new JTextArea(book.getDescription());
        plotText.setEditable(false);
        plotPanel.add(new JScrollPane(plotText), BorderLayout.CENTER);
        tabbedPane.addTab("줄거리", plotPanel);

        // 인물 관계도 탭
        JPanel characterPanel = new JPanel();
        JLabel characterImage = new JLabel(new ImageIcon("/img/people.png"));
        characterPanel.add(characterImage);
        tabbedPane.addTab("인물 관계도", characterPanel);

        // 댓글 보기 탭
        JPanel commentsPanel = new JPanel(new BorderLayout());
        JTextArea commentsText = new JTextArea();
        commentsText.setEditable(false);
        commentsPanel.add(new JScrollPane(commentsText), BorderLayout.CENTER);
        tabbedPane.addTab("댓글 보기", commentsPanel);

        // 책 정보 패널을 탭 아래에 추가
        add(tabbedPane, BorderLayout.CENTER); // 탭은 아래에 배치

        setVisible(true);
    }

    // 책 정보를 HTML 형식으로 변환하는 메서드
    public static String generateHtmlContent(AladinResponse.Item book) {
        StringBuilder sb = new StringBuilder();

        sb.append("<html><head><style>");
        sb.append("body { font-family: Inter; }");
        sb.append("b { font-size: 25px; }");
        sb.append("p { font-size: 20px; }");
        sb.append("</style></head><body>");

        sb.append("<b>").append(escapeHtml(book.getTitle())).append("</b><br>");
        sb.append("<p>저자 ").append(escapeHtml(book.getAuthor())).append("</p>");
        sb.append("<p>출판사 ").append(escapeHtml(book.getPublisher())).append("</p><br>");

        // 이미지 URL이 있는 경우 이미지 추가
        if (book.getImageUrl() != null && !book.getImageUrl().isEmpty()) {
            sb.append("<img src='").append(book.getImageUrl()).append("' alt='Book Cover' style='width:200px; height:auto;'><br><br>");
        }

        sb.append("<p>").append(escapeHtml(book.getDescription())).append("</p><br>");

        sb.append("</body></html>");

        return sb.toString();
    }

    // HTML 특수 문자를 이스케이프 처리하는 메서드
    private static String escapeHtml(String input) {
        return input.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }

    // 책 정보 패널 설정
    private void setUpBookInfoPanel() {
        // 책 정보 HTML 생성
        String htmlContent = generateHtmlContent(book);

        // 책 정보 JPanel 설정 (탭이 아닌 위쪽에 위치)
        JPanel bookInfoPanel = new JPanel(new BorderLayout());

        // HTML 콘텐츠를 표시할 JEditorPane 사용
        editorPane = new JEditorPane();
        editorPane.setContentType("text/html");  // HTML 콘텐츠로 설정
        editorPane.setText(htmlContent);  // HTML로 변환된 책 정보 설정
        editorPane.setEditable(false);  // 편집 불가 설정

        // JEditorPane 크기 키우기
        editorPane.setPreferredSize(new Dimension(1800, 300)); // 원하는 크기로 설정

        bookInfoPanel.add(new JScrollPane(editorPane), BorderLayout.CENTER);
        bookInfoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // 여백 추가

        // 책 정보 패널을 탭 아래에 추가
        add(bookInfoPanel, BorderLayout.NORTH); // 책 정보는 위에 배치
    }
}