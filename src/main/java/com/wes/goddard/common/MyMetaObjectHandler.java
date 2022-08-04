package com.wes.goddard.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

/**
 * Custom Meta data
 */
@Component
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {
    /**
     * Insert
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("Inserting public field [create]...");
        log.info(metaObject.toString());
        metaObject.setValue("createTime",LocalDateTime.now());
        metaObject.setValue("updateTime",LocalDateTime.now());
        metaObject.setValue("createUser",BaseContext.getCurrentId());
        metaObject.setValue("updateUser",BaseContext.getCurrentId());
    }

    /**
     * update
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("Inserting public field [update]...");
        log.info(metaObject.toString());

        long id = Thread.currentThread().getId();
        log.info("ID：{}",id);

        metaObject.setValue("updateTime",LocalDateTime.now());
        metaObject.setValue("updateUser",BaseContext.getCurrentId());
    }
}
