package com.example.demo_study.presentation;

import com.example.demo_study.dto.ResponseDTO;
import com.example.demo_study.dto.TestRequestBodyDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("test") // 리소스
public class TestController {

    @GetMapping
    public String testController(){
        return "hello world";
    }

    @GetMapping("testGetMapping")
    public String testControllerWithPath(){
        return "hello world testGetMapping";
    }

    @GetMapping("/{id}")
    public String testControllweWithPathVariable(@PathVariable(required = false) int id){ //false 필수값은 아니라는 의미
        return "Hello World Id" + id;
    }

    @GetMapping("/testRequestParam")
    public String testControllerRequestParam(@RequestParam(required = false)int id){
        return "Hello World! ID"+ id;
    }

    @GetMapping("/testRequestBody")
    public String testControllerRequestBody(@RequestBody TestRequestBodyDTO testRequestBodyDTO){
        return "Hello World! ID" + testRequestBodyDTO.getId() + "Message :" + testRequestBodyDTO.getMessage();
    }

    @GetMapping("/testResponseBody")
    public ResponseDTO<String> testControllerResponseBody(){
        List<String> list = new ArrayList<>();
        list.add("Hello World! I'm ResponseDTO");
        ResponseDTO<String> response = ResponseDTO.<String>builder().data(list).build();
        return response;
    }

    @GetMapping("/testResponseEntity")
    public ResponseEntity<?> testControllerResponseEntity(){
        List<String> list = new ArrayList<>();
        list.add("Hello World! I'm ResponseEntity. And you got 400!");
        ResponseDTO<String> response = ResponseDTO.<String>builder().data(list).build();
        //http status를 400으로 설정
        return ResponseEntity.badRequest().body(response);
    }

}
