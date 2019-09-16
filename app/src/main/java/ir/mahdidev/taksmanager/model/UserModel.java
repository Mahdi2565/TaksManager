package ir.mahdidev.taksmanager.model;

import android.graphics.Bitmap;

import java.io.Serializable;

public class UserModel implements Serializable {
    private int id              ;
    private int isAdmin         ;
    private Bitmap imageUser    ;
    private String userName     ;
    private String password     ;
    private String email        ;
    private String age          ;
    private int isLoggedIn      ;
    private String registerDate ;

    public UserModel() {
    }

    public UserModel(int id, int isAdmin, Bitmap imageUser, String userName, String password, String email, String age, int isLoggedIn, String registerDate) {
        this.id = id;
        this.isAdmin = isAdmin;
        this.imageUser = imageUser;
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.age = age;
        this.isLoggedIn = isLoggedIn;
        this.registerDate = registerDate;
    }

    public Bitmap getImageUser() {
        return imageUser;
    }

    public void setImageUser(Bitmap imageUser) {
        this.imageUser = imageUser;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(int isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public int getIsLoggedIn() {
        return isLoggedIn;
    }

    public void setIsLoggedIn(int isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
    }

    public String getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(String registerDate) {
        this.registerDate = registerDate;
    }
}
