package org.homo.core.constant;

/**
 * @author wujianchuan 2018/12/27
 */
public enum LogicOperateTypes {
    /**
     * 新增操作
     */
    SAVE("保存"),
    UPDATE("更新"),
    DELETE("删除");

    LogicOperateTypes(String name) {
        this.name = name;
    }

    private String name;

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return this.name;
    }}

