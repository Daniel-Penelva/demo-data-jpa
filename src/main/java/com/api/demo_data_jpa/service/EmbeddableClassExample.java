package com.api.demo_data_jpa.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.api.demo_data_jpa.model.embedded.Address;
import com.api.demo_data_jpa.model.embedded.Order;
import com.api.demo_data_jpa.model.embedded.OrderId;
import com.api.demo_data_jpa.repository.OrderRepository;

import jakarta.transaction.Transactional;

@Component
public class EmbeddableClassExample implements CommandLineRunner{

    @Autowired
    private OrderRepository orderRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        // 1) Exemplo 1 - Consultar por Id composto inteiro
        System.out.println("\n=== Consultar por Id composto inteiro ===");

        // Criando o ID Composto
        OrderId id = new OrderId("Daniel", LocalDateTime.now());

        // Criando um endereço
        Address address = new Address("Rua A", "123", "99999-999");

        // Criando um pedido
        Order order = new Order(id, address, "Pedido de Teste", "Outro campo");

        // salvando no BD
        orderRepository.save(order);

        System.out.println("Pedido salvo no banco com ID: " + id);

        Optional<Order> optionalOrder = orderRepository.findById(id);
        optionalOrder.ifPresent(o -> System.out.println(
            "Pedido Encontrado - Nome: " + o.getOrderId().getUsername()
            + " | Data: " + o.getOrderId().getOrderDate()
            + " | Info: " + o.getOrderInfo()
        ));


        // 2) Exemplo 2 - Consultar por parte da chave (exemplo: só username)
        System.out.println("\n=== Consultar por parte da chave (exemplo: só username) ===");

        List<Order> orders = orderRepository.findByUsername("Daniel");
        
        orders.forEach(o -> System.out.println(
            "Buscando por parte da chave username - Nome: " + o.getOrderId().getUsername()
            + " | Data: " + o.getOrderId().getOrderDate()
            + " | Info: " + o.getOrderInfo()
        ));


        // 3) Exemplo 3 - Consultar usando o campo @Embedded (ex: zipCode do Address)
        System.out.println("\n=== Consultar usando o campo @Embedded (ex: zipCode do Address) ===");

        List<Order> ordersZipCode = orderRepository.findByZipCode("99999-999");

        ordersZipCode.forEach(o -> System.out.println(
            "Buscando por zipCode - Nome: " + o.getOrderId().getUsername()
            + " | Endereço: " + o.getAddress().getStreetName() 
            + " | Número da Casa: " + o.getAddress().getHouseNumber()
            + " | CEP: " + o.getAddress().getZipCode()
        ));

    }
    
}
