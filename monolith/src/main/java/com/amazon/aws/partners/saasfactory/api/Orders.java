package com.amazon.aws.partners.saasfactory.api;

import com.amazon.aws.partners.saasfactory.domain.Order;
import com.amazon.aws.partners.saasfactory.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping(path="/api")
public class Orders {

    private static final Logger logger = LoggerFactory.getLogger(Orders.class);

    @Autowired
    private OrderService orderService;

    @CrossOrigin
    @GetMapping(path = "/orders/{id}")
    public Order getOrder(@PathVariable Integer id) throws Exception {
        return orderService.getOrder(id);
    }

    @CrossOrigin
    @GetMapping(path = "/orders")
    public List<Order> getOrders() throws Exception {
        return orderService.getOrders();
    }

    @CrossOrigin
    @PutMapping(path = "/orders/{id}")
    public Order saveOrder(@PathVariable Integer id, @RequestBody Order order) throws Exception {
        logger.info("Orders::saveOrder [id = " + id + ", order = " + order + ", order.id = " + (order != null ? order.getId() : "null") + "]");
        if (id == null || order == null || !id.equals(order.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return orderService.saveOrder(order);
    }

    @CrossOrigin
    @PostMapping(path = "/orders")
    public Order saveOrder(@RequestBody Order order) throws Exception {
        if (order == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return orderService.saveOrder(order);
    }

    @CrossOrigin
    @DeleteMapping(path = "/orders/{id}")
    public ResponseEntity deleteOrder(@PathVariable Integer id, @RequestBody Order order) throws Exception {
        if (id == null || order == null || !id.equals(order.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        orderService.deleteOrder(order);
        return ResponseEntity.ok().build();
    }
}
