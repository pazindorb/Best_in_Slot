package pl.bloniarz.bis.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/items")
public class ItemController {

    @GetMapping("/adminonly")
    public String helloAdmin(){
        return "HELLO ADMIN!";
    }

    @GetMapping("/user")
    public String hello(){
        return "HELLO USER!";
    }

}
