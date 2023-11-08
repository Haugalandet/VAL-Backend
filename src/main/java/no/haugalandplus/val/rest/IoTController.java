package no.haugalandplus.val.rest;

import no.haugalandplus.val.service.IoTService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IoTController {

    private IoTService iotService;

    public IoTController(IoTService iotService) {
        this.iotService = iotService;
    }

    @PostMapping("polls/{poll-id}/iot")
    public ResponseEntity<String> connectToPoll(@PathVariable("poll-id") Long id) {
        String token = iotService.addIotToPoll(id);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", token);
        return ResponseEntity.ok().headers(headers).body(token);
    }
}
