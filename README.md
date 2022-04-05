# spring-study
 
 ## ispring 自定义spring框架

1. aop的实现逻辑<br/>
通过解析@Aspect注解标注的类，注册advisor<br/>
遍历所有bean的方法是否存在advisor匹配<br/>
创建代理对象<br/>
代理对象执行方法匹配的所有advisor封装，并以责任链的模式依次执行方法，最终执行到原方法<br/>

2. @Bean解析实现逻辑<br/>
ConfigurationClassPostProcessor解析所有@Bean注解标注的方法，但是只是解析@Bean标注方法的方法名称，以及@Bean注解方法所在config的beanName<br/>
当beanDefinition的factoryMethodName不会空时，则解析并执行基于java反射所在config的@Bean注解标注的方法，从而实例化bean对象<br/>
spring是通过AutowiredAnnotationBeanPostProcessor后置处理器完成属性注入<br/>
但是此外为了简单实现，直接赋值beanDefinition的beanClass属性，复用后续属性赋值逻辑<br/>

3. @Transactional实现逻辑<br/>
通过TransactionSynchronizationManager缓存已创建的数据库连接，现在待解决的问题是如果在业务逻辑中获取到当前线程缓存的数据库连接实现事务