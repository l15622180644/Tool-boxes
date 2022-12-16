package com.lzk.toolboxes.controller;

import com.lzk.toolboxes.utils.SseServer;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;

/**
 * @author
 * @module
 * @date 2022/12/15 17:12
 */

@RestController
@RequestMapping("/sse")
public class SseController {

    @GetMapping("/connect/{userId}")
    public SseEmitter connect(@PathVariable String userId){
        return SseServer.connect(userId);
    }

    @PostMapping("/sendMsg")
    public void sendMsg(@RequestBody Map data){
        SseServer.sendMsgToOne((String) data.get("userId"),(String) data.get("msg"));
    }

    @GetMapping("/end/{userId}")
    public void end(@PathVariable String userId){
        SseServer.close(userId);
    }
}
