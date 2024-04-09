package org.example.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//@AllArgsConstructor
//@Getter
//@Setter
public class UserImpl implements User{
    public int function(int a){
        System.out.println("fun执行！");
        return a;
    }
}
