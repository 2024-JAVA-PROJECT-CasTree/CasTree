package MainLogin;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookSearchApp extends JFrame {
    private JTextField searchField;
    private JTextArea resultArea;
    private JButton searchButton;
    private JButton coverButton;

    private AladinApi apiService;

    private AladinResponse.Item selectedBook;

    private JLabel coverLabel;

    public BookSearchApp() {
        super("도서 검색");
        setSize(1920, 1024);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        apiService = RetrofitClient.getClient().create(AladinApi.class);

        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel searchPanel = new JPanel();
        searchField = new JTextField(20);
        searchField = new JTextField("원하는 검색어를 입력하세요",20);
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
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        resultArea = new JTextArea();
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);

        // JScrollPane의 크기 조정
        scrollPane.setPreferredSize(new Dimension(800, 600)); // 원하는 크기로 설정

        // 결과 패널 중앙 정렬
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.add(scrollPane);

//        add(searchPanel, BorderLayout.NORTH);
//        add(scrollPane, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        // Search button listener
        searchButton.addActionListener(e -> {
            String searchTerm = searchField.getText();
            if (!searchTerm.isEmpty()) {
                // Open BookSearchApp with the search term and close the current window
                BookSearchApp bookSearchApp = new BookSearchApp(searchTerm);
                bookSearchApp.setVisible(true);
                dispose(); // 현재 MainScreen 창 닫기
            } else {
                JOptionPane.showMessageDialog(this, "Please enter a search term.");
            }
        });

//        searchButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                searchBooks();
//            }
//        });
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
//        add(coverLabel, BorderLayout.WEST);

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
                ttbkey, query, "Keyword", 10, 1,
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
        for (AladinResponse.Item item : response.getItems()) {
            JLabel titleLabel = new JLabel("<html><b>제목: <b> " + item.getTitle());
            titleLabel.setFont(new Font("Arial", Font.BOLD, 14));

            sb.append("제목: ").append(item.getTitle()).append("\n");
            sb.append("저자: ").append(item.getAuthor()).append("\n");
            sb.append("출판사: ").append(item.getPublisher()).append("\n");
            sb.append("줄거리: ").append(item.getDescription()).append("\n");
            sb.append("\n");
        }
        resultArea.setText(sb.toString());
        if (!response.getItems().isEmpty()) {
            selectedBook = response.getItems().get(0);
            loadAndDisplayCover(selectedBook.getCover());
        } else {
            coverButton.setIcon(null);
            coverButton.setText("표지 없음");
            selectedBook = null;
        }
    }

    private void openBookDetailsWindow(AladinResponse.Item book) {
        JFrame detailsFrame = new JFrame("책 상세 정보");
        detailsFrame.setSize(600, 400);
        detailsFrame.setLayout(new BorderLayout());

        JTextArea detailsArea = new JTextArea();
        detailsArea.setEditable(false);
        detailsArea.setText(
                "제목: " + book.getTitle() + "\n" +
                "저자: " + book.getAuthor() + "\n" +
                "출판사: " + book.getPublisher() + "\n" +
                "설명: " + book.getDescription()
        );
        // 스크롤 가능
        JScrollPane scrollPane = new JScrollPane(detailsArea);

        scrollPane.setPreferredSize(new Dimension(800,600));
        detailsFrame.add(scrollPane, BorderLayout.CENTER);

        detailsFrame.setVisible(true);
    }
    public BookSearchApp(String searchTerm) {
        this(); // 기존 생성자 호출
        searchField.setText(searchTerm);
        searchBooks(); // 즉시 검색 수행
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new
                BookSearchApp().setVisible(true));
    }
}