package org.tryImpl.framework.transaction;

/**
 * Title: TransactionDefinition<br>
 * Description:  <br>
 * Company: <a href=www.jd.com>京东</a><br>
 *
 * @author: <a href=mailto:guowenbin9@jd.com>郭文彬</a><br>
 * @date: 2022/1/25 11:17
 */
public interface TransactionDefinition {

    int PROPAGATION_REQUIRED = 0;

    default int getPropagationBehavior() {
        return PROPAGATION_REQUIRED;
    }

    static TransactionDefinition withDefaults() {
        return StaticTransactionDefinition.INSTANCE;
    }
}
