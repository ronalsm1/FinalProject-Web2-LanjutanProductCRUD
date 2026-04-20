package com.example.productcrud.dto;

public class ChangePasswordRequest {
    //Ronald Saut M. buat file ChangePasswordRequest di folder dto
    private String oldPassword;
    private String newPassword;
    private String confirmNewPassword;

    //Ini untuk getter dan setter nya

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmNewPassword() {
        return confirmNewPassword;
    }

    public void setConfirmNewPassword(String confirmNewPassword) {
        this.confirmNewPassword = confirmNewPassword;
    }
}
