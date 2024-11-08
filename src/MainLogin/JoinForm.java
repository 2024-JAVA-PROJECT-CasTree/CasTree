package MainLogin;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class JoinForm  extends JDialog {
    private LoginForm owner;
    private UsersDS users;

    //입력하라는 안내 레이블
    private JLabel lblTitle;
    private JLabel lblId;  // 사용자 id
    private JLabel lblPw;  // 비번 입력
    private JLabel lblRePw; // 비번 확인
    private JLabel lblName; // 사용자 이름

    // 입력하는 텍스트 필드
    private JTextField tfId;//사용자 id
    private JPasswordField tfPw; //비번 입력
    private JPasswordField tfRePw; // 비번확인
    private JTextField tfName; //사용자 이름
    private JButton btnJoin; //회원가입 버튼
    private JButton btnCancel; //작업 취소 또는 이전 화면으로 돌아가기 버튼

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
        //크기 고정
        int tfSize = 10;
        Dimension lblSize = new Dimension(80, 35);
        Dimension btnSize = new Dimension(100, 25);

        lblTitle = new JLabel("Input your information");
        lblTitle.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        lblId = new JLabel("ID", JLabel.LEFT);
        lblId.setPreferredSize(lblSize);
        lblPw = new JLabel("Password", JLabel.LEFT);
        lblPw.setPreferredSize(lblSize);
        lblRePw = new JLabel("Retry", JLabel.LEFT);
        lblRePw.setPreferredSize(lblSize);
        lblName = new JLabel("Name", JLabel.LEFT);
        lblName.setPreferredSize(lblSize);

        tfId = new JTextField(tfSize);
        tfPw = new JPasswordField(tfSize);
        tfRePw = new JPasswordField(tfSize);
        tfName = new JTextField(tfSize);

        btnJoin = new JButton("Join");
        btnJoin.setPreferredSize(btnSize);
        btnCancel = new JButton("Cancel");
        btnCancel.setPreferredSize(btnSize);
    }

    private void setDisplay() {
        // FlowLayout 왼쪽 정렬
        FlowLayout flowLeft = new FlowLayout(FlowLayout.LEFT);

        JPanel pnlMain = new JPanel(new BorderLayout());

        JPanel pnlMNorth = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlMNorth.add(lblTitle);

        JPanel pnlMSouth = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlMSouth.add(btnJoin);
        pnlMSouth.add(btnCancel);

//        JPanel pnlMNorth = new JPanel(new BorderLayout());
//        pnlMNorth.add(lblTitle);
//
//        JPanel pnlMSouth = new JPanel(new BorderLayout());
//        pnlMSouth.add(lblTitle);

        JPanel pnlMCenter = new JPanel(new GridLayout(0, 1));
        JPanel pnlId = new JPanel(flowLeft);
        pnlId.add(lblId);
        pnlId.add(tfId);

        JPanel pnlPw = new JPanel(flowLeft);
        pnlPw.add(lblPw);
        pnlPw.add(tfPw);

        JPanel pnlRePw = new JPanel(flowLeft);
        pnlRePw.add(lblRePw);
        pnlRePw.add(tfRePw);

        JPanel pnlName = new JPanel(flowLeft);
        pnlName.add(lblName);
        pnlName.add(tfName);

        pnlMCenter.add(pnlId);
        pnlMCenter.add(pnlPw);
        pnlMCenter.add(pnlRePw);
        pnlMCenter.add(pnlName);

        pnlMain.add(pnlMNorth, BorderLayout.NORTH);
        pnlMain.add(pnlMCenter, BorderLayout.CENTER);
        pnlMain.add(pnlMSouth, BorderLayout.SOUTH);

        pnlMain.setBorder(new EmptyBorder(0, 20, 0, 20));
        pnlMSouth.setBorder(new EmptyBorder(0, 0, 10, 0));

        add(pnlMain, BorderLayout.NORTH);
        add(pnlMSouth, BorderLayout.SOUTH);
    }

    private void addListeners() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                dispose();
                owner.setVisible(true);
            }
        });
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                dispose();
                owner.setVisible(true);
            }
        });
        btnJoin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                //정보가 하나라도 비어있으면
                if (isBlank()) {
                    JOptionPane.showMessageDialog(
                            JoinForm.this,
                            "모든 정보를 입력해주세요"
                    );
                    //모두 입력했을 때
                } else {
                    //Id 중복일 때
                    if (users.isIdOverlap(tfId.getText())) {
                        JOptionPane.showMessageDialog(
                                JoinForm.this,
                                "이미 존재하는 Id입니다."
                        );
                        tfId.requestFocus();

                        //Pw와 RePw가 일치하지 않았을 때
                    } else if (!String.valueOf(tfPw.getPassword()).equals(String.valueOf(tfRePw.getPassword()))) {
                        JOptionPane.showMessageDialog(
                                JoinForm.this,
                                "Password와 Retry가 일치하지 않습니다."
                        );
                        tfPw.requestFocus();
                    } else {
                        users.addUsers(new Users(
                                tfId.getText(),
                                String.valueOf(tfPw.getPassword()),
                                tfName.getText()
                        ));
                        JOptionPane.showMessageDialog(
                                JoinForm.this,
                                "회원가입을 완료했습니다."
                        );
                        dispose();
                        owner.setVisible(true);
                    }//else
                }//else
            }//actionPerformed(ActionEvent ae)
        });  // btnJoin.addActionListener(new ActionListener()
    } //private void addListeners()

    public boolean isBlank(){
        boolean result = false;
        if(tfId.getText().isEmpty()){
            tfId.requestFocus();
            return true;
        }
        if(String.valueOf(tfPw.getPassword()).isEmpty()){
            tfPw.requestFocus();
            return true;
        }
        if(String.valueOf(tfRePw.getPassword()).isEmpty()){
            tfRePw.requestFocus();
            return true;
        }
        if(tfName.getText().isEmpty()) {
            tfName.requestFocus();
            return true;
        }
        return result;
    }
    private  void showFrame(){
        pack();
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }
}