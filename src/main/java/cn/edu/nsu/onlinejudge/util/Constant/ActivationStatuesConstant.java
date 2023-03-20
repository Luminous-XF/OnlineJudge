package cn.edu.nsu.onlinejudge.util.Constant;

/**
 * 激活账号操作时返回的结果
 */
public interface ActivationStatuesConstant {

    /**
     * 激活成功
     */
    int ACTIVATION_SUCCESS = 0;

    /**
     * 重复激活
     */
    int ACTIVATION_REPEAT = 1;

    /**
     * 激活失败
     */
    int ACTIVATION_FAILURE = 2;
}
