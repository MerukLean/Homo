package org.homo.listeners;

import org.homo.authority.model.User;
import org.homo.core.model.BaseEntity;
import org.homo.core.constant.LogicOperateTypes;
import org.homo.core.history.factory.HistoryFactory;
import org.homo.core.evens.RepositoryEven;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.SmartApplicationListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author wujianchuan 2018/12/26
 */
@Component
public class HistoryListener implements SmartApplicationListener {
    @Override
    public boolean supportsEventType(Class<? extends ApplicationEvent> eventType) {
        return RepositoryEven.class == eventType;
    }

    @Override
    public boolean supportsSourceType(Class<?> sourceType) {
        return true;
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event){
        Map<String, Object> source = (Map<String, Object>) event.getSource();
        Class clazz = (Class) source.get("clazz");
        BaseEntity entity = (BaseEntity) source.get("entity");
        User operator = (User) source.get("operator");
        LogicOperateTypes operateType = (LogicOperateTypes) source.get("operateType");
        HistoryFactory.getInstance().saveEntityHistory(entity, clazz, operateType, operator);
    }

    @Override
    public int getOrder() {
        return 10;
    }
}
