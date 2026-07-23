package model;

public class staff {
    private  int  staffId;
    private String username;
    private String password;
    private String full_name;
    private String role;

    public staff(int staffId, String username, String password, String full_name, String role){
        this.staffId = staffId;
        this.username = username;
        this.password = password;
        this.full_name = full_name;
        this.role = role;
    }
    public int getStaffId() { return staffId; }
    public String getUsername() { return username; }
    public String getPassword () { return password; }
    public String getFull_name () { return full_name; }
    public String getRole () { return role; }
}
