package java1.lang.annotation;

import java.util.Date;

import java1.lang.annotation.validation.AssertFalse;
import java1.lang.annotation.validation.Email;
import java1.lang.annotation.validation.NotNull;
import java1.lang.annotation.validation.Number;
import java1.lang.annotation.validation.PastTime;
import java1.lang.annotation.validation.StringCut;
import java1.lang.annotation.validation.Tel;
import java1.lang.annotation.validation.ToUpper;

@StringCut
public class AnnoVo {
    @Email
    @StringCut(minLength = 7, completion = "zxiaofan")
    @NotNull
    private String guestEmail;

    @ToUpper
    private String toUpper;

    @StringCut(maxLength = 5)
    @Tel
    private String cutName;

    @AssertFalse
    private boolean boolFalse;

    @PastTime
    private Date date;

    @Number(max = 5, overstep = "2", defaultValue = "3")
    private Integer num;

    /**
     * 设置guestEmail.
     * 
     * @return 返回guestEmail
     */
    public String getGuestEmail() {
        return guestEmail;
    }

    /**
     * 获取guestEmail.
     * 
     * @param guestEmail
     *            要设置的guestEmail
     */
    public void setGuestEmail(String guestEmail) {
        this.guestEmail = guestEmail;
    }

    /**
     * 设置toUpper.
     * 
     * @return 返回toUpper
     */
    public String getToUpper() {
        return toUpper;
    }

    /**
     * 获取toUpper.
     * 
     * @param toUpper
     *            要设置的toUpper
     */
    public void setToUpper(String toUpper) {
        this.toUpper = toUpper;
    }

    /**
     * 设置boolFalse.
     * 
     * @return 返回boolFalse
     */
    public boolean isBoolFalse() {
        return boolFalse;
    }

    /**
     * 获取boolFalse.
     * 
     * @param boolFalse
     *            要设置的boolFalse
     */
    public void setBoolFalse(boolean boolFalse) {
        this.boolFalse = boolFalse;
    }

    /**
     * 设置cutName.
     * 
     * @return 返回cutName
     */
    public String getCutName() {
        return cutName;
    }

    /**
     * 获取cutName.
     * 
     * @param cutName
     *            要设置的cutName
     */
    public void setCutName(String cutName) {
        this.cutName = cutName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }
}