package com.example.demo_study.presentation;

import com.example.demo_study.dto.ResponseDTO;
import com.example.demo_study.dto.TodoDTO;
import com.example.demo_study.model.TodoEntity;
import com.example.demo_study.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("todo")
public class TodoController {
    @Autowired
    private TodoService service;

    @GetMapping("test")
    public ResponseEntity<?> testTodo(){

        String str = service.testService(); //테스트 서비스 사용
        List<String> list = new ArrayList<>();
        list.add(str);
        ResponseDTO<String> response = ResponseDTO.
                                                <String>builder()
                                                .data(list).build();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<?> createTodo(@AuthenticationPrincipal String userId, @RequestBody TodoDTO dto){
        // userId는 스프링이 넘겨준다. @AuthenicationPrincipal을 이용해서, JwtAuthenticationFilter클래스에서 
        // UsernamesPasswordAuthenticationToken을 참고
        try {
//            String temporaryUserId = "temporary-user" ; //temporary user id

            //1. TodoEntity로 변환한다.
            TodoEntity entity = TodoDTO.toEntity(dto);

            //2. id를 null로 초기화 한다. 생성 당시에는 id가 없어야 하기 때문
            entity.setId(null);

            //3. 임시 사용자 아이디를 설정해 준다. 추 후, 인증과 , 인가에서 수정예정.
            //   현재 한 사용자만 로그인 없이 사용할 수 있는 app
            entity.setUserId(userId);

            //4. 서비스를 이용해, Todo엔티티를 생성한다
            List<TodoEntity> entities = service.create(entity);

            // 5. 자바 스트림을 이용해 리턴된 엔티티 리스르를 TodoDTO리스트로 변환한다.
            List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

            // 6. 변환된 TodoDTO  리스트를 이용해 ResponseDTO를 초기화한다.
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

            //7 ResponseDTO를 리턴한다.
            return ResponseEntity.ok(response);
        } catch (Exception e) {

            //혹시 예외가 있는 경우, dto대신 error에 메시지를 넣는다.
            String error = e.getMessage();
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping
    public ResponseEntity<?> retrieveTodoList(@AuthenticationPrincipal String userId){
        String temporaryUserId = "temporary-user"; //temporary user id

        //1 서비스 메소드의 retrieve()메서드를 사용해, Todo 리스트를 가져온다.
        List<TodoEntity> enties = service.retrieve(userId);

        //2. 자바 스트림을 이용해, 리턴된 엔티티 리스트를 TodoDTO 리스트로 변환한다.
        List<TodoDTO> dtos = enties.stream().map(TodoDTO::new).collect(Collectors.toList());

        //3. 변환된 TodoDTO 리스트를 이용해, ResponseDTO를 초기화한다.
        ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

        //4. ResponseDTO를 리턴한다.
        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<?> updateTodo(@AuthenticationPrincipal String userid, @RequestBody TodoDTO dto){

        // 1. dto를 entity로 변환
        TodoEntity entity = TodoDTO.toEntity(dto);

        // 2.id를 temporaryUserId로 초기화한다.인증과 인가에서 수정예정
        entity.setUserId(userid);

        //3. 서비스를 이용해 entity를 업데이트 한다.
        List<TodoEntity> entities = service.update(entity);

        //4. 자바 스트림을 이용해, 리턴된 엔티티 리스트를 TodoDTO리스트로 변환한다.
        List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

        //5. 변환된 TodoDTO 리스트를 이용해 ResponseDTO를 초기화한다.
        ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteTodo(@AuthenticationPrincipal String userId, @RequestBody TodoDTO dto){
        try {
//            String temporaryUserId = "temporary-user";

            //(1) TodoEntity로 변환한다.
            TodoEntity entity = TodoDTO.toEntity(dto);

            //(2) 임시 사용자 아이디를 설정해 준다.
            entity.setUserId(userId);

            //(3) 서비스를 이용해, entity를 삭제한다.
            List<TodoEntity> entities = service.delete(entity);

            //(4)자바 스트림을 이용해 리턴된 엔티티 리스트를 Todo 리스트로 변환한다.
            List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

            //(5) 변환된 Todo 리스트를 이용해 ResponseDTO를 초기화한다.
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

            //(6) ResponseDTO를 리턴한다.
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // 혹시, 예외가 있는 경우 dto대신 error에 메시지를 넣어 리턴한다.
            String error = e.getMessage();
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }
}
