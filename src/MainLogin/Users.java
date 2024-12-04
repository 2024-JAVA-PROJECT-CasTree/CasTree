package MainLogin;

public class Users {
    private String id;
    private String pw;
    private String name;

    public Users(String id, String pw, String name){
//        setId(id);
//        setPw(pw);
//        setName(name);
        this.id = id;
        this.pw = pw;
        this.name = name;
    }

    public Users(String id, String name){
        this.id = id;
        this.name = name;
    }

    // ID만 받는 생성자
    public Users(String id){
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null || !(obj instanceof Users)){
            return false;
        }
        Users temp = (Users)obj;

        //return super.equals(obj);
        return id.equals(temp.getId());
    }
}
