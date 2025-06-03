package com.fastfile.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
public class SharedFile {

    @EmbeddedId
    private SharedFileKey id;

    @ManyToOne
    @NonNull
    @MapsId("ownerId")
    @JoinColumn(name="owner_id", nullable = false)
    private User owner;

    @ManyToOne
    @NonNull
    @MapsId("sharedUserId")
    @JoinColumn(name="shared_user_id", nullable = false)
    private User sharedUser;

    @NonNull
    @Column(nullable = false, insertable = false, updatable = false)
    private String path;
}
