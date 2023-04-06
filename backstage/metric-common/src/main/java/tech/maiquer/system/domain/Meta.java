package tech.maiquer.system.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author lin 组件元属性
 * @Description
 * @Date: 2022/5/2 19:30
 */
@Data
public class Meta implements Serializable {

    private String title;

    private String icon;

}
