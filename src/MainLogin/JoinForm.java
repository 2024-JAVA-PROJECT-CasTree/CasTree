package MainLogin;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class JoinForm extends JDialog {
    private LoginForm owner;
    private UsersDS users;

    private JLabel lblName, lblTitle, lblId, lblPw, lblRePw;
    private JTextField tfName, tfId;
    private JPasswordField tfPw, tfRePw;
    private JButton btnJoin, btnCancel;

    public JoinForm(LoginForm owner) {
        super(owner, "Join", true);
        this.owner = owner;
        users = owner.getUsers();

        init();
        setDisplay();
        addListeners();
        showFrame();
    }

    private void init() {
        int tfSize = 10;
        Dimension lblSize = new Dimension(80, 35);
        Dimension btnSize = new Dimension(100, 25);

        lblTitle = new JLabel("회원정보를 입력해주세요");
        lblTitle.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        lblId = new JLabel("아이디", JLabel.LEFT);
        lblId.setPreferredSize(lblSize);
        lblName = new JLabel("이름", JLabel.LEFT);
        lblName.setPreferredSize(lblSize);
        lblPw = new JLabel("비밀번호", JLabel.LEFT);
        lblPw.setPreferredSize(lblSize);
        lblRePw = new JLabel("비밀번호 확인", JLabel.LEFT);
        lblRePw.setPreferredSize(lblSize);

        tfId = new JTextField(tfSize);
        tfPw = new JPasswordField(tfSize);
        tfRePw = new JPasswordField(tfSize);
        tfName = new JTextField(tfSize);

        btnJoin = new JButton("회원가입");
        btnJoin.setPreferredSize(btnSize);
        btnCancel = new JButton("취소");
        btnCancel.setPreferredSize(btnSize);
    }

    private void setDisplay() {
        JPanel pnlMain = new JPanel(new BorderLayout());
        JPanel pnlCenter = new JPanel(new GridLayout(4, 1, 5, 5));
        JPanel pnlSouth = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        pnlCenter.add(createInputPanel(lblId, tfId));
        pnlCenter.add(createInputPanel(lblName, tfName));
        pnlCenter.add(createInputPanel(lblPw, tfPw));
        pnlCenter.add(createInputPanel(lblRePw, tfRePw));

        pnlSouth.add(btnJoin);
        pnlSouth.add(btnCancel);

        pnlMain.add(lblTitle, BorderLayout.NORTH);
        pnlMain.add(pnlCenter, BorderLayout.CENTER);
        pnlMain.add(pnlSouth, BorderLayout.SOUTH);

        pnlMain.setBorder(new EmptyBorder(20, 20, 20, 20));

        setContentPane(pnlMain);
    }

    private JPanel createInputPanel(JLabel label, JComponent field) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(label);
        panel.add(field);
        return panel;
    }

    private void addListeners() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                dispose();
                owner.setVisible(true);
            }
        });

        btnCancel.addActionListener(e -> {
            dispose();
            owner.setVisible(true);
        });

        btnJoin.addActionListener(e -> performJoin());
    }

    private void performJoin() {
        if (isBlank()) {
            JOptionPane.showMessageDialog(this, "모든 정보를 입력해주세요");
            return;
        }

        String id = tfId.getText();
        String password = new String(tfPw.getPassword());
        String retryPassword = new String(tfRePw.getPassword());
        String name = tfName.getText();

        if (users.idOverlap(id)) {
            JOptionPane.showMessageDialog(this, "이미 존재하는 아이디입니다.");
            tfId.requestFocus();
        } else if (!password.equals(retryPassword)) {
            JOptionPane.showMessageDialog(this, "비밀번호가 일치하지 않습니다.");
            tfPw.requestFocus();
        } else {
            Users newUser = new Users(id, password, name);
            users.addUsers(newUser);
            JOptionPane.showMessageDialog(this, "회원가입을 완료했습니다.");
            dispose();
            owner.setVisible(true);
        }
    }

    private boolean isBlank() {
        return tfId.getText().isEmpty() ||
                new String(tfPw.getPassword()).isEmpty() ||
                new String(tfRePw.getPassword()).isEmpty() ||
                tfName.getText().isEmpty();
    }

    private void showFrame() {
        pack();
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }
}