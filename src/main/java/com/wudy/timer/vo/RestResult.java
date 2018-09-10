package com.wudy.timer.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wudy.timer.enums.StatusCode;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Map;

/**
 * Description:
 * 客户端响应返回值封装。
 * @author Eric Lau
 * @Date 2017/8/28.
 */
@Slf4j
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RestResult<T> implements Serializable {

    /**
     * 返回的状态码
     */
    private int status = StatusCode.INFO_SUCCESS.getStatus();

    /**
     * 提示信息
     */
    private String message = StatusCode.INFO_SUCCESS.getDetail();

    /**
     * 返回的数据主体内容
     */
    private T body;

    /**
     * 返回的额外信息，无特殊情况，不使用
     */
    private Map<String, Object> extras;

    public RestResult() {
    }

    public RestResult(int status, String message, T body) {
        this.status = status;
        this.message = message;
        this.body = body;
    }

    public static <T> RestResult<T> success() {
        return new RestResult<>();
    }

    public static <T> RestResult<T> success(T body) {

        RestResult<T> result = success();
        result.setBody(body);
        return result;
    }

    public boolean isSuccess() {
        return this.status == StatusCode.INFO_SUCCESS.getStatus();
    }

    public static <T> RestResult<T> fail() {
        RestResult<T> result = new RestResult<>();
        result.setStatus(StatusCode.BIZ_FAILED.getStatus());
        result.setMessage(StatusCode.BIZ_FAILED.getDetail());
        return result;
    }

    public static <T> RestResult<T> fail(StatusCode statusCode, String message) {
        int status = StatusCode.BIZ_FAILED.getStatus();
        if (statusCode != null) {
            status = statusCode.getStatus();
        }
        return fail(status, message);
    }

    public static <T> RestResult<T>  fail(int status, String message) {
        RestResult<T> result = new RestResult<>();
        result.setStatus(status);
        if (StringUtils.isBlank(message)) {
            message = StatusCode.BIZ_FAILED.getDetail();
        }
        result.setMessage(message);
        return result;
    }


    /**
     * 将RestResult转换成ResponseObj
     * @param obj restResult
     * @param <T> REST泛型
     * @return ResponseObj
     */
    public static <T> RestResult<T> of(ResponseObj obj) {
        RestResult<T> result = new RestResult<>();
        result.setStatus(obj.getStatus());
        result.setMessage(obj.getMessage());
        Object body = obj.getBody();
        if (body != null) {
            result.setBody((T) body);
        }
        result.setExtras(obj.getExtras());
        return result;
    }

    /**
     * 将ResponseObj对象转换成JSON字符串。
     * 基于Jackson对象序列化技术。
     * @return JSON String
     */
    public String toJSON() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.setDateFormat(DateFormat.getDateTimeInstance());
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            log.error("JSON序列化异常,{}", e);
        }
        return StringUtils.EMPTY;
    }

    @Override
    public String toString() {
        return toJSON();
    }

}
