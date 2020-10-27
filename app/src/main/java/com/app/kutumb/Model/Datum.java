
package com.app.kutumb.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("coupon_id")
    @Expose
    private String couponId;
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("offer_start_date")
    @Expose
    private String offerStartDate;
    @SerializedName("offer_end_date")
    @Expose
    private String offerEndDate;
    @SerializedName("desc")
    @Expose
    private String desc;
    @SerializedName("status")
    @Expose
    private String status;

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getOfferStartDate() {
        return offerStartDate;
    }

    public void setOfferStartDate(String offerStartDate) {
        this.offerStartDate = offerStartDate;
    }

    public String getOfferEndDate() {
        return offerEndDate;
    }

    public void setOfferEndDate(String offerEndDate) {
        this.offerEndDate = offerEndDate;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
