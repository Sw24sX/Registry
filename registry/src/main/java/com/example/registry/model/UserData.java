package com.example.registry.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "USER_DATA")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserData extends BaseEntity {
    @Column(name = "NAME")
    private String name;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "LOGIN")
    private String login;

    @Column(name = "IS_APPROVAL")
    private boolean isApproval;
}
