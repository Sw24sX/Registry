package com.example.registry.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "MESSAGE_STATE")
@Getter
@Setter
public class MessageState extends BaseEntity {
    @Column(name = "REQUEST_MESSAGE")
    private String request;

    @Column(name = "RESPONSE_MESSAGE")
    private String response;
}
