package cn.edu.nsu.onlinejudge.common.Constant;


/**
 * 登录凭证时长
 */
public interface ExpiredSecondsConstant {

    /**
     * 默认状态凭证超时时长
     */
    int DEFAULT_EXPIRED_SECONDS = 3600 * 12;


    /**
     * 记住状态下凭证超时登录时长
     */
    int REMEMBER_EXPIRED_SECONDS = 3600 * 24 * 30;
}
