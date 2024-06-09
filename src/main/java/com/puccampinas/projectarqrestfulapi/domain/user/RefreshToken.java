package com.puccampinas.projectarqrestfulapi.domain.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;


@Document(collection="refresh-tokens")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefreshToken {
    @Id
    private String id;
    @DocumentReference
    private User owner;

}
