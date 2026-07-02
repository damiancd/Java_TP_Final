package com.ecommerce.api.service;

import com.ecommerce.api.exception.ProductoNoEncontradoException;
import com.ecommerce.api.model.Producto;
import com.ecommerce.api.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    public List<Producto> obtenerTodos() { return productoRepository.findAll(); }

    public Producto obtenerPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new ProductoNoEncontradoException("Producto no encontrado con el id: " + id));
    }

    public Producto guardarProducto(Producto producto) { return productoRepository.save(producto); }

    public Producto actualizarProducto(Long id, Producto productoDetalles) {
        Producto producto = obtenerPorId(id);
        producto.setNombre(productoDetalles.getNombre());
        producto.setPrecio(productoDetalles.getPrecio());
        producto.setStock(productoDetalles.getStock());
        producto.setEsImportado(productoDetalles.getEsImportado());
        producto.setCategoria(productoDetalles.getCategoria());
        return productoRepository.save(producto);
    }

    public void eliminarProducto(Long id) {
        Producto producto = obtenerPorId(id);
        productoRepository.delete(producto);
    }
}