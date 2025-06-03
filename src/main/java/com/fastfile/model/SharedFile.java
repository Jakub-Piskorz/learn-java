package com.fastfile.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fastfile.dto.UserDTO;
import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "shared_file")
@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
public class SharedFile { // Bruh, I'm starting to regret choosing JPA for many to many tables...

    @EmbeddedId
    @JsonIgnore
    private SharedFileKey id;

    @ManyToOne(fetch = FetchType.LAZY)
    @NonNull
    @MapsId("ownerId")
    @JoinColumn(name="owner_id", nullable = false)
    @JsonIgnore
    private User owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @NonNull
    @MapsId("sharedUserId")
    @JoinColumn(name = "shared_user_id", nullable = false)
    @JsonIgnore
    private User sharedUser;

    public void setPath(String path) {
        id.setPath(path);
    }

    public String getPath() {
        return id.getPath();
    }

    // Getter for ownerId WITHOUT triggering join:
    public Long getOwnerId() {
        return id.getOwnerId();
    }

    // Getter for sharedUserId WITHOUT triggering join:
    public Long getSharedUserId() {
        return id.getSharedUserId();
    }
}
