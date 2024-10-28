package com.example.service.adoptions;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.annotation.Id;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@ResponseBody
class DogAdoptionsController {

    private final DogAdoptionService service;

    DogAdoptionsController(DogAdoptionService service) {
        this.service = service;
    }

    @PostMapping("/dogs/{dogId}/adoptions")
    void adopt(@PathVariable int dogId,
               @RequestBody Map<String, String> request) {
        this.service.adopt(dogId, request.get("name"));
    }
}

@Service
@Transactional
class DogAdoptionService {

    private final DogRepository repository;

    private final ApplicationEventPublisher publisher;

    DogAdoptionService(DogRepository repository, ApplicationEventPublisher publisher) {
        this.repository = repository;
        this.publisher = publisher;
    }

    void adopt(int dogId, String owner) {
        this.repository.findById(dogId).ifPresent(dog -> {
            var newDog = this.repository
                    .save(new Dog(dogId, dog.name(), dog.description(), owner, dog.image()));
            this.publisher.publishEvent(new DogAdoptionEvent(dogId));
            System.out.println("adopted [" + newDog + "]");
        });
    }

}


interface DogRepository extends ListCrudRepository<Dog, Integer> {
}

// look mom, no Lombok!
record Dog(@Id int id, String name, String description, String owner, String image) {
}
