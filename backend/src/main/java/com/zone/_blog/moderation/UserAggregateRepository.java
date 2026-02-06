package com.zone._blog.moderation;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zone._blog.users.UserInfo;

@Repository
public interface UserAggregateRepository extends JpaRepository<UserInfo, UUID> {

}
