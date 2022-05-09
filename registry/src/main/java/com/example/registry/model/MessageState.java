package com.example.registry.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "MESSAGE_STATE")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessageState extends BaseEntity {
    @Column(name = "CORRELATION_ID")
    private String correlationId;

    @Column(name = "QUEUE_NAME")
    private String queueName;

    @Column(name = "PROPERTY_BODY_NAME")
    private String propertyBodyName;
}
