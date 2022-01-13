package com.scorpios.customenhance.config;

import com.scorpios.customenhance.annotation.Scorpios;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CustomEnhanceRegister implements ImportBeanDefinitionRegistrar, EnvironmentAware {

    static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";

    private Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }


    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {


//        ConfigurableEnvironment c = (ConfigurableEnvironment) environment;
//        MutablePropertySources m = c.getPropertySources();

//        log.info("=================systemEnvironment======================");
//        log.info("ConfigurableEnvironment",c);
//        log.info("MutablePropertySources",m);
//        Map<String, Object> systemEnvironment = c.getSystemEnvironment();
//        for(Map.Entry<String, Object> entry: systemEnvironment.entrySet()){
//            log.info(entry.getKey()+"--->"+entry.getValue());
//        }
//        log.info("=======================================");
//
//
//        log.info("================PropertySources=======================");
//        log.info("MutablePropertySources",m);
//        Iterator<PropertySource<?>> iterator = m.stream().iterator();
//        while (iterator.hasNext()){
//            PropertySource<?> next = iterator.next();
//            log.info(next.getName() + ":::::" + String.valueOf(next.getSource()));
//        }
//        log.info("=======================================");
        log.info("=================扫描自定义注解开始======================");

        // 扫描指定包，可以手动传入
        String basePackages = "com.scorpios.customenhance";

        List<Class<?>> candidates = scanPackages(basePackages);
        if (candidates.isEmpty()) {
            log.info("扫描指定包下，未发现符合条件的类", basePackages.toString());
            return;
        }
        log.info("扫描到符合条件的类：");
        candidates.forEach(item -> {
            log.info(item.getName());
        });
        // 注册扫描到的类
        registerBeanDefinitions(candidates, registry);

        log.info("=================扫描自定义注解结束======================");
    }


    private List<Class<?>> scanPackages(String basePackages) {
        List<Class<?>> candidates = new ArrayList<Class<?>>();
        try {
            candidates.addAll(findCandidateClasses(basePackages));
        } catch (IOException e) {
            log.error("扫描指定包时出现异常", basePackages);
        }
        return candidates;
    }


    // 将指定包下面的符合条件的类返回
    private List<Class<?>> findCandidateClasses(String basePackage) throws IOException {
        List<Class<?>> candidates = new ArrayList<Class<?>>();
        // classpath*:com/scorpios/**/*.class
        String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
                convertPath(basePackage) + '/' + this.DEFAULT_RESOURCE_PATTERN;
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        MetadataReaderFactory readerFactory = new SimpleMetadataReaderFactory(resourceLoader);
        Resource[] resources = ResourcePatternUtils.getResourcePatternResolver(resourceLoader).getResources(packageSearchPath);
        for (Resource resource : resources) {
            MetadataReader reader = readerFactory.getMetadataReader(resource);
            // 过滤哪些类符合条件，此处判断自定义注解 @Scorpios
            if (match(reader.getClassMetadata())) {
                Class<?> candidateClass = transform(reader.getClassMetadata().getClassName());
                if (candidateClass != null) {
                    candidates.add(candidateClass);
                    log.debug("扫描到符合要求的类：" + candidateClass.getName());
                }
            }
        }
        return candidates;
    }


    private void registerBeanDefinitions(List<Class<?>> internalClasses, BeanDefinitionRegistry registry) {
        for (Class<?> clazz : internalClasses) {
            String beanName = ClassUtils.getShortNameAsProperty(clazz);
            RootBeanDefinition rbd = new RootBeanDefinition(clazz);
            registry.registerBeanDefinition(beanName, rbd);
            if (registerSpringBean(clazz)) {
                registry.registerBeanDefinition(beanName, new RootBeanDefinition(clazz));
            }
        }
    }


    private String convertPath(String path) {
        return StringUtils.replace(path, ".", "/");
    }

    // 根据类名返回Class
    private Class<?> transform(String className) {
        Class<?> clazz = null;
        try {
            clazz = ClassUtils.forName(className, this.getClass().getClassLoader());
        } catch (ClassNotFoundException e) {
            log.info("未找到指定类", className);
        }
        return clazz;
    }


    protected boolean match(ClassMetadata metadata) {
        Class<?> clazz = transformToClass(metadata.getClassName());
        if (clazz == null || !clazz.isAnnotationPresent(Scorpios.class)) {
            return false;
        }
        Scorpios scorpios = clazz.getAnnotation(Scorpios.class);
        if (scorpios.registerBean() && isAnnotatedBySpring(clazz)) {
            throw new IllegalStateException("类{" + clazz.getName() + "}已经标识了Spring组件注解");
        }
        // 过滤抽象类,接口,注解,枚举,内部类及匿名类
        return !metadata.isAbstract() && !clazz.isInterface() && !clazz.isAnnotation() && !clazz.isEnum()
                && !clazz.isMemberClass() && !clazz.getName().contains("$");
    }

    private Class<?> transformToClass(String className) {
        Class<?> clazz = null;
        try {
            clazz = ClassUtils.forName(className, this.getClass().getClassLoader());
        } catch (ClassNotFoundException e) {
            log.info("未找到指定类", className);
        }
        return clazz;
    }


    private boolean isAnnotatedBySpring(Class<?> clazz) {
        return clazz.isAnnotationPresent(Component.class) || clazz.isAnnotationPresent(Configuration.class)
                || clazz.isAnnotationPresent(Service.class) || clazz.isAnnotationPresent(Repository.class)
                || clazz.isAnnotationPresent(Controller.class);
    }

    private boolean registerSpringBean(Class<?> beanClass) {
        return beanClass.getAnnotation(Scorpios.class).registerBean();
    }
}
