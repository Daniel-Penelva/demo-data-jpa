package com.api.demo_data_jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.api.demo_data_jpa.model.embedded.Order;
import com.api.demo_data_jpa.model.embedded.OrderId;

@Repository
public interface OrderRepository extends JpaRepository<Order, OrderId>{

    /* OBSERVAÇÃO: o tipo da PK JpaRepository é OrderId */

    // Buscar todos os pedidos pelo username
    @Query("SELECT o FROM Order o WHERE o.orderId.username = :username")
    List<Order> findByUsername(@Param("username") String username);

    // Buscando endereço pelo zipCode
    @Query("SELECT o FROM Order o WHERE o.address.zipCode = :zipCode")
    List<Order> findByZipCode(@Param("zipCode") String zipCode);
    
}
