package goveed20.CardPaymentService.aspects;

import goveed20.CardPaymentService.model.Transaction;
import goveed20.PaymentConcentrator.payment.concentrator.plugin.AsyncLogging;
import goveed20.PaymentConcentrator.payment.concentrator.plugin.InitializationPaymentPayload;
import goveed20.PaymentConcentrator.payment.concentrator.plugin.LogDTO;
import goveed20.PaymentConcentrator.payment.concentrator.plugin.TransactionStatus;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Aspect
@Component
public class PaymentAspect {

    @Autowired
    private AsyncLogging asyncLogging;

    @Before("execution(public * goveed20.CardPaymentService.services.PaymentService.*(..)) || " +
            "execution(* goveed20.CardPaymentService.controllers.*.*(..)) || " +
            "execution(public * goveed20.CardPaymentService.services.BankService.*(..))) || " +
            "execution(public * goveed20.CardPaymentService.services.PCCService.*(..)))")
    public void paymentBefore(JoinPoint joinPoint) {
        LogDTO logDTO = null;
        Object[] arguments = joinPoint.getArgs();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        String message = generateMessage(className, methodName, arguments, true);
        try {
            logDTO = generateLog(className, methodName, "INFO", message);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        asyncLogging.callLoggingFeignClient(logDTO);
    }

    @AfterReturning("execution(public * goveed20.CardPaymentService.services.PaymentService.*(..)) || " +
            "execution(public * goveed20.CardPaymentService.services.BankService.*(..))) || " +
            "execution(public * goveed20.CardPaymentService.services.PCCService.*(..)))")
    public void paymentServiceAfterSuccess(JoinPoint joinPoint) {
        LogDTO logDTO = null;
        Object[] arguments = joinPoint.getArgs();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        String message = generateMessage(className, methodName, arguments, false);
        try {
            logDTO = generateLog(className, methodName, "INFO", message);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        asyncLogging.callLoggingFeignClient(logDTO);
    }

    @AfterThrowing(pointcut = "execution(public * goveed20.CardPaymentService.services.PaymentService.*(..)) || " +
            "execution(* goveed20.CardPaymentService.controllers.*.*(..)) || " +
            "execution(public * goveed20.CardPaymentService.services.BankService.*(..))) || " +
            "execution(public * goveed20.CardPaymentService.services.PCCService.*(..)))", throwing = "error")
    public void paymentServiceAfterError(JoinPoint joinPoint, Throwable error) {

        LogDTO logDTO = null;
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        String message = error.getMessage();
        try {
            logDTO = generateLog(className, methodName, "ERROR", message);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        asyncLogging.callLoggingFeignClient(logDTO);
    }

    private LogDTO generateLog(String className, String methodName, String logLevel, String message) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        return LogDTO.builder()
                .date(formatter.parse(formatter.format(new Date())))
                .serviceName("card-payment-service")
                .className(className)
                .methodName(methodName)
                .logLevel(logLevel)
                .message(message)
                .build();
    }

    private String generateMessage(String className, String methodName, Object[] arguments, boolean isBefore) {
        String returnMessage;
        returnMessage = className.equals("PaymentController") ?
                generateControllerMessage(methodName, arguments, isBefore)
                :
                generateServiceMessage(methodName, arguments, isBefore);

        return returnMessage;
    }

    private String generateServiceMessage(String methodName, Object[] arguments, boolean isBefore) {
        String message;
        switch (methodName) {
            case "initializePayment":
                InitializationPaymentPayload initializationPaymentPayload = (InitializationPaymentPayload) arguments[0];
                message = isBefore ?
                        "Initialize bank transaction with id " + initializationPaymentPayload.getTransactionId() +
                                " and amount " + initializationPaymentPayload.getAmount()
                        :
                        "Bank transaction with id " + initializationPaymentPayload.getTransactionId() +
                                " successfully initialized";
                break;
            case "completePayment":
                Long bankTransactionId = (Long) arguments[0];
                message = isBefore ?
                        "Complete bank transaction with id " + bankTransactionId
                        :
                        "Bank transaction with id " + bankTransactionId +
                                " completed";
                break;
            case "sendTransactionResponse":
                Long transactionId = (Long) arguments[0];
                TransactionStatus status = (TransactionStatus) arguments[1];
                message = isBefore ?
                        "Sending data of bank transaction with id " + transactionId + " and status " +
                                status + " to payment concentrator"
                        :
                        "Data of bank transaction with id " + transactionId + " and status " + status +
                                " sent to payment concentrator";
                break;
            case "callPCC":
                Transaction transaction = (Transaction) arguments[6];
                message = isBefore ?
                        "Sending data of bank transaction with id " + transaction.getTransactionID() +
                                " to PCC"
                        :
                        "Data of bank transaction with id " + transaction.getTransactionID() +
                                " is processed on PCC";
                break;
            case "completePaymentInCustomersBank":
                Transaction transactionCustomer = (Transaction) arguments[0];
                message = isBefore ?
                        "Complete bank transaction with id " + transactionCustomer.getTransactionID() +
                                " in customer's bank"
                        :
                        "Bank transaction with id " + transactionCustomer.getTransactionID() +
                                " completed in customer's bank";
                break;
            case "completePaymentInMerchantsBank":
                Long merchantTransactionId = (Long) arguments[0];
                message = isBefore ?
                        "Complete bank transaction with id " + merchantTransactionId + " in merchant's bank"
                        :
                        "Bank transaction with id " + merchantTransactionId +
                                " completed in merchant's bank";
                break;
            default:
                message = "";
        }

        return message;
    }

    private String generateControllerMessage(String methodName, Object[] arguments, boolean isBefore) {
        String message;
        switch (methodName) {
            case "initializePayment":
                InitializationPaymentPayload initializationPaymentPayload = (InitializationPaymentPayload) arguments[0];
                message = isBefore ?
                        "Starting initialization of bank transaction with id " + initializationPaymentPayload
                                .getTransactionId() +
                                " and amount " + initializationPaymentPayload.getAmount()
                        :
                        "Successfully initialized bank transaction with id " + initializationPaymentPayload
                                .getTransactionId() +
                                " and amount " + initializationPaymentPayload.getAmount();
                break;
            case "completePayment":
                message = isBefore ?
                        "Starting completing bank transaction"
                        :
                        "Successfully completed bank transaction";
                break;
            default:
                message = "";
        }

        return message;
    }

}
