package com.flashsaleproject.service.model;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author Xuanchi Guo
 * @project flashsale
 * @created 8/25/23
 */
public class UserModel {
    private Integer id;
    @NotBlank(message = "Name cannot be empty")
    private String name;
    @NotNull(message = "gender cannot be empty")
    private Byte gender;
    @NotNull(message = "Age cannot be empty")
    @Min(value = 0, message = "Age cannot be less than 0")
    @Max(value = 150, message = "Age cannot be greater than 150")
    private Integer age;
    @NotBlank(message = "telphone cannot be empty")
    private String telphone;
    private String registerMode;
    private String thirdPartyId;
    @NotBlank(message = "password cannot be empty")

    private String encrptPassword; // This is not in UserDO, but in UserPasswordDO

    public String getEncrptPassword() {
        return encrptPassword;
    }

    public void setEncrptPassword(String encrptPassword) {
        this.encrptPassword = encrptPassword;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Byte getGender() {
        return gender;
    }

    public void setGender(Byte gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getTelphone() {
        return telphone;
    }

    public void setTelphone(String telphone) {
        this.telphone = telphone;
    }

    public String getRegisterMode() {
        return registerMode;
    }

    public void setRegisterMode(String registerMode) {
        this.registerMode = registerMode;
    }

    public String getThirdPartyId() {
        return thirdPartyId;
    }

    public void setThirdPartyId(String thirdPartyId) {
        this.thirdPartyId = thirdPartyId;
    }
}
