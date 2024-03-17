package com.kvbadev.wms.controllers;

import com.kvbadev.wms.data.warehouse.ItemRepository;
import com.kvbadev.wms.models.exceptions.EntityNotFoundException;
import com.kvbadev.wms.models.exceptions.ItemNotFoundException;
import com.kvbadev.wms.models.warehouse.Item;
import com.kvbadev.wms.models.warehouse.ItemModelAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/items")
public class ItemsController {
    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemModelAssembler itemModelAssembler;

    @GetMapping
    public CollectionModel<EntityModel<Item>> getItems() {
        List<EntityModel<Item>> items = itemRepository.findAll().stream()
                .map(item -> itemModelAssembler.toModel(item)).collect(Collectors.toList());

        return CollectionModel.of(items, linkTo(methodOn(ItemsController.class).getItems()).withSelfRel());
    }

    @GetMapping("{Id}")
    public EntityModel<Item> getItem(@PathVariable("Id") int Id) {
        Optional<Item> res = itemRepository.findById(Id);
        return res.map(itemModelAssembler::toModel).orElseThrow(() -> new ItemNotFoundException(Id));
    }

    @PostMapping
    public ResponseEntity<Item> createItem(@RequestBody Item item) {
        item = itemRepository.save(item);
        return ResponseEntity.ok(item);
    }

    @DeleteMapping("{Id}")
    public ResponseEntity<Void> deleteItem(@PathVariable("Id") int Id) {
        Optional<Item> item = itemRepository.findById(Id);
        if (item.isPresent()) {
            itemRepository.deleteById(item.get().getId());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("parcelId")
    public ResponseEntity<List<Item>> getParcelItems(@PathVariable("parcelId") int parcelId) {
        return ResponseEntity.ok(itemRepository.findItemsByParcelId(parcelId));
    }

}
