package com.reactivespring.learnreactivespring.controller.v1;

import com.reactivespring.learnreactivespring.document.Item;
import com.reactivespring.learnreactivespring.repository.ItemReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.reactivespring.learnreactivespring.constants.ItemConstants.ITEM_END_POINT_V1;

@RestController
@Slf4j
public class ItemController {

//    @ExceptionHandler(RuntimeException.class)
//    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
//        log.error("Exception caught in handleRuntimeException : {}", ex);
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
//    }

    @Autowired
    ItemReactiveRepository itemReactiveRepository;

    @GetMapping(ITEM_END_POINT_V1)
    public Flux<Item> getAllItems() {
        return itemReactiveRepository.findAll();
    }

    @GetMapping(ITEM_END_POINT_V1 + "/{id}")
    public Mono<ResponseEntity<Item>> getOneItem(@PathVariable String id) {
        return itemReactiveRepository.findById(id)
                .map(item -> new ResponseEntity<>(item, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping(ITEM_END_POINT_V1)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Item> createItem(@RequestBody Item item) {
        return itemReactiveRepository.save(item);
    }

    @DeleteMapping(ITEM_END_POINT_V1 + "/{id}")
    public Mono<Void> deleteItem(@PathVariable String id) {
        return itemReactiveRepository.deleteById(id);
    }

    @GetMapping(ITEM_END_POINT_V1 + "/runtimeException")
    public Flux<Item> runtimeException() {
        return itemReactiveRepository.findAll()
                .concatWith(Mono.error(new RuntimeException("RuntimeException Occurred.")));
    }

    @PutMapping(ITEM_END_POINT_V1 + "/{id}")
    public Mono<ResponseEntity<Item>> updateItem(@PathVariable String id,
                                                 @RequestBody Item item) {
        return itemReactiveRepository.findById(id)
                .flatMap(current -> {
                    current.setPrice(item.getPrice());
                    current.setDescription(item.getDescription());
                    return itemReactiveRepository.save(current);
                })
                .map(updated -> new ResponseEntity<>(updated, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
