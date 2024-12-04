package MainLogin;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.net.URL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookSearchApp extends JFrame {
    private JTextField searchField;
    private JTextArea resultArea;
    private JButton searchButton;

    private AladinApi apiService;

    private JLabel coverLabel;

    public BookSearchApp() {
        super("알라딘 도서 검색");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        apiService = RetrofitClient.getClient().create(AladinApi.class);

        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel searchPanel = new JPanel();
        searchField = new JTextField(20);
        searchButton = new JButton("검색");
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        resultArea = new JTextArea();
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);

        add(searchPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchBooks();
            }
        });
        // 책 표지
        coverLabel = new JLabel();
        coverLabel.setPreferredSize(new Dimension(150, 200));
        add(coverLabel, BorderLayout.EAST);

    }
    // 이미지 로딩 및 표시 메서드
    private void loadAndDisplayCover(String imageUrl) {
        SwingWorker<ImageIcon, Void> worker = new SwingWorker<>() {
            @Override
            protected ImageIcon doInBackground() throws Exception {
                URL url = new URL(imageUrl);
                // 기존 코드 불러오는 이미지 너무 깨짐
//                Image image = ImageIO.read(url);
                // new 코드
                BufferedImage originalImage = ImageIO.read(url);

                int maxWidth = 150;
                int maxHeight = 200;

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
        String ttbkey = "ttbjsbiom231837002"; // 실제 TTB 키로 교체해야 합니다

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
            sb.append("제목: ").append(item.getTitle()).append("\n");
            sb.append("저자: ").append(item.getAuthor()).append("\n");
            sb.append("설명: ").append(item.getDescription()).append("\n");
            sb.append("\n");
        }
        resultArea.setText(sb.toString());
        if (!response.getItems().isEmpty()) {
            AladinResponse.Item firstItem = response.getItems().get(0);
            loadAndDisplayCover(firstItem.getCover());
        } else {
            coverLabel.setIcon(null);
            coverLabel.setText("표지 없음");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new BookSearchApp().setVisible(true);
            }
        });
    }
}