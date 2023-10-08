package no.haugalandplus.val.rest;

import no.haugalandplus.val.dto.ConfigDTO;
import no.haugalandplus.val.service.ConfigService;
import no.haugalandplus.val.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/configs")
public class ConfigController {

    private ConfigService configService;

    public ConfigController(ConfigService configService) {
        this.configService = configService;
    }

    @GetMapping
    public List<ConfigDTO> getAll() {
        return configService.getAll();
    }

    @GetMapping("/{id}")
    public ConfigDTO getConfigById(@PathVariable Long id) {
        return configService.getConfig(id);
    }

    @PostMapping
    public ConfigDTO createConfig(@RequestBody ConfigDTO configDTO) {
        return configService.createConfig(configDTO);
    }

    @PutMapping("/{id}")
    public ConfigDTO updateConfig(@PathVariable Long id, @RequestBody ConfigDTO configDTO) {
        return configService.updateConfig(configDTO);
    }

    @DeleteMapping("/{id}")
    public ConfigDTO deleteConfigById(@PathVariable Long id) {
        return configService.deleteConfig(id);
    }


}
