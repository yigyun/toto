package com.main.toto.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ModelMapper 설정하는 Cofnig 클래스
 * setMatchingStrategy(MatchingStrategies.STRICT): 속성의 이름이 완벽하게 일치하는 경우에만 매핑을 수행할 수 있다.
 * setFieldMatchingEnabled(true): 필드 매핑
 * setSkipNullEnabled(true): null 값이 있는 필드는 대상 객체로 매핑하지 않는다.
 * setFieldAccessLevel(Configuration.AccessLevel.PRIVATE): private 필드에 직접 접근하여 매핑을 수행한다.
 *
 * 매핑 규칙을 런타임에 확인한다. 그래서 런타임 에러가 발생할 수 있음.
 */

@Configuration
public class RootConfig {

    @Bean
    public ModelMapper getMapper(){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
                .setMatchingStrategy(MatchingStrategies.STRICT);

        return modelMapper;
    }


}
