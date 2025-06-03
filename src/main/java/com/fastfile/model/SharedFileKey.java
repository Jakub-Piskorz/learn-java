package com.fastfile.model;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class SharedFileKey implements Serializable {
    Long ownerId;
    Long sharedUserId;
    String path;
}
