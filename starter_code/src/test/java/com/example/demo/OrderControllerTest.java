package com.example.demo;

import com.example.demo.controllers.OrderController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {
    private final UserRepository userRepository = mock(UserRepository.class);
    private final OrderRepository orderRepository = mock(OrderRepository.class);
    private OrderController orderController = new OrderController(userRepository,orderRepository);

    @Test
    public void Submit() {
        User user = new User(0L,"abdulaziz","password",null);

        Item item = new Item(0L,"item1",new BigDecimal("3.54"),"item1 description");

        List<Item> itemList = Collections.singletonList(item);
        Cart cart = new Cart(0L,itemList,user,new BigDecimal("3.54"));

        user.setCart(cart);

        when(userRepository.findByUsername("abdulaziz")).thenReturn(user);

        ResponseEntity<UserOrder> response = orderController.submit("abdulaziz");

        assertThat(response).isNotNull();
        assertThat(response.getStatusCodeValue()).isEqualTo(200);

        UserOrder retrievedUserOrder = response.getBody();
        assertThat(retrievedUserOrder).isNotNull();
        assertThat(retrievedUserOrder.getItems()).isNotNull();
        assertThat(retrievedUserOrder.getTotal()).isNotNull();
        assertThat(retrievedUserOrder.getUser()).isNotNull();
    }


    @Test
    public void SubmitNullUser() {
        when(userRepository.findByUsername("abdulaziz")).thenReturn(null);

        ResponseEntity<UserOrder> response = orderController.submit("abdulaziz");

        assertThat(response).isNotNull();
        assertThat(response.getStatusCodeValue()).isEqualTo(404);
    }

    @Test
    public void getOrdersForUser() {
        User user = new User(0L,"abdulaziz","password",null);

        Item item = new Item(0L,"item1",new BigDecimal("3.54"),"item1 description");

        List<Item> itemList = Collections.singletonList(item);
        Cart cart = new Cart(0L,itemList,user,new BigDecimal("3.54"));

        user.setCart(cart);

        when(userRepository.findByUsername("abdulaziz")).thenReturn(user);

        orderController.submit("abdulaziz");
        ResponseEntity<List<UserOrder>> responseEntity = orderController.getOrdersForUser("abdulaziz");

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        List<UserOrder> userOrders = responseEntity.getBody();
        assertThat(userOrders).isNotNull();
        assertThat(userOrders).isEmpty();
    }

    @Test
    public void getOrdersForUserNullUser() {
        when(userRepository.findByUsername("abdulaziz")).thenReturn(null);

        orderController.submit("abdulaziz");
        ResponseEntity<List<UserOrder>> responseEntity = orderController.getOrdersForUser("abdulaziz");

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(404);
    }


    @Test
    public void userOrderTest() {
        User user = new User(0L,"abdulaziz","password",null);

        Item item = new Item(0L,"item1",new BigDecimal("3.54"),"item1 description");

        List<Item> itemList = Collections.singletonList(item);
        Cart cart = new Cart(0L,itemList,user,new BigDecimal("3.54"));

        user.setCart(cart);

        when(userRepository.findByUsername("abdulaziz")).thenReturn(user);

        ResponseEntity<UserOrder> response = orderController.submit("abdulaziz");

        assertThat(response).isNotNull();
        assertThat(response.getStatusCodeValue()).isEqualTo(200);

        UserOrder retrievedUserOrder = response.getBody();
        assertThat(retrievedUserOrder).isNotNull();

        assertThat(retrievedUserOrder.getItems()).hasSize(1);
        assertThat(retrievedUserOrder.getTotal()).isEqualByComparingTo(new BigDecimal("3.54"));
        assertThat(retrievedUserOrder.getUser()).isEqualTo(user);
    }

}