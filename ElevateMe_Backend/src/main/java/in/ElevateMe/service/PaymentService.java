package in.ElevateMe.service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import in.ElevateMe.Document.Payment;
import in.ElevateMe.Document.User;
import in.ElevateMe.dto.AuthResponse;
import in.ElevateMe.repository.PaymentRepository;
import in.ElevateMe.repository.UserRepository;
import lombok.Generated;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static in.ElevateMe.util.AppConstants.PREMIUM;

@Service
@Slf4j
public class PaymentService {

    private final PaymentRepository paymentRepository;

    private final AuthService authService;

//    private final Payment payment;

    private final UserRepository userRepository;

    @Value("${razorpay.key.id}")
    private String razorpayKeyId;

    @Value("${razorpay.key.secret}")
    private String razorpayKeySecret;

    public Payment createOrder(Object principal, String planType) throws RazorpayException {
        log.info("🔹 Starting createOrder for planType={}", planType);
        log.info("🔹 Using Razorpay keyId={}", razorpayKeyId);
        //Step:0: We need to get userprofile
       AuthResponse authResponse = authService.getProfile(principal);

        //Step1: Initialize the razorpay client
        RazorpayClient razorpayClient = new RazorpayClient(razorpayKeyId,razorpayKeySecret);

        //Step2: prepare the JSON object to pass the razorpay
        int amount= 99900; // Amount in paise
        String currency = "INR";
        String receipt= PREMIUM + "_" + UUID.randomUUID().toString().substring(0,8);

        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", amount);
        orderRequest.put("currency", currency);
        orderRequest.put("receipt",receipt);

        //Step3: Call the razorpay API to create order
        Order razorpayOrder = razorpayClient.orders.create(orderRequest);

        //Step 4: Save the order details into db
        Payment newPayment =  Payment.builder()
                .userId(authResponse.getId())
                .razorpayOrderId(razorpayOrder.get("id"))
                .amount(amount)
                .currency(currency)
                .planType(planType)
                .status("created")
                .receipt(receipt)
                .build();

        //Step5: return result
        return paymentRepository.save(newPayment);

    }

    public boolean verifyPayment(String razorpayOrderId, String razorpaySignature, String razorpayPaymentId) throws RazorpayException {

        try{
            JSONObject attributes = new JSONObject();
            attributes.put("razorpay_order_id", razorpayOrderId);
            attributes.put("razorpay_payment_id", razorpayPaymentId);
            attributes.put("razorpay_signature", razorpaySignature);

            boolean isValidSignature = Utils.verifyPaymentSignature(attributes,razorpayKeySecret);

            if (isValidSignature){

                //Update the payment status
                Payment payment = paymentRepository.findByRazorpayOrderId(razorpayOrderId)
                        .orElseThrow(()-> new RuntimeException("Payment not found"));
                payment.setRazorpayPaymentId(razorpayPaymentId);
                payment.setRazorpaySignature(razorpaySignature);
                payment.setStatus("paid");
                paymentRepository.save(payment);

                //Upgrade the user subscription
                upgradeUserSubscription(payment.getUserId(),payment.getPlanType());
                return true;
            }
            return false;

        } catch (Exception e) {
            log.error("Error verifying the payment: ", e);
            return false;
        }
    }

    private void upgradeUserSubscription(String userId, String planType) {
        User existingUser = userRepository.findById(userId)
                        .orElseThrow(()->new UsernameNotFoundException("User not found"));
        existingUser.setSubscriptionPlan(planType);
        userRepository.save(existingUser);

        log.info("User {} upgraded to {} plan", userId, planType);
    }

    public List<Payment> getUserPayments(Object principal) {
        //Current profile Get the current
        AuthResponse authResponse = authService.getProfile(principal);

        //Step2: Call the repo finder method
       return paymentRepository.findByUserIdOrderByCreatedAtDesc(authResponse.getId());

    }

    public Payment getPaymentDetails(String orderId) {
        // Get the repository finder method
          return  paymentRepository.findByRazorpayOrderId(orderId)
                    .orElseThrow(()->new RuntimeException("Payment not found"));
    }
    @Generated
    public PaymentService(final PaymentRepository paymentRepository, final AuthService authService, final UserRepository userRepository) {
        this.paymentRepository = paymentRepository;
        this.authService = authService;
        this.userRepository = userRepository;
    }


}
