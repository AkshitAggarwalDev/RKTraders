package RKTraders.web.Modules.Order;

import RKTraders.web.Modules.Address.AddressEntity;
import RKTraders.web.Modules.Address.AddressTypeEnum;
import RKTraders.web.Modules.Owner.OrderStatus;
import RKTraders.web.Modules.Payment.PaymentStatusEnum;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CustomerOrderResponseSerializationTests {

    @Test
    void serializesAFlatCustomerOrderResponseWithThePersistedOrderId() throws Exception {
        AddressEntity address = new AddressEntity();
        address.setId(9);
        address.setFullName("Customer Name");
        address.setPhoneNumber("9999999999");
        address.setHouseNumber("12");
        address.setStreet("Main Street");
        address.setCity("Yamunanagar");
        address.setState("Haryana");
        address.setPincode("135001");
        address.setCountry("India");
        address.setAddressType(AddressTypeEnum.HOME);

        OrderItemEntity item = new OrderItemEntity();
        item.setId(31);
        item.setProductName("Chair");
        item.setProductPrice(2500);
        item.setQuantity(1);
        item.setTotalPrice(2500);

        OrderEntity order = new OrderEntity();
        order.setId(42);
        order.setAddress(address);
        order.setOrderStatus(OrderStatus.PLACED);
        order.setPaymentStatus(PaymentStatusEnum.PENDING);
        order.setOrderDate(LocalDateTime.of(2026, 10, 16, 10, 0));
        order.setTotalAmount(2500);
        order.setOrderItems(List.of(item));

        ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();
        JsonNode json = mapper.readTree(mapper.writeValueAsString(CustomerOrderResponse.from(order)));

        assertThat(json.path("id").asInt()).isEqualTo(42);
        assertThat(json.path("orderItems")).hasSize(1);
        assertThat(json.path("orderItems").get(0).path("productName").asText()).isEqualTo("Chair");
        assertThat(json.has("customer")).isFalse();
        assertThat(json.path("orderItems").get(0).has("order")).isFalse();
    }
}
