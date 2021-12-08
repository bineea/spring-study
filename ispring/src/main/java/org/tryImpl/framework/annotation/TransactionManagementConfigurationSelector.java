package org.tryImpl.framework.annotation;

public class TransactionManagementConfigurationSelector implements ImportSelector {

    @Override
    public String[] selectImports() {
        return new String[] {
                AutoProxyRegistrar.class.getName(),
                ProxyTransactionManagementConfiguration.class.getName()
        };
    }
}
