package MainLogin;

import java.awt.*;
import javax.swing.*;

public class MainScreen extends JFrame {
    private JButton loginBtn;
    private JButton logoutBtn;
    private JPanel topBar;
    private JPanel mainP;
    private Users currentUser;

    public MainScreen() {
        initUI();
        updateUIForLoggedOutUser();
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

        // 검색 패널
        JPanel searchPanel = createSearchPanel();
        topBar.add(searchPanel, BorderLayout.CENTER);

        mainP.add(topBar, BorderLayout.NORTH);

        // 로고 이미지
        JPanel imgPanel = createLogoPanel();
        mainP.add(imgPanel, BorderLayout.CENTER);

        // 이벤트 리스너 추가
        addEventListeners();

        setVisible(true);
    }

    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JTextField searchField = new JTextField(20);
        searchField.setFont(new Font("Arial", Font.PLAIN, 16));
        JButton searchButton = new JButton("Search");
        searchButton.setFont(new Font("Arial", Font.PLAIN, 16));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        searchButton.addActionListener(e -> {
            String searchTerm = searchField.getText();
            if (!searchTerm.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Searching for: " + searchTerm);
            } else {
                JOptionPane.showMessageDialog(this, "Please enter a search term.");
            }
        });

        return searchPanel;
    }

    private JPanel createLogoPanel() {
        JPanel imgPanel = new JPanel();
        ImageIcon logoIcon = new ImageIcon(MainScreen.class.getResource("/img/logo.png"));
        Image scaledImg = logoIcon.getImage().getScaledInstance(490, 245, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImg);
        JLabel logoLabel = new JLabel(scaledIcon);
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
            updateUIForLoggedOutUser();
        });
    }

    public void updateUIForLoggedInUser(Users user) {
        this.currentUser = user;
        loginBtn.setVisible(false);
        logoutBtn.setVisible(true);
        // 추가적인 로그인 상태 UI 업데이트
    }

    private void updateUIForLoggedOutUser() {
        loginBtn.setVisible(true);
        logoutBtn.setVisible(false);
        // 추가적인 로그아웃 상태 UI 업데이트
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
}
