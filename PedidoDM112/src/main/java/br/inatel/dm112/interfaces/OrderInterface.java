package br.inatel.dm112.interfaces;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestParam;

import br.inatel.dm112.model.Order;

public interface OrderInterface {

	public Order getOrder(Integer orderNumber);

	public List<Order> searchOrders(String cpf);

	public void updateOrder(Order order, Integer orderNumber);

	public List<Order> getAllOrders(@RequestParam Map<String, String> queryParams);
}