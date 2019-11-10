package com.assesment.usermanager.entity;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.xml.bind.annotation.XmlTransient;

@Getter
@Setter
public class Token {
    @XmlTransient
    private Long id;

    @NotBlank
    private String token;

    @XmlTransient
    private Long userId;
}
