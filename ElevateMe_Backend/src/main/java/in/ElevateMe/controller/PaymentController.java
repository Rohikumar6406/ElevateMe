package in.ElevateMe.controller;

import com.razorpay.RazorpayException;
import in.ElevateMe.Document.Payment;
import in.ElevateMe.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static in.ElevateMe.util.AppConstants.PREMIUM;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/payment")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/create-order")
    public ResponseEntity<?> createOrder(@RequestBody Map<String, String> request,
                                          Authentication authentication) throws RazorpayException {
        log.info("➡️ Inside createOrder: {}", request);

        //Validate the request
        String planType=request.get("planType");
        if (!PREMIUM.equalsIgnoreCase(planType)){
            return ResponseEntity.badRequest().body(Map.of("message","Invalid plan type"));
        }

        // call the service method
       Payment payment = paymentService.createOrder(authentication.getPrincipal(),planType);

        //Prepare the response object
        Map<String, Object> response= Map.of(
                "orderId",payment.getRazorpayOrderId(),
                "amount",payment.getAmount(),
                "currency",payment.getCurrency(),
                "receipt",payment.getReceipt()
        );
        // return response
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyPayment(@RequestBody Map<String, String> request) throws RazorpayException {
        //Step1: Validate the request
        String razorpayOrderId =  request.get("razorpay_order_id");
        String razorpayPaymentId = request.get("razorpay_payment_id");
        String razorpaySignature = request.get("razorpay_signature");

        if (Objects.isNull(razorpayOrderId) || Objects.isNull(razorpayPaymentId) || Objects.isNull(razorpaySignature)){
            return ResponseEntity.badRequest().body(Map.of("message","Missing required payment parameters"));
        }

        //Step2: Call the service method
        boolean isValid = paymentService.verifyPayment(razorpayOrderId,razorpaySignature,razorpayPaymentId);

        //Step3: return the response
        if (isValid){
            return ResponseEntity.ok(Map.of(
                    "message", "Payment verified successfully",
                    "status","success"
            ));

        } else {
            return ResponseEntity.badRequest().body(Map.of("message","Payment verification failed"));
        }
    }
    @GetMapping("/history")
    public ResponseEntity<?> getPaymentHistory(Authentication authentication){
        //Step1: Call the service
       List<Payment> payments = paymentService.getUserPayments(authentication.getPrincipal());

        //Step2: return the response
        return ResponseEntity.ok(payments);

    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<?> getOrderDetails(@PathVariable String orderId){

        //Step1: call the service
        Payment paymentDetails =  paymentService.getPaymentDetails(orderId);

        //Step2: return response
        return ResponseEntity.ok(paymentDetails);

    }


}
