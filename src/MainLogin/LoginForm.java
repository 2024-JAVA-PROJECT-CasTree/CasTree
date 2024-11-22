package MainLogin;

import com.mysql.cj.log.LogFactory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class LoginForm extends JFrame {
    private UsersDS users;
    private JLabel lblId, lblPw;
    private JTextField tfId;
    private JPasswordField tfPw;
    private JButton btnLogin,btnJoin;

    public LoginForm() {
        users = new UsersDS();
        init();
        setDisplay();
        addListeners();
        showFrame();
    }

    public void init() {
        Dimension lblSize = new Dimension(80, 30);
        int tfSize = 10;
        Dimension btnSize = new Dimension(100, 25);

        lblId = new JLabel("아이디");
        lblId.setPreferredSize(lblSize);
        lblPw = new JLabel("비밀번호");
        lblPw.setPreferredSize(lblSize);

        tfId = new JTextField(tfSize);
        tfPw = new JPasswordField(tfSize);

        btnLogin = new JButton("로그인");
        btnLogin.setPreferredSize(btnSize);
        btnJoin = new JButton("회원가입");
        btnJoin.setPreferredSize(btnSize);
    }

    public UsersDS getUsers() {
        return users;
    }

    public String getTfId() {
        return tfId.getText();
    }

    public void setDisplay() {
        FlowLayout flowLeft = new FlowLayout(FlowLayout.LEFT);

        JPanel pnlNorth = new JPanel(new GridLayout(0, 1));

        JPanel pnlId = new JPanel(flowLeft);
        pnlId.add(lblId);
        pnlId.add(tfId);

        JPanel pnlPw = new JPanel(flowLeft);
        pnlPw.add(lblPw);
        pnlPw.add(tfPw);

        pnlNorth.add(pnlId);
        pnlNorth.add(pnlPw);

        JPanel pnlSouth = new JPanel();
        pnlSouth.add(btnLogin);
        pnlSouth.add(btnJoin);

        pnlNorth.setBorder(new EmptyBorder(0, 20, 0, 20));
        pnlSouth.setBorder(new EmptyBorder(0, 0, 10, 0));

        add(pnlNorth, BorderLayout.NORTH);
        add(pnlSouth, BorderLayout.SOUTH);
    }

    public void addListeners() {
        btnLogin.addActionListener(e-> {
            String id = tfId.getText();
            if(id.isEmpty()){
                JOptionPane.showMessageDialog(LoginForm.this,
                        "아이디를 입력하세요",
                        "로그인 오류", JOptionPane.WARNING_MESSAGE);
                return;
            }
            // 존재하는 아이디일 경우
            if (users.contains(new Users(id))) {
                String password = String.valueOf(tfPw.getPassword());

                // 비밀번호 입력 확인
                if (password.isEmpty()) {
                        JOptionPane.showMessageDialog(
                                LoginForm.this,
                                "비밀번호를 입력하세요.",
                                "로그인 오류",
                                JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                // 비밀번호 확인
                if (!users.getUser(id).getPw().equals(password)) {
                    JOptionPane.showMessageDialog(
                            LoginForm.this,
                            "비밀번호가 일치하지 않습니다.",
                            "로그인 오류",
                            JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                // 로그인 성공 시
                JOptionPane.showMessageDialog(
                        LoginForm.this,
                        "로그인 성공!",
                        "로그인 성공",
                        JOptionPane.INFORMATION_MESSAGE);

                    // MainScreen으로 이동
                    Users loggedInUser = users.getUser(id);
                    MainScreen mainScreen = new MainScreen(loggedInUser);
                    mainScreen.setVisible(true);
                    dispose();
                    tfId.setText("");
                    tfPw.setText("");
                } else {
                    // 존재하지 않는 아이디일 경우
                    JOptionPane.showMessageDialog(
                            LoginForm.this,
                            "존재하지 않는 아이디입니다.",
                            "로그인 오류",
                            JOptionPane.ERROR_MESSAGE);
                }
        });

        btnJoin.addActionListener(e-> new JoinForm(LoginForm.this));

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                int choice = JOptionPane.showConfirmDialog(LoginForm.this,
                        "프로그램을 종료하시겠습니까?",
                        "종료 확인",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.WARNING_MESSAGE);
                if (choice == JOptionPane.OK_OPTION){
                    System.exit(0);
                }
            }
        });
    }

    public void showFrame() {
        setTitle("Login");
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }

    public static void main(String[] args) {
        new LoginForm();
    }
}
