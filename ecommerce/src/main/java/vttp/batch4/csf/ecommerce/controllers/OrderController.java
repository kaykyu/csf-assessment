package vttp.batch4.csf.ecommerce.controllers;

import java.io.StringReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonValue;
import vttp.batch4.csf.ecommerce.exceptions.SaveOrderException;
import vttp.batch4.csf.ecommerce.models.Cart;
import vttp.batch4.csf.ecommerce.models.LineItem;
import vttp.batch4.csf.ecommerce.models.Order;
import vttp.batch4.csf.ecommerce.services.PurchaseOrderService;

@Controller
@RequestMapping(path = "/api")
public class OrderController {

  @Autowired
  private PurchaseOrderService poSvc;

  // IMPORTANT: DO NOT MODIFY THIS METHOD.
  // If this method is changed, any assessment task relying on this method will
  // not be marked
  @PostMapping(path = "/order")
  @ResponseBody
  public ResponseEntity<String> postOrder(@RequestBody String payload) {

    JsonObject jOrder = Json.createReader(new StringReader(payload)).readObject();
    Order order = new Order();

    try {
      order.setName(jOrder.getString("name"));
      order.setAddress(jOrder.getString("address"));
      order.setPriority(jOrder.getBoolean("priority"));
      order.setComments(jOrder.getString("comments"));

      Cart cart = new Cart();
      JsonArray jCart = jOrder.getJsonObject("cart").getJsonArray("lineItems");
      for (JsonValue value : jCart) {

        JsonObject jItem = value.asJsonObject();
        LineItem item = new LineItem();
        item.setProductId(jItem.getString("prodId"));
        item.setName(jItem.getString("name"));
        item.setQuantity(jItem.getInt("quantity"));
        item.setPrice(Float.parseFloat(jItem.get("price").toString()));
        cart.addLineItem(item);
      }
      
      order.setCart(cart);
      poSvc.createNewPurchaseOrder(order);

    } catch (SaveOrderException e) {
      return ResponseEntity.status(HttpStatusCode.valueOf(400))
          .body(Json.createObjectBuilder().add("message", e.getMessage()).build().toString());
    }

    return ResponseEntity.status(HttpStatusCode.valueOf(200))
        .body(Json.createObjectBuilder().add("orderId", order.getOrderId()).build().toString());
  }
}
