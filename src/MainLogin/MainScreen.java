package MainLogin;

import java.awt.*;
import javax.swing.*;

public class MainScreen extends JFrame {
    public MainScreen(Users loggedInUser) {
        setTitle("casTree");
        setBounds(0, 0, 1920, 1024);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainP = new JPanel(new BorderLayout());
        getContentPane().add(mainP);

        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JPanel BtnP = new JPanel(new FlowLayout(FlowLayout.LEFT));
        BtnP.setOpaque(false);
        BtnP.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));

        // 로그인한 사용자 정보 표시
        JLabel userInfoLabel = new JLabel("Welcome, " + loggedInUser.getName());
        userInfoLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        BtnP.add(userInfoLabel);

        // 로그아웃 버튼 추가
        JButton logoutBtn = createStyledBtn("Logout");
        logoutBtn.setForeground(Color.black);
        BtnP.add(logoutBtn);

        topBar.add(BtnP, BorderLayout.WEST);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JTextField searchField = new JTextField(20);
        searchField.setFont(new Font("Arial", Font.PLAIN, 16));

        JButton searchButton = new JButton("Search");
        searchButton.setFont(new Font("Arial", Font.PLAIN, 16));

        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        topBar.add(searchPanel, BorderLayout.EAST);
        mainP.add(topBar, BorderLayout.NORTH);

        JPanel ImgP = new JPanel();
        ImageIcon LogoIcon = new ImageIcon(MainScreen.class.getResource("/img/logo.png"));
        Image scaledImg = LogoIcon.getImage().getScaledInstance(490, 245, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImg);

        JLabel LogoL = new JLabel(scaledIcon);
        LogoL.setBorder(BorderFactory.createEmptyBorder(50, 0, 0, 0));
        ImgP.add(LogoL);

        mainP.add(ImgP, BorderLayout.CENTER);

        searchButton.addActionListener(e-> {
            String searchTerm = searchField.getText();
            if(!searchTerm.isEmpty()){
                System.out.println("Searching for: " + searchTerm);
                JOptionPane.showMessageDialog(searchPanel, "Searching for: " + searchTerm);
            }else {
                JOptionPane.showMessageDialog(searchPanel, "Please enter a search term.");
            }
        });

        logoutBtn.addActionListener(e-> {
            dispose();
            new LoginForm().setVisible(true);
        });

        mainP.add(searchPanel, BorderLayout.SOUTH);

        setVisible(true);
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