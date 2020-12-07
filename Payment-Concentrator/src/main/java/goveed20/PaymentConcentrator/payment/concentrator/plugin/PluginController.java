package goveed20.PaymentConcentrator.payment.concentrator.plugin;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.Set;

@RequestMapping("/api")
public interface PluginController {

    @GetMapping("/name")
    ResponseEntity<PluginName> getName();

    @PostMapping("/initialize-payment")
    ResponseEntity initializePayment(@Valid @RequestBody InitializationPaymentPayload payload);

    @PostMapping("/complete-payment")
    ResponseEntity completePayment(@RequestBody BasePayload payload);

    @GetMapping("/payment/fields")
    ResponseEntity<Set<String>> getPaymentFields();

}
