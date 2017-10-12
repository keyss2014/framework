package cn.keyss.common.rpc;

/**
 * 获取下一个Rpc注入处理链
 */
public interface GetNextRpcInterceptionHandlerDelegate {
    /**
     * 获取下一个Rpc注入链
     * @return
     */
    RpcInterceptionHandler getNextRpcInterceptionHandler();
}
