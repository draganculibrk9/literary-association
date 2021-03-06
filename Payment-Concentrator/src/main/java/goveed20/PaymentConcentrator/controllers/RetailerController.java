package goveed20.PaymentConcentrator.controllers;

import goveed20.PaymentConcentrator.dtos.RetailerData;
import goveed20.PaymentConcentrator.exceptions.BadRequestException;
import goveed20.PaymentConcentrator.services.RetailerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
public class RetailerController {

    @Autowired
    private RetailerService retailerService;

    @PostMapping(value = "/register", consumes = "multipart/form-data")
    public ResponseEntity<?> registerRetailerByAdmin(@RequestPart("retailerData") RetailerData retailerData) {
        try {
            return new ResponseEntity<>(retailerService.registerRetailerByAdmin(retailerData), HttpStatus.OK);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/register-external")
    public ResponseEntity<?> registerRetailerExternal(@RequestBody RetailerData retailerData) {
        try {
            return new ResponseEntity<>(retailerService.registerRetailerExternally(retailerData), HttpStatus.OK);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
