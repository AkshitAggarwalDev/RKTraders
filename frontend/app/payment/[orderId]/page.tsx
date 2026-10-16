// COD-only flow: the customer has nothing further to do after checkout, so any
// visit to an old /payment/[orderId] link is routed to the order itself.
import { redirect } from "next/navigation";

export default async function LegacyOrderPaymentRedirect({ params }: { params: Promise<{ orderId: string }> }) {
  const { orderId } = await params;
  if (!/^[1-9]\d*$/.test(orderId)) redirect("/orders");
  redirect(`/order-confirmation/${orderId}`);
}
