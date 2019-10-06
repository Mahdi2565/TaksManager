package ir.mahdidev.taksmanager.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

@Entity
public class TaskModel {
    @Id(autoincrement = true)
    private Long id;
    @NotNull
    private Long userId;
    @NotNull
    private String title;
    @NotNull
    private String description;
    @NotNull
    private String status;
    @NotNull
    private String date;
    @NotNull
    private String time;
    @ToOne(joinProperty = "userId")
    private UserModel userModel;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 2101842271)
    private transient TaskModelDao myDao;
    @Generated(hash = 57123137)
    public TaskModel(Long id, @NotNull Long userId, @NotNull String title,
            @NotNull String description, @NotNull String status,
            @NotNull String date, @NotNull String time) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.status = status;
        this.date = date;
        this.time = time;
    }
    @Generated(hash = 648620828)
    public TaskModel() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getUserId() {
        return this.userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDescription() {
        return this.description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getStatus() {
        return this.status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getDate() {
        return this.date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getTime() {
        return this.time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    @Generated(hash = 2141663969)
    private transient Long userModel__resolvedKey;
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1197189035)
    public UserModel getUserModel() {
        Long __key = this.userId;
        if (userModel__resolvedKey == null
                || !userModel__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            UserModelDao targetDao = daoSession.getUserModelDao();
            UserModel userModelNew = targetDao.load(__key);
            synchronized (this) {
                userModel = userModelNew;
                userModel__resolvedKey = __key;
            }
        }
        return userModel;
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 452552788)
    public void setUserModel(@NotNull UserModel userModel) {
        if (userModel == null) {
            throw new DaoException(
                    "To-one property 'userId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.userModel = userModel;
            userId = userModel.getId();
            userModel__resolvedKey = userId;
        }
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1383696229)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getTaskModelDao() : null;
    }
    

}
