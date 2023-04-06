package tech.maiquer.common.utils;

import java.util.UUID;

public class UUIDUtils {

    /**
     * 创建用户名
     *
     * @return
     */
    public static String createName() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 10);
    }


}
