package com.example.demo_study.service;

import com.example.demo_study.model.UserEntity;
import com.example.demo_study.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserEntity create(final UserEntity userEntity){
        if(userEntity == null || userEntity.getEmail() == null){
            throw new RuntimeException("Invailed arguments");
        }
        final String email = userEntity.getEmail();
        if(userRepository.existsByEmail(email)){
            log.warn("Email already exists {}", email);
            throw new RuntimeException("Email already exists");
        }
        return userRepository.save(userEntity);
    }

    public UserEntity getByCredentials(final String email, final String password, final PasswordEncoder encoder){
        final UserEntity originalUser = userRepository.findByEmail(email);

        //matchers 메서드를 이용해 패스워드가 같은지 확인
        if(originalUser != null &&
            encoder.matches(password, originalUser.getPassword())){
            return originalUser;
        }
        return null;
    }
}
/* 보통 암호화된 패스워드를 비교해야 하는 경우 사용자에게 받은 패스워드를 같은 방법으로 암호화한 후, 그 결과를 DB의 값과 비교 하는 것이
    자연스런 생각의 흐름이나, 우리는 matches()를 사용한다. BCryptPasswordEncoder는 같은 값을 인코딩 하더라도 할 때마다 값이 다르고
    패스워드에 랜덤하게 의미 없는 값을 붙여 결과를 생성하기 때문이다. 이런 의미없는 값을 Salt라고 한다.

    따라서, 사용자에게 받은 패스워드를 인코딩해도 DB에 저장된 패스워드와는 다를 확률이 높다.
    대신 BCryPasswordEncoder는 어떤 두 값의 일치 여부를 알려주는 메서드인 matches()를 제공한다.
    이 메서드는 Salt를 고려해 두 값을 비교해준다.
* */