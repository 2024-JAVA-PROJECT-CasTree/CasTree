package MainLogin;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class LoginForm extends JDialog {
    private UsersDS users;
    private JLabel lblId, lblPw;
    private JTextField tfId;
    private JPasswordField tfPw;
    private JButton btnLogin, btnJoin;
    private MainScreen mainScreen;

    public LoginForm(MainScreen parent) {
        super(parent, "Login", true);
        this.mainScreen = parent;
        users = new UsersDS();
        init();
        setDisplay();
        addListeners();

        pack();
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
    }

    private void init() {
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

    private void setDisplay() {
        JPanel pnlMain = new JPanel(new BorderLayout());
        JPanel pnlNorth = new JPanel(new GridLayout(2, 1, 5, 5));
        JPanel pnlSouth = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JPanel pnlId = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlId.add(lblId);
        pnlId.add(tfId);

        JPanel pnlPw = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlPw.add(lblPw);
        pnlPw.add(tfPw);

        pnlNorth.add(pnlId);
        pnlNorth.add(pnlPw);

        pnlSouth.add(btnLogin);
        pnlSouth.add(btnJoin);

        pnlMain.add(pnlNorth, BorderLayout.CENTER);
        pnlMain.add(pnlSouth, BorderLayout.SOUTH);
        pnlMain.setBorder(new EmptyBorder(10, 20, 10, 20));

        setContentPane(pnlMain);
    }

    private void addListeners() {
        btnLogin.addActionListener(e -> performLogin());
        btnJoin.addActionListener(e -> new JoinForm(this));

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                dispose();
            }
        });
    }

    private void performLogin() {
        String id = tfId.getText();
        String password = new String(tfPw.getPassword());

        if (id.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "아이디와 비밀번호를 모두 입력하세요.", "로그인 오류", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (users.contains(new Users(id))) {
            if (users.getUser(id).getPw().equals(password)) {
                JOptionPane.showMessageDialog(this, "로그인 성공!", "로그인 성공", JOptionPane.INFORMATION_MESSAGE);
                Users loggedInUser = users.getUser(id);
                mainScreen.updateUIForLoggedInUser(loggedInUser);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "비밀번호가 일치하지 않습니다.", "로그인 오류", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "존재하지 않는 아이디입니다.", "로그인 오류", JOptionPane.ERROR_MESSAGE);
        }
    }

    public UsersDS getUsers() {
        return users;
    }
}