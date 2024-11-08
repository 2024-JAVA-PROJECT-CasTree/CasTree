import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.Border;

public class MainScreen extends JFrame{
    public MainScreen(){

        // jframe
        setTitle("casTree");  //창의 제목
        setBounds(0, 0, 1920, 1024); //창의 크기
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //닫기 버튼 눌렀을 때 프로그램 종료

        // 실질적 모든 컴포넌트 패널 위에 위치
        JPanel mainP = new JPanel(new BorderLayout()); //BorderLayout을 적용해 창의 전체 레이아웃 관리
        getContentPane().add(mainP); //메인 프레임에 메인 패널을 추가

        // 상단 바
//        JPanel topBar = new JPanel(new BorderLayout());
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10)); //  FlowLayout을 중앙 정렬로 설정

        // 버튼 패널
        JPanel BtnP = new JPanel(new FlowLayout(FlowLayout.LEFT));
        BtnP.setOpaque(false);
        BtnP.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20)); // 오른쪽에 50px 여백 추가

        // 버튼 생성
        JButton signUpBtn = createStyledBtn("Sign Up");
        JLabel slush = new JLabel("|");
        JButton signInBtn = createStyledBtn("Sign In");

        // 버튼 색상 설정
        signUpBtn.setForeground(Color.black);
        signInBtn.setForeground(Color.black);
        slush.setForeground(Color.black);
        slush.setFont(new Font("Inter", Font.PLAIN, 20));

        // 버튼 패널에 추가
        BtnP.add(signUpBtn);
        BtnP.add(slush);
        BtnP.add(signInBtn);

        // 상단 바에 버튼 패널 추가
        topBar.add(BtnP, BorderLayout.WEST);

        // 검색 패널 생성 및 설정
//        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JTextField searchField = new JTextField(20); // 검색어 입력 필드
        searchField.setFont(new Font("Arial", Font.PLAIN, 16));

        JButton searchButton = new JButton("Search");
        searchButton.setFont(new Font("Arial", Font.PLAIN, 16));

        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        topBar.add(BtnP, BorderLayout.EAST);   // 상단 바에 검색 패널을 오른쪽에 추가
        mainP.add(topBar, BorderLayout.NORTH); // 메인 패널에 상단 바 추가

        // 로고 생성
        JPanel ImgP = new JPanel();
        ImageIcon LogoIcon = new ImageIcon(MainScreen.class.getResource("/img/logo.png"));
        Image scaledImg = LogoIcon.getImage().getScaledInstance(490, 245, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImg);

        JLabel LogoL = new JLabel(scaledIcon);
        LogoL.setBorder(BorderFactory.createEmptyBorder(50, 0, 0, 0));
        ImgP.add(LogoL);

        // 메인 패널에 상단 바와 이미지 패널 추가
        mainP.add(ImgP, BorderLayout.CENTER);


        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchTerm = searchField.getText(); // 검색어 가져오기
                if (!searchTerm.isEmpty()) {
                    System.out.println("Searching for: " + searchTerm); // 콘솔에 검색어 출력
                    JOptionPane.showMessageDialog(searchPanel, "Searching for: " + searchTerm); // 알림 창에 표시
                } else {
                    JOptionPane.showMessageDialog(searchPanel, "Please enter a search term.");
                }
            }
        });

        // 검색 패널을 메인 패널의 SOUTH 위치에 추가하여 이미지 아래에 표시되도록 설정
        mainP.add(searchPanel, BorderLayout.SOUTH);

        setVisible(true);

    }

    // signin, signup 버튼 헬퍼 메서드
    private JButton createStyledBtn(String text) {
        JButton btn = new JButton(text);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Inter", Font.PLAIN, 17));
        btn.setContentAreaFilled(false);
        btn.setForeground(Color.white);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
    public static void main(String[] args) {
        new MainScreen();
    }
}
