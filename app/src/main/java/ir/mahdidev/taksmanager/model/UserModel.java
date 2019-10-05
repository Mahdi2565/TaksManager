package ir.mahdidev.taksmanager.model;

import android.graphics.Bitmap;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Unique;

@Entity
public class UserModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id(autoincrement = true)
    private Long id              ;
    @NotNull
    private int isAdmin         ;
    @NotNull
    private byte[] imageUser    ;
    @NotNull
    @Unique
    private String userName     ;
    @NotNull
    private String password     ;
    @NotNull
    private String email        ;
    @NotNull
    private String age          ;
    @NotNull
    private int isLoggedIn      ;
    @NotNull
    private String registerDate ;

    public UserModel() {
    }

    @Generated(hash = 1096068859)
    public UserModel(Long id, int isAdmin, @NotNull byte[] imageUser,
            @NotNull String userName, @NotNull String password,
            @NotNull String email, @NotNull String age, int isLoggedIn,
            @NotNull String registerDate) {
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

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getIsAdmin() {
        return this.isAdmin;
    }

    public void setIsAdmin(int isAdmin) {
        this.isAdmin = isAdmin;
    }

    public byte[] getImageUser() {
        return this.imageUser;
    }

    public void setImageUser(byte[] imageUser) {
        this.imageUser = imageUser;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAge() {
        return this.age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public int getIsLoggedIn() {
        return this.isLoggedIn;
    }

    public void setIsLoggedIn(int isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
    }

    public String getRegisterDate() {
        return this.registerDate;
    }

    public void setRegisterDate(String registerDate) {
        this.registerDate = registerDate;
    }


}
