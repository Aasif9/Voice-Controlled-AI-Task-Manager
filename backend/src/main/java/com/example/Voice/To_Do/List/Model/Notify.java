package com.example.Voice.To_Do.List.Model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notify {

    private String email;
    private String phone;
    private String message;
    private String metaData;


}
