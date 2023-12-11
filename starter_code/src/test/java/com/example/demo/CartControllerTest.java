package com.example.demo;

import com.example.demo.controllers.CartController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {
    private final UserRepository userRepository = mock(UserRepository.class);
    private final CartRepository cartRepository = mock(CartRepository.class);
    private final ItemRepository itemRepository = mock(ItemRepository.class);
    private CartController cartController = new CartController(userRepository, cartRepository, itemRepository);


    @Test
    public void addToCart() {
        User user = new User();
        user.setUsername("test");

        Item item = new Item();
        item.setId(0L);
        item.setName("item1");
        item.setPrice(new BigDecimal("3.54"));
        item.setDescription("item1 description");

        Cart cart = new Cart();
        cart.setId(0L);
        List<Item> itemList = new ArrayList<>();
        itemList.add(item);
        cart.setItems(itemList);
        cart.setTotal(item.getPrice());
        cart.setUser(user);
        user.setCart(cart);

        when(userRepository.findByUsername("test")).thenReturn(user);
        when(itemRepository.findById(0L)).thenReturn(java.util.Optional.of(item));

        ModifyCartRequest request = new ModifyCartRequest();
        request.setItemId(0L);
        request.setQuantity(1);
        request.setUsername("test");

        ResponseEntity<Cart> response = cartController.addTocart(request);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        Cart retrievedCart = response.getBody();
        assertNotNull(retrievedCart);
        assertEquals((Long) 0L, retrievedCart.getId());
        List<Item> items = retrievedCart.getItems();
        assertNotNull(items);
        Item retrievedItem = items.get(0);
        assertEquals(2, items.size());
        assertNotNull(retrievedItem);
        assertEquals(item, retrievedItem);
        assertEquals(new BigDecimal("7.08"), retrievedCart.getTotal());
        assertEquals(user, retrievedCart.getUser());
    }

    @Test
    public void removeFromCart(){
        User user = new User();
        user.setUsername("test");

        Item item = new Item();
        item.setId(0L);
        item.setName("item1");
        item.setPrice(new BigDecimal("3.54"));
        item.setDescription("item1 description");

        Cart cart = new Cart();
        cart.setId(0L);
        List<Item> itemList = new ArrayList<>();
        itemList.add(item);
        cart.setItems(itemList);
        cart.setTotal(item.getPrice());
        cart.setUser(user);
        user.setCart(cart);

        when(userRepository.findByUsername("test")).thenReturn(user);
        when(itemRepository.findById(0L)).thenReturn(java.util.Optional.of(item));

        ModifyCartRequest request = new ModifyCartRequest();
        request.setItemId(0L);
        request.setQuantity(1);
        request.setUsername("test");

        ResponseEntity<Cart> response = cartController.addTocart(request);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        Cart retrievedCart = response.getBody();
        assertNotNull(retrievedCart);
        assertEquals((Long) 0L, retrievedCart.getId());
        List<Item> items = retrievedCart.getItems();
        assertNotNull(items);
        Item retrievedItem = items.get(0);
        assertEquals(2, items.size());
        assertNotNull(retrievedItem);
        assertEquals(item, retrievedItem);
        assertEquals(new BigDecimal("7.08"), retrievedCart.getTotal());
        assertEquals(user, retrievedCart.getUser());
        response = cartController.removeFromcart(request);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        retrievedCart = response.getBody();
        assertNotNull(retrievedCart);
        assertEquals((Long) 0L, retrievedCart.getId());
        items = retrievedCart.getItems();
        assertNotNull(items);
        retrievedItem = items.get(0);
        assertEquals(1, items.size());
        assertNotNull(retrievedItem);
        assertEquals(new BigDecimal("3.54"), retrievedCart.getTotal());
        assertEquals(user, retrievedCart.getUser());

    }

    @Test
    public void add(){
        Cart cart =new Cart();
        cart.setItems(new ArrayList<>());
        Item item = new Item();
        item.setId(0L);
        item.setName("item1");
        item.setPrice(new BigDecimal("3.54"));
        item.setDescription("item1 description");
        cart.addItem(item);
        cart.removeItem(item);
        assertEquals(0,cart.getItems().size());
        cart.setId(0L);
        List<Item> itemList = new ArrayList<>();
        itemList.add(item);
        cart.setItems(itemList);
        cart.setTotal(item.getPrice());
        assertEquals(cart.getItems().size(),itemList.size());
        assertEquals((long)cart.getId(),0L);
        assertEquals(cart.getTotal(),item.getPrice());
        cart.getUser();
    }

}