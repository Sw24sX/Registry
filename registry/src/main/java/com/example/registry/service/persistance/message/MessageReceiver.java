package com.example.registry.service.persistance.message;

import com.example.registry.model.UserData;
import com.example.registry.service.persistance.Routing;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.util.concurrent.ListenableFuture;

@Data
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class MessageReceiver {
    private String id;
    private Routing routingKey;
    private ListenableFuture<Object> listenableFuture;
}
