package com.example;

import org.example.annotation.Controller;
import org.example.annotation.Service;
import org.example.model.User;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * @Controller 애노테이션이 설정돼 있는 모든 클래스를 찾아서 출력한다.
 */
public class ReflectionTest {
    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    void controllerScan(){
        Set<Class<?>> beans = getTypesAnonotatedWith(List.of(Controller.class,Service.class));
        logger.debug("beans : [{}]",beans);
    }

    @Test
    void showClass() {
        Class<User> clazz = User.class;
        logger.debug(clazz.getName());

        logger.debug("User all declared fields: [{}]", Arrays.stream(clazz.getDeclaredFields()).collect(Collectors.toList()));
        logger.debug("User all declared constructors: [{}]", Arrays.stream(clazz.getDeclaredConstructors()).collect(Collectors.toList()));
        logger.debug("User all declared methods: [{}]", Arrays.stream(clazz.getDeclaredMethods()).collect(Collectors.toList()));
    }

    @Test
    void load() throws ClassNotFoundException {
        // 힙 영역에 로드되어 있는 클래스를 가져올 수 있는 방법 3가지
        // 첫번째 방법
        Class<User> clazz = User.class;

        // 두번째 방법
        User user = new User("serverwizard","김수빈");
        Class<? extends User> clazz1 = user.getClass();

        // 세번째 방법
        Class<?> clazz2 = Class.forName("org.example.model.User");

        logger.debug("clazz : [{}]",clazz);
        logger.debug("clazz1 : [{}]",clazz1);
        logger.debug("clazz2 : [{}]",clazz2);

        assertThat(clazz == clazz1).isTrue();
        assertThat(clazz1 == clazz2).isTrue();
        assertThat(clazz2 == clazz).isTrue();
    }

    private static Set<Class<?>> getTypesAnonotatedWith(List<Class<? extends Annotation>> annotations) {
        Reflections reflections= new Reflections("org.example");

        Set<Class<?>> beans = new HashSet<>();
        annotations.forEach(annotation -> beans.addAll(reflections.getTypesAnnotatedWith(annotation)));

        return beans;
    }
}
