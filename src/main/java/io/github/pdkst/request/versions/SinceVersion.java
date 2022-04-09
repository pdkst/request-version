package io.github.pdkst.request.versions;

import org.springframework.data.util.Version;

import java.lang.annotation.*;

/**
 * 最低支持版本，如果只有一个匹配，则忽略此注解
 *
 * @author pdkst.zhang
 * @since 1.5
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SinceVersion {

    String DEFAULT_VERSION_STR = "0.0.0";
    Version DEFAULT_VERSION = Version.parse(DEFAULT_VERSION_STR);

    /**
     * 接口版本，接口
     *
     * @return 最低请求版本
     */
    String value() default DEFAULT_VERSION_STR;

    /**
     * 保底版本，客户端过低的时候应该保底调用的版本，不能设置超过一个
     *
     * @return 默认false，非最新接口
     */
    boolean fallback() default false;
}