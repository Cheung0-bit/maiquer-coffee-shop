package tech.maiquer.system.domain;

import lombok.Data;

import java.util.Date;

@Data
public abstract class BaseEntity {

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

}
