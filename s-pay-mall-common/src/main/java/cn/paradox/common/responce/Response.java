package cn.paradox.common.responce;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Response<T> implements Serializable {
    private static final long serialVersionUID = 7000723935764546321L;

    private String code;
    private String info;
    private T data;
}
