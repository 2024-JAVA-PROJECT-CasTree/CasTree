package MainLogin;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import MainLogin.DetailBook;

public class BookSearchApp extends JFrame {
    private JTextField searchField;
    private JEditorPane resultArea;
    private JButton searchButton;
    private JButton coverButton;

    private JPanel logoPanel;
    private ImageIcon logoIcon;
    private JButton logoButton;

    private AladinApi apiService;

    private AladinResponse.Item selectedBook;

    private JLabel coverLabel;

    public BookSearchApp() {
        super("도서 검색");
        setSize(1920, 1024);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        apiService = RetrofitClient.getClient().create(AladinApi.class);

        getContentPane().setBackground(Color.WHITE);

        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        logoPanel = new JPanel(new BorderLayout());
        logoPanel.setBackground(Color.WHITE);
        // 이미지 경로 수정해야됨
        logoIcon = new ImageIcon(getClass().getResource("/img/logo.png"));
        logoButton = new JButton(logoIcon);
        logoButton.setContentAreaFilled(false);  // 버튼 배경색 제거
        logoButton.setBorder(BorderFactory.createEmptyBorder());  // 테두리 제거
        logoButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));  // 커서 변경
        logoPanel.add(logoButton, BorderLayout.WEST);
        add(logoPanel, BorderLayout.WEST);

        // 검색창 및 버튼 패널
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(Color.WHITE);

        searchField = new JTextField("원하는 검색어를 입력하세요", 20);
        searchField.setForeground(Color.GRAY);
        searchField.setBackground(Color.LIGHT_GRAY);
        searchField.setBorder(BorderFactory.createEmptyBorder());
        searchField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (searchField.getText().equals("원하는 검색어를 입력하세요")) {
                    searchField.setText("");
                    searchField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setForeground(Color.GRAY);
                    searchField.setText("원하는 검색어를 입력하세요");
                }
            }
        });

        searchButton = new JButton("검색");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchBooks();
            }
        });

        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        add(searchPanel, BorderLayout.WEST);

        // 결과 텍스트 영역
        resultArea = new JEditorPane();
        resultArea.setEditable(false);
        resultArea.setBackground(Color.WHITE);
        resultArea.setBorder(BorderFactory.createEmptyBorder());
        JScrollPane scrollPane = new JScrollPane(resultArea);

        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        // JScrollPane의 크기 조정
        scrollPane.setPreferredSize(new Dimension(800, 600)); // 원하는 크기로 설정
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // 결과 패널 중앙 정렬
//        JPanel centerPanel = new JPanel(new GridLayout(1,2,10,10));
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.WHITE);
        centerPanel.add(scrollPane);

//        add(centerPanel, BorderLayout.CENTER);

        // Search button listener
        searchButton.addActionListener(e -> {
            String searchTerm = searchField.getText();
            if (!searchTerm.isEmpty()) {
                BookSearchApp bookSearchApp = new BookSearchApp(searchTerm);
                bookSearchApp.setVisible(true);
                dispose(); // 현재 MainScreen 창 닫기
            } else {
                JOptionPane.showMessageDialog(this, "Please enter a search term.");
            }
        });

        // 검색 패널에 검색 필드와 버튼 추가
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        centerPanel.add(scrollPane);

        // 책 표지
        coverLabel = new JLabel();
        coverLabel.setPreferredSize(new Dimension(313, 415));
        coverLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        add(coverLabel, BorderLayout.WEST);

        coverLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (selectedBook != null) {
                    openBookDetailsWindow(selectedBook);
                }
            }
        });
        centerPanel.add(coverLabel);

        add(centerPanel, BorderLayout.WEST);
        add(searchPanel, BorderLayout.NORTH);
    }

    // 이미지 로딩 및 표시 메서드
    private void loadAndDisplayCover(String imageUrl) {
        SwingWorker<ImageIcon, Void> worker = new SwingWorker<>() {
            @Override
            protected ImageIcon doInBackground() throws Exception {
                URL url = new URL(imageUrl);
                BufferedImage originalImage = ImageIO.read(url);

                int maxWidth = 313;
                int maxHeight = 415;

                double widthRatio = (double) maxWidth / originalImage.getWidth();
                double heightRatio = (double) maxHeight / originalImage.getHeight();
                double ratio = Math.min(widthRatio, heightRatio);

                int newWidth = (int) (originalImage.getWidth() * ratio);
                int newHeight = (int) (originalImage.getHeight() * ratio);

                Image resizedImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);

                BufferedImage bufferedResizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
                bufferedResizedImage.getGraphics().drawImage(resizedImage, 0, 0, null);

                return new ImageIcon(bufferedResizedImage);
            }

            @Override
            protected void done() {
                try {
                    coverLabel.setIcon(get());
                } catch (Exception e) {
                    coverLabel.setIcon(null);
                    coverLabel.setText("이미지 로드 실패");
                }
            }
        };
        worker.execute();
    }

    private void searchBooks() {
        String query = searchField.getText();
        String ttbkey = "ttbjsbiom231837002";

        Call<AladinResponse> call = apiService.searchBooks(
                ttbkey, query, "Keyword", 5, 1,
                "Book", "js", "20131101"
        );

        call.enqueue(new Callback<AladinResponse>() {
            @Override
            public void onResponse(Call<AladinResponse> call, Response<AladinResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AladinResponse aladinResponse = response.body();
                    displayResults(aladinResponse);
                } else {
                    resultArea.setText("검색 결과를 가져오는 데 실패했습니다.");
                }
            }

            @Override
            public void onFailure(Call<AladinResponse> call, Throwable t) {
                resultArea.setText("오류 발생: " + t.getMessage());
            }
        });
    }

    private void displayResults(AladinResponse response) {
        StringBuilder sb = new StringBuilder();

        //html 스타일로 출력 텍스트 포맷
        sb.append("<html><body style='font-family: Inter;'>");

        for (AladinResponse.Item item : response.getItems()) {

            sb.append("<b style='font-size: 25px'>").append(item.getTitle()).append("</b><br>");
            sb.append("<p style='font-size: 20px'>저자 ").append(item.getAuthor()).append("</p>");
            sb.append("<p style='font-size: 20px'>출판사 ").append(item.getPublisher()).append("</p><br><br>");
            sb.append("<p style='font-size: 20px'>").append(item.getDescription()).append("</p><br>");
        }
        sb.append("</body></html>");

        if (!response.getItems().isEmpty()) {
            selectedBook = response.getItems().get(0);
            loadAndDisplayCover(selectedBook.getCover());
//            new DetailBook(selectedBook, sb.toString()); // html 값 넘겨 상제 정보 보여줌
        } else {
            coverButton.setIcon(null);
            coverButton.setText("표지 없음");
            selectedBook = null;
        }

        resultArea.setContentType("text/html");
        resultArea.setText(sb.toString());

    }

    private void openBookDetailsWindow(AladinResponse.Item book) {
        if (book != null) {

            new DetailBook(book); // html 값 넘겨 상세 정보 보여줌
        }
    }

    public BookSearchApp(String searchTerm) {
        this(); // 기존 생성자 호출
        searchField.setText(searchTerm);
        searchBooks(); // 즉시 검색 수행
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BookSearchApp().setVisible(true));
    }
}
