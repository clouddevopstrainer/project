package com.example.demo.web;

import com.example.demo.model.Message;
import com.example.demo.repo.MessageRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MessageController {
  private final MessageRepository repo;
  public MessageController(MessageRepository repo) { this.repo = repo; }

  @GetMapping("/")
  public String home() { return "Hello from Spring Boot + MongoDB"; }

  @GetMapping("/messages")
  public List<Message> all() { return repo.findAll(); }

  @PostMapping("/messages")
  public Message create(@RequestBody Message m) { return repo.save(m); }

  @GetMapping("/messages/{id}")
  public ResponseEntity<Message> getById(@PathVariable String id) {
    return repo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
  }
}
