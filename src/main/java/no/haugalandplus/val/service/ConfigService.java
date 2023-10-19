package no.haugalandplus.val.service;

import no.haugalandplus.val.dto.ConfigDTO;
import no.haugalandplus.val.entities.Config;
import no.haugalandplus.val.repository.ConfigRepository;
import no.haugalandplus.val.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConfigService extends ServiceUtils {

    private ConfigRepository configRepository;
    private UserRepository userRepository;
    private ModelMapper modelMapper;

    public ConfigService(ConfigRepository configRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.configRepository = configRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    public List<ConfigDTO> getAll() {
        return configRepository.findAll().stream().map(this::convert).toList();
    }

    public ConfigDTO createConfig(ConfigDTO configDTO) {
        Config config = convert(configDTO);
        config.setOwner(getCurrentUser());
        config = configRepository.save(config);
        return convert(config);
    }

    private ConfigDTO convert(Config config) {
        ConfigDTO configDTO = modelMapper.map(config, ConfigDTO.class);
        configDTO.setOwnerId(config.getOwner().getUserId());
        return configDTO;
    }

    private Config convert(ConfigDTO configDTO) {
        Config config = modelMapper.map(configDTO, Config.class);
        if (configDTO.getConfigId() != 0) {
            config.setOwner(userRepository.findById(configDTO.getConfigId()).get());
        }
        return config;
    }

    public ConfigDTO getConfig(Long id) {
        return convert(configRepository.findById(id).get());
    }

    public ConfigDTO deleteConfig(Long id) {
        Config config = configRepository.findById(id).get();
        configRepository.delete(config);
        return convert(config);
    }

    public ConfigDTO updateConfig(ConfigDTO configDTO) {
        return convert(configRepository.save(convert(configDTO)));
    }
}
