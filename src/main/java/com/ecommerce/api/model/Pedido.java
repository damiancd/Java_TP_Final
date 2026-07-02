package com.ecommerce.api.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedido")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    private List<DetallePedido> lineas = new ArrayList<>();

    private Double total = 0.0;

    public Pedido() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public List<DetallePedido> getLineas() { return lineas; }
    public void setLineas(List<DetallePedido> lineas) { this.lineas = lineas; }
    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }
}