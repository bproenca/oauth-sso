package br.com.bcp.foo;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/foos")
public class FooController {

    private static final Map<Long, FooDto> DB = new HashMap<>(Map.of(
        1l, new FooDto(1l, "one"), 
        2l, new FooDto(2l, "two"),
        3l, new FooDto(3l, "three")
    ));

    //@CrossOrigin(origins = "http://localhost:8089")
    @GetMapping(value = "/{id}")
    public FooDto findOne(@PathVariable Long id) {
        return DB.get(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void create(@RequestBody FooDto newFoo) {
        DB.put(newFoo.getId(), newFoo);
    }

    @GetMapping
    public Collection<FooDto> findAll() {
        return DB.values();
    }

    @PutMapping("/{id}")
    public FooDto updateFoo(@PathVariable("id") Long id, @RequestBody FooDto updatedFoo) {
        DB.put(id, updatedFoo);
        return updatedFoo;
    }

}