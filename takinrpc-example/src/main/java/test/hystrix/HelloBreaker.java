package test.hystrix;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolKey;
import com.netflix.hystrix.HystrixThreadPoolProperties;
import com.takin.rpc.client.ProxyFactory;

import test.Hello;

public class HelloBreaker extends HystrixCommand<String> {

    final Hello helloService = ProxyFactory.create(Hello.class, "test", null, null);

    public HelloBreaker() {
        super(setter());
    }

    private static Setter setter() {
        HystrixCommandGroupKey groupkey = HystrixCommandGroupKey.Factory.asKey("rpc");
        HystrixCommandKey commandkey = HystrixCommandKey.Factory.asKey("say");
        HystrixThreadPoolKey threadpoolkey = HystrixThreadPoolKey.Factory.asKey("hello-1");
        HystrixThreadPoolProperties.Setter threadproperties = HystrixThreadPoolProperties.Setter()//
                        .withCoreSize(20).withKeepAliveTimeMinutes(5).withMaxQueueSize(1000).withQueueSizeRejectionThreshold(100);

        HystrixCommandProperties.Setter commandproperty = HystrixCommandProperties.Setter()//
                        .withCircuitBreakerEnabled(true).withCircuitBreakerForceClosed(false)//
                        .withCircuitBreakerForceOpen(false).withCircuitBreakerErrorThresholdPercentage(50)//
                        .withCircuitBreakerRequestVolumeThreshold(20)//
                        .withCircuitBreakerSleepWindowInMilliseconds(5000);
        return HystrixCommand.Setter.withGroupKey(groupkey).andCommandKey(commandkey)//
                        .andThreadPoolKey(threadpoolkey).andThreadPoolPropertiesDefaults(threadproperties)//
                        .andCommandPropertiesDefaults(commandproperty);
    }

    @Override
    protected String run() throws Exception {
        return helloService.say("helo");
    }

}
