package com.senhua.my.wonderland.web.mail;

/**
 * Created by wly on 2018/3/7.
 */

public class MailExample {

    public static void main (String args[]) throws Exception {
        String email = "1139140536@qq.com";
        String validateCode = "";
        SendEmail.sendEmailMessage(email,validateCode);

    }
}
