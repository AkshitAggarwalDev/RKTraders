package RKTraders.web.Modules.Order;

import RKTraders.web.Modules.Address.AddressEntity;
import RKTraders.web.Modules.Address.AddressTypeEnum;
import RKTraders.web.Modules.Owner.OrderStatus;
import RKTraders.web.Modules.Payment.PaymentStatusEnum;

import java.time.LocalDateTime;
import java.util.List;

/** A customer-safe, non-recursive representation of an order API response. */
public record CustomerOrderResponse(
        int id,
        double totalAmount,
        OrderStatus orderStatus,
        LocalDateTime orderDate,
        PaymentStatusEnum paymentStatus,
        AddressResponse address,
        List<OrderItemResponse> orderItems
) {
    public static CustomerOrderResponse from(OrderEntity order) {
        AddressEntity address = order.getAddress();
        AddressResponse addressResponse = address == null ? null : new AddressResponse(
                address.getId(), address.getFullName(), address.getPhoneNumber(), address.getHouseNumber(),
                address.getStreet(), address.getNearByLoc(), address.getCity(), address.getState(),
                address.getPincode(), address.getCountry(), address.getAddressType(), address.getDefaultAddress());
        List<OrderItemResponse> items = order.getOrderItems().stream()
                .map(item -> new OrderItemResponse(item.getId(), item.getProductName(), item.getProductPrice(), item.getQuantity(), item.getTotalPrice()))
                .toList();
        return new CustomerOrderResponse(order.getId(), order.getTotalAmount(), order.getOrderStatus(),
                order.getOrderDate(), order.getPaymentStatus(), addressResponse, items);
    }

    public record AddressResponse(Integer id, String fullName, String phoneNumber, String houseNumber,
                                  String street, String NearByLoc, String city, String state, String pincode,
                                  String country, AddressTypeEnum addressType, Boolean defaultAddress) { }

    public record OrderItemResponse(int id, String productName, double productPrice, int quantity, double totalPrice) { }
}
