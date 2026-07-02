package com.ecommerce.api.service;

import com.ecommerce.api.exception.ProductoNoEncontradoException;
import com.ecommerce.api.exception.StockInsuficienteException;
import com.ecommerce.api.model.DetallePedido;
import com.ecommerce.api.model.Pedido;
import com.ecommerce.api.model.Producto;
import com.ecommerce.api.repository.PedidoRepository;
import com.ecommerce.api.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;
    
    @Autowired
    private ProductoRepository productoRepository;

    public List<Pedido> obtenerTodos() {
        return pedidoRepository.findAll();
    }

    @Transactional 
    public Pedido confirmarPedido(Pedido pedido) {
        double totalCalculado = 0.0;

        for (DetallePedido linea : pedido.getLineas()) {
            Producto productoDB = productoRepository.findById(linea.getProducto().getId())
                    .orElseThrow(() -> new ProductoNoEncontradoException("Producto no encontrado"));

            if (productoDB.getStock() < linea.getCantidad()) {
                throw new StockInsuficienteException("Stock insuficiente para: " + productoDB.getNombre());
            }

            // Descontar stock
            productoDB.setStock(productoDB.getStock() - linea.getCantidad());
            
            // Lógica de Producto Importado (21% recargo)
            double precioFinal = productoDB.getPrecio();
            if (Boolean.TRUE.equals(productoDB.getEsImportado())) {
                precioFinal = precioFinal * 1.21; 
            }
            
            double subtotal = precioFinal * linea.getCantidad();
            linea.setSubtotal(subtotal);
            linea.setPedido(pedido); // Vincular la línea con el pedido padre
            linea.setProducto(productoDB); // Asegurar que devolvemos los datos completos del producto
            
            totalCalculado += subtotal;
        }

        pedido.setTotal(totalCalculado);
        return pedidoRepository.save(pedido);
    }
}