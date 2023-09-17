package com.jiyuan.wiki.req;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class UserResetPasswordReq {
    private Long id;

    @NotNull(message = "[password] cannot be empty")
    // @Length(min = 6, max = 20, message = "[Password] contains at least numbers and English, length 6-32")
    @Pattern(regexp = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,32}$", message = "[Password] contains at least numbers and English, length 6-32")
    private String password;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", password=").append(password);
        sb.append("]");
        return sb.toString();
    }
}
