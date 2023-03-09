package com.example.demo_study.service;

import com.example.demo_study.model.TodoEntity;
import com.example.demo_study.repository.TodoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class TodoService {

    @Autowired
    private TodoRepository repository;

    // create
    public String testService() {

        //TodoEntity 생성
        TodoEntity entity = TodoEntity.builder().title("My first todo item").build();
        //TopdoEntity 저장
        repository.save(entity);
        //TodoEntity 검색
        TodoEntity saveEntity = repository.findById(entity.getId()).get();
        return saveEntity.getTitle();
    }

    public List<TodoEntity> create(final TodoEntity entity){
        //Validation
        validation(entity);

        repository.save(entity);
        log.info("Entity Id : {} is saved", entity.getUserId());

        return repository.findByUserId(entity.getUserId());
    }


    // todo read
    public List<TodoEntity> retrieve(final String userId){
        return repository.findByUserId(userId);
    }

    //update
    public List<TodoEntity> update(final TodoEntity entity){
        validation(entity);

        final Optional<TodoEntity> original = repository.findById(entity.getId());

        original.ifPresent(todo ->{
            // 반환된 TodoEntity가 존재하면 값을 새 entity 값으로 덮어 씌운다.
            todo.setTitle(entity.getTitle());
            todo.setDone(entity.isDone());

            // DB에 새 값을 저장
            repository.save(todo);
        });
        // retrieve Todo에서 만든 메서드를 이용해 사용자의 모든 Todo 리스트를 리턴한다.
        return retrieve(entity.getUserId());
    }

    //Delete
    public List<TodoEntity> delete(final  TodoEntity entity){
        validation(entity);

        //2. 엔티티를 삭제
        try {
            repository.delete(entity);
        } catch (Exception e) {
            //exception 발생 시, id와 exception을 로깅
            log.error("error deleting entity" + entity.getId());

        //4, 컨트롤러로 Exception을 보낸고, DB 내부 로직을 캡슐화하려면 e를 리턴하지 않고,
        // 새 exception을 리턴한다.
            throw new RuntimeException("error deleting entity" + entity.getId());
        }
        // 새 Todo 리스트를 가져와 리턴한다.
        return retrieve(entity.getUserId());
    }

    //validation
    private void validation(TodoEntity entity) {
        if(entity == null){
            log.warn("Entity Cannot be null");
            throw new RuntimeException("Entity cannot be null");
        }
        if(entity.getUserId() == null){
            log.warn("Unknown user");
            throw new RuntimeException("Unknown user");
        }
    }


}
