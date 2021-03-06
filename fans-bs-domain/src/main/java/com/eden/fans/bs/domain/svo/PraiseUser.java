package com.eden.fans.bs.domain.svo;

import com.eden.fans.bs.domain.enu.PostPraise;

import java.io.Serializable;
import java.util.Date;

/**
 * 点赞用户
 * Created by Administrator on 2016/3/14.
 */
public class PraiseUser implements Serializable {
    /**
     *用户编码
     */
    private Long userCode;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     *1：点赞，0取消点赞
     */
    private PostPraise praise;

    /**
     * 点赞的时间
     */
    private Date time;

    public Long getUserCode() {
        return userCode;
    }

    public void setUserCode(Long userCode) {
        this.userCode = userCode;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public PostPraise getPraise() {
        return praise;
    }

    public void setPraise(PostPraise praise) {
        this.praise = praise;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
