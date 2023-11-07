package no.haugalandplus.val.service;

import no.haugalandplus.val.auth.JwtTokenUtil;
import no.haugalandplus.val.constants.PollStatusEnum;
import no.haugalandplus.val.constants.UserTypeEnum;
import no.haugalandplus.val.entities.Poll;
import no.haugalandplus.val.entities.User;
import no.haugalandplus.val.repository.PollRepository;
import no.haugalandplus.val.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class IoTService {

    private PollRepository pollRepository;
    private UserRepository userRepository;
    private JwtTokenUtil jwtTokenUtil;

    public IoTService(PollRepository pollRepository, UserRepository userRepository, JwtTokenUtil jwtTokenUtil) {
        this.pollRepository = pollRepository;
        this.userRepository = userRepository;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    public String addIotToPoll(Long pollId) {
        Poll poll = pollRepository.findById(pollId).get();
        if (poll.getStatus() != PollStatusEnum.NOT_INITIALISED) {
            throw new RuntimeException("Can not add IoT-device to initiated polls");
        }

        User iot = new User();
        iot.setUsername(UUID.randomUUID().toString());
        iot.setUserType(UserTypeEnum.IOT);
        iot = userRepository.save(iot);

        poll.getIotList().add(iot);

        pollRepository.save(poll);

        return jwtTokenUtil.createJWT(iot.getUserId());
    }

    public void removeIot(List<User> iotList) {
        userRepository.deleteAll(
                iotList.stream()
                    .filter(iot -> iot.getUserType() == UserTypeEnum.IOT)
                    .toList()
        );
    }
}
