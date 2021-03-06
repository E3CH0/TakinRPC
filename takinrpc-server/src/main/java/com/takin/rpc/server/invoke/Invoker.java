package com.takin.rpc.server.invoke;

import com.google.inject.ImplementedBy;
import com.takin.rpc.remoting.netty4.RemotingProtocol;

@ImplementedBy(value = JDKInvoker.class)
public interface Invoker {
    @SuppressWarnings("rawtypes")
    public abstract Object invoke(RemotingProtocol msg) throws Exception;

}
