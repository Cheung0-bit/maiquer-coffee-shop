package tech.maiquer.common.model;

import lombok.Data;

import java.io.Serializable;


@Data
public class LayuiPage implements Serializable {
    private Integer page;

    private Integer limit;

    public Long getStart(){
        return (page - 1L) * limit;
    }
}
