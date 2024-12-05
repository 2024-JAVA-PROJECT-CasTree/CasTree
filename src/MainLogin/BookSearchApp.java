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

        // Logo Panel
        logoPanel = new JPanel(new BorderLayout());
        logoPanel.setBackground(Color.WHITE);
        logoIcon = new ImageIcon(getClass().getResource("/img/logo.png"));
        logoButton = new JButton(logoIcon);
        logoButton.setContentAreaFilled(false);  // Remove background color
        logoButton.setBorder(BorderFactory.createEmptyBorder());  // Remove border
        logoPanel.add(logoButton, BorderLayout.WEST);
        add(logoPanel, BorderLayout.NORTH);

        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        searchPanel.setBackground(Color.WHITE);

        searchField = new JTextField("원하는 검색어를 입력하세요", 48);
        searchField.setForeground(Color.GRAY);
        searchField.setBackground(Color.WHITE);
//        searchField.setBorder(BorderFactory.createEmptyBorder());
        searchField.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        searchField.setPreferredSize(new Dimension(500, 30));
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

        searchButton = new JButton("serch");
        searchButton.addActionListener(e -> searchBooks());
        searchButton.setPreferredSize(new Dimension(70, 30));

        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        add(searchPanel, BorderLayout.NORTH);

        // Main Panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // Book Cover (left side)
        coverLabel = new JLabel();
        coverLabel.setPreferredSize(new Dimension(313, 415));
        coverLabel.setHorizontalAlignment(SwingConstants.CENTER); // Center align
        mainPanel.add(coverLabel, BorderLayout.WEST); // **Left side**

        coverLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (selectedBook != null) {
                    openBookDetailsWindow(selectedBook);
                }
            }
        });

        // Result Area (center)
        resultArea = new JEditorPane();
        resultArea.setEditable(false);
        resultArea.setContentType("text/html"); // HTML format support
        JScrollPane scrollPane = new JScrollPane(resultArea);
        mainPanel.add(scrollPane, BorderLayout.CENTER); // **Center side**

        // Add main panel to content pane
        add(mainPanel, BorderLayout.CENTER);

        // ScrollPane settings
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setPreferredSize(new Dimension(800, 600)); // Desired size
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
    }

    // Image loading and display method
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

        sb.append("<html><body style='font-family: Arial, sans-serif; line-height: 1.6; margin: 0; padding: 0; align-items: center'>");

        if (response.getItems().isEmpty()) {
            sb.append("<p style='text-align: center;'>검색 결과가 없습니다.</p>");
        } else {
            if (response.getItems().size() == 1) {
                // 결과가 하나일 경우 전체를 중앙에 배치
                sb.append("<div style='display: flex; justify-content: center; align-items: center; flex-direction: column; text-align: center; margin-top: 200px'>");
            } else {
                sb.append("<div style='text-align: left;'>");
            }
            for (AladinResponse.Item item : response.getItems()) {
                // 제목 스타일: 크기, 굵기, 여백 등 조정
                sb.append("<div style='font-size: 28px; font-weight: bold; margin-bottom: 10px;'>")
                        .append(item.getTitle())
                        .append("</div>");

                // 저자 및 출판사 정보 스타일: 크기, 여백 추가
                sb.append("<p style='font-size: 20px; margin: 5px 0;'><b>저자:</b> ").append(item.getAuthor()).append("</p>");
                sb.append("<p style='font-size: 20px; margin: 5px 0;'><b>출판사:</b> ").append(item.getPublisher()).append("</p>");

                // 설명 텍스트: 크기, 줄 간격, 여백 추가
                sb.append("<div style='font-size: 18px; margin-top: 10px;'>")
                        .append(item.getDescription())
                        .append("</div>");

                sb.append("<hr style='border: 1px solid #ccc; margin: 20px 10px;'>"); // 각 항목 간 구분선
            }

            sb.append("</body></html>");

            if (!response.getItems().isEmpty()) {
                selectedBook = response.getItems().get(0);
                loadAndDisplayCover(selectedBook.getCover());
            } else {
                coverButton.setIcon(null);
                coverButton.setText("표지 없음");
                selectedBook = null;
            }

            resultArea.setContentType("text/html");
            resultArea.setText(sb.toString());
        }
    }

    private void openBookDetailsWindow(AladinResponse.Item book) {
        if (book != null) {
            new DetailBook(book); // Open detailed view
        }
    }

    public BookSearchApp(String searchTerm) {
        this(); // Call existing constructor
        searchField.setText(searchTerm);
        searchBooks(); // Perform search immediately
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BookSearchApp().setVisible(true));
    }
}

