package com.thelastimperial.resmenubash.processors;

import org.jspecify.annotations.Nullable;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.thelastimperial.resmenubash.entities.UserEntity;
import com.thelastimperial.resmenubash.entities.UserSettingEntity;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class UserSettingToUserProcessor implements ItemProcessor<UserSettingEntity, UserEntity>{

    @Override
    public @Nullable UserEntity process(UserSettingEntity item) throws Exception {
        UserEntity user = item.getUser();
        if(user == null){
            log.info("No user for UserSetting: {}", item.getId());
        }
        return user;
    }
    
}
