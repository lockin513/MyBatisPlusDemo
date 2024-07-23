package com.itheima.mp.domain.po;

import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
public class UserInfo {
    private Integer age;
    private String intro;
    private String gender;
}
