package com.fastfile.repository;

import com.fastfile.model.SharedFile;
import com.fastfile.model.SharedFileKey;
import com.fastfile.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface SharedFileRepository extends JpaRepository<SharedFile, SharedFileKey> {

    @Query("SELECT DISTINCT sf.owner FROM SharedFile sf WHERE sf.sharedUser.id = :userId")
    Set<User> findUsersSharingMe(Long userId);
}
