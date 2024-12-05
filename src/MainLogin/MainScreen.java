package MainLogin;

import java.awt.*;
import javax.swing.*;

public class MainScreen extends JFrame {
    private JButton loginBtn;
    private JButton logoutBtn;
    private JPanel topBar;
    private JPanel mainP;
    private Users currentUser;
    private BGMPlayer bgmPlayer;

    public MainScreen() {
        initUI(); // UI 초기화
        startBGM(); // BGM 스레드 시작
        updateUIForLoggedOutUser(); // 로그아웃 상태로 UI 업데이트
    }

    private void initUI() {
        setTitle("casTree");
        setBounds(0, 0, 1920, 1024);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainP = new JPanel(new BorderLayout());
        getContentPane().add(mainP);

        // 상단바 구성
        topBar = new JPanel(new BorderLayout());
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setOpaque(false);
        btnPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));

        loginBtn = createStyledBtn("Login");
        logoutBtn = createStyledBtn("Logout");
        btnPanel.add(loginBtn);
        btnPanel.add(logoutBtn);

        topBar.add(btnPanel, BorderLayout.EAST);
        mainP.add(topBar, BorderLayout.NORTH); // 상단 바를 mainP의 북쪽에 추가

        // 중앙에 로고와 검색 패널 추가
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS)); // 수직 정렬
        centerPanel.setOpaque(false);

        // 로고 이미지 패널 추가
        JPanel imgPanel = createLogoPanel();
        imgPanel.setAlignmentX(Component.CENTER_ALIGNMENT); // 가운데 정렬
        imgPanel.setMaximumSize(new Dimension(490, 345)); // 로고 패널 크기 고정
        centerPanel.add(imgPanel);

        // 검색 패널 추가
        JPanel searchPanel = createSearchPanel();
        searchPanel.setOpaque(false); // 배경 투명
        searchPanel.setAlignmentX(Component.CENTER_ALIGNMENT); // 가운데 정렬
        searchPanel.setMaximumSize(new Dimension(400, 50)); // 검색 패널 크기 고정
        centerPanel.add(searchPanel);

        mainP.add(centerPanel, BorderLayout.CENTER); // 중앙 패널을 mainP에 추가

        addEventListeners(); // 이벤트 리스너 추가
        setVisible(true); // 창 표시
    }

    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JTextField searchField = new JTextField(20);
        searchField.setFont(new Font("맑은 고딕", Font.PLAIN, 16));

        JButton searchButton = new JButton("Search");
        searchButton.setFont(new Font("Arial", Font.PLAIN, 16));

        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        searchButton.addActionListener(e -> {
            String searchTerm = searchField.getText();
            if (!searchTerm.isEmpty()) {
                BookSearchApp bookSearchApp = new BookSearchApp(searchTerm);
                bookSearchApp.setVisible(true);
                dispose(); // 현재 창 닫기
            } else {
                JOptionPane.showMessageDialog(MainScreen.this, "Please enter a search term.");
            }
        });

        return searchPanel;
    }

    private JPanel createLogoPanel() {
        JPanel imgPanel = new JPanel();

        ImageIcon logoIcon = new ImageIcon(MainScreen.class.getResource("/img/logo.png"));
        Image scaledImg = logoIcon.getImage().getScaledInstance(490, 245, Image.SCALE_SMOOTH);

        JLabel logoLabel = new JLabel(new ImageIcon(scaledImg));
        logoLabel.setBorder(BorderFactory.createEmptyBorder(50, 0, 0, 0));

        imgPanel.add(logoLabel);

        return imgPanel;
    }

    private void addEventListeners() {
        loginBtn.addActionListener(e -> {
            LoginForm loginForm = new LoginForm(this);
            loginForm.setVisible(true);
        });

        logoutBtn.addActionListener(e -> {
            currentUser = null;
            updateUIForLoggedOutUser(); // 로그아웃 상태로 UI 업데이트
        });
    }

    public void updateUIForLoggedInUser(Users user) {
        this.currentUser = user;
        loginBtn.setVisible(false); // 로그인 버튼 숨김
        logoutBtn.setVisible(true); // 로그아웃 버튼 표시
    }

    private void updateUIForLoggedOutUser() {
        loginBtn.setVisible(true); // 로그인 버튼 표시
        logoutBtn.setVisible(false); // 로그아웃 버튼 숨김
    }

    private JButton createStyledBtn(String text) {
        JButton btn = new JButton(text);

        btn.setBorderPainted(false);
        btn.setFocusPainted(false);

        btn.setFont(new Font("Inter", Font.PLAIN, 17));

        btn.setContentAreaFilled(false);

        btn.setForeground(Color.black);

        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return btn;
    }

    private void startBGM() {
        String bgmPath = "/bgm/Raindrops.wav"; // BGM 파일 경로
        bgmPlayer = new BGMPlayer(bgmPath, true); // true: 반복 재생
        bgmPlayer.start(); // 스레드 시작
    }
}