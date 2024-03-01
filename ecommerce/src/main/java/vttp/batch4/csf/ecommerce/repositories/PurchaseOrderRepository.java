package vttp.batch4.csf.ecommerce.repositories;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import vttp.batch4.csf.ecommerce.exceptions.SaveOrderException;
import vttp.batch4.csf.ecommerce.models.Order;

@Repository
public class PurchaseOrderRepository {

  @Autowired
  private JdbcTemplate template;

  private static final String SQL_SAVE_ORDER = """
      insert into orders(id, date, name, address, priority, comments)
      value (?, ?, ?, ?, ?, ?)
      """;

  private static final String SQL_SAVE_CART_ITEMS = """
      insert into cart_items(order_id, prod_id, quantity, name, price)
      value (?, ?, ?, ?, ?)
      """;

  // IMPORTANT: DO NOT MODIFY THIS METHOD.
  // If this method is changed, any assessment task relying on this method will
  // not be marked
  // You may only add Exception to the method's signature
  public void create(Order order) throws SaveOrderException {

    if (template.update(SQL_SAVE_ORDER,
        order.getOrderId(), order.getDate(), order.getName(), order.getAddress(), order.getPriority(),
        order.getComments()) != 1)
      throw new SaveOrderException("Failed to save order");

    List<Boolean> results = order.getCart().getLineItems().stream()
        .map(item -> template.update(SQL_SAVE_CART_ITEMS, order.getOrderId(), item.getProductId(), item.getQuantity(),
              item.getName(), item.getPrice()) == 1)
        .toList();

    for (Boolean result : results) {
      if (result == false)
        throw new SaveOrderException("Failed to save cart items");
    }
  }
}
