package com.thelastimperial.resmenubatch.processors;

import org.jspecify.annotations.Nullable;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.thelastimperial.resmenubatch.entities.UserEntity;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class UserExpiredProcessor implements ItemProcessor<UserEntity, UserEntity> {
    @Override
    public @Nullable UserEntity process(UserEntity item) throws Exception {
        log.info("Account expired: {}", item.getId());
        item.setAccountNonExpired(false);
        return item;
    }
}
