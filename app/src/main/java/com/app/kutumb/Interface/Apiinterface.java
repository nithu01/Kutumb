package com.app.kutumb.Interface;

import com.app.kutumb.Activity.CouponActivity;
import com.app.kutumb.Model.CouponStatus;
import com.app.kutumb.Model.OtpConfirmResponse;
import com.app.kutumb.Model.Response;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Apiinterface {

        @POST("get_coupons")
        Call<CouponStatus> getCoupon(@Query("apicode")String apicode);


    @FormUrlEncoded
    @POST("loginWithOtp ")
    Call<OtpConfirmResponse> otp(@Field("apicode") String apicode, @Field("group_id")String group_id, @Field("mobile")String mobile);


    @FormUrlEncoded
    @POST("login")
    Call<Response> login(@Field("apicode") String apicode, @Field("email")String email, @Field("password")String mobile, @Field("group_id")String group_id);

    @FormUrlEncoded
    @POST("login")
    Call<com.app.kutumb.Model.Response> forget(@Field("apicode") String apicode, @Field("mobile")String mobile);

    @FormUrlEncoded
    @POST("addAddress")
    Call<com.app.kutumb.Model.Response> addaddress(@Field("apicode") String apicode, @Field("user_id")String userid, @Field("house_no")String house_no, @Field("street")String street, @Field("landmark")String landmark, @Field("pincode")String pincode, @Field("city")String city, @Field("locality")String locality, @Field("lat")String lat, @Field("long")String longs, @Field("location_id")String location_id);

    @FormUrlEncoded
    @POST("addOrder")
    Call<com.app.kutumb.Model.Response> order(@Field("apicode") String apicode, @Field("user_id")String userid, @Field("house_no")String house_no, @Field("street")String street, @Field("landmark")String landmark, @Field("pincode")String pincode, @Field("city")String city, @Field("locality")String locality, @Field("lat")String lat, @Field("long")String longs, @Field("location_id")String location_id,@Field("orderitems")String orderitems,@Field("totalcost")String totalcost,@Field("paymentmode")String paymentmode);

}
