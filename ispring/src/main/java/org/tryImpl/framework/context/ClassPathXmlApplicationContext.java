package org.tryImpl.framework.context;

public class ClassPathXmlApplicationContext extends AbstractApplicationContext {

    private String configLocation;

    private ClassPathXmlApplicationContext() {}

    public ClassPathXmlApplicationContext(String configLocation) {
        this.init(configLocation);
        super.refresh();
    }

    private void init(String configLocation) {
        if(configLocation == null || configLocation.length() <= 0) {
            throw new IllegalArgumentException("配置ispring的xml配置文件路径不为空");
        }
        this.configLocation = configLocation;
    }

    @Override
    public ListableBeanFactory getBeanFactory() {
        return null;
    }

    @Override
    protected void invokeBeanFactoryPostProcessor(BeanFactory beanFactory) {
        //TODO
    }

//    @Override
//    protected void refreshBeanFactory() {
//        try {
//            InputStream inputStream = ClassPathXmlApplicationContext.class.getClassLoader().getResourceAsStream(configLocation);
//            //解析sqlMapperConfig.xml
//            Document document = new SAXReader().read(inputStream);
//            //获取配置文件根对象，即<configuration>
//            Element rootElement = document.getRootElement();
//
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new RuntimeException(e);
//        }
//    }

}
