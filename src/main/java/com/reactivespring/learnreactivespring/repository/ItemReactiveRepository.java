package com.reactivespring.learnreactivespring.repository;

import com.reactivespring.learnreactivespring.document.Item;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ItemReactiveRepository extends ReactiveMongoRepository<Item, String> {
}
