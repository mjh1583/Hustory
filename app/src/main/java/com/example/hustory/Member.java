package com.example.hustory;

import java.util.HashMap;
import java.util.Map;

public class Member {

    private String email;
    private String pw;

    public Member(String email, String pw) {
        this.email = email;
        this.pw = pw;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    // Member를 HashMap 형태로 값을 변환하여 데이터베이스에 저장하기 위한 메서드드먀
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("email", this.email);
        result.put("pw", this.pw);

        return result;
    }

}
