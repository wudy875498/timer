package com.wudy.timer.utils;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 对象序列化工具类
 */
@Slf4j
public class SerializeUtils {

    /**
     * 序列化对象
     * @param obj 需要被序列化的对象
     * @return 序列化字节数组
     */
    public static byte[] serialize(Object obj) {
        if(obj == null) {
            return new byte[0];
        }
        Hessian2Output output = null;
        ByteArrayOutputStream os;
        try {
            os = new ByteArrayOutputStream();
            output = new Hessian2Output(os);
            output.setCloseStreamOnClose(true);
            output.writeObject(obj);
            output.flush();
            return os.toByteArray();
        } catch (IOException e) {
            log.error("[Hessian2]对象序列化失败, {}", e);
        } finally {
            try {
                if(output != null) {
                    output.close();
                }
            } catch (IOException e) {
                log.error("输出流关闭异常, {}", e);
            }
        }
        return new byte[0];
    }

    /**
     * 反序列化对象
     *
     * @param bytes 字节数组
     * @return 反序列化出来的对象
     */
    public static Object deserialize(byte[] bytes) {
        if(bytes == null || bytes.length <= 0) {
            return null;
        }
        Hessian2Input input = null;
        ByteArrayInputStream is;
        try {
            is = new ByteArrayInputStream(bytes);
            input = new Hessian2Input(is);
            input.setCloseStreamOnClose(true);
            return input.readObject();
        } catch (IOException e) {
            log.error("[Hessian2]对象反序列化失败, {}", e);
        } finally {
            try {
                if(input != null) {
                    input.close();
                }
            } catch (IOException e) {
                log.error("输入流关闭异常, {}", e);
            }
        }
        return null;
    }
}
