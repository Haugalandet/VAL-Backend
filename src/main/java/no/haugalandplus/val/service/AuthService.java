package no.haugalandplus.val.service;

import no.haugalandplus.val.constants.PollStatusEnum;
import no.haugalandplus.val.entities.Choice;
import no.haugalandplus.val.entities.Poll;
import no.haugalandplus.val.entities.User;
import no.haugalandplus.val.repository.ChoiceRepository;
import no.haugalandplus.val.repository.PollRepository;
import no.haugalandplus.val.repository.VoteRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService extends ServiceUtils {

    private final PollRepository pollRepository;

    private final ChoiceRepository choiceRepository;

    private final VoteRepository voteRepository;


    public AuthService(PollRepository pollRepository, ChoiceRepository choiceRepository, VoteRepository voteRepository) {
        this.pollRepository = pollRepository;
        this.choiceRepository = choiceRepository;
        this.voteRepository = voteRepository;
    }

    public Boolean isLoggedInUser(Long id) {
        User user = getCurrentUserSafe();
        if (user == null) {
            return false;
        }
        return getCurrentUser().getUserId() == id;
    }

    public Boolean isLoggedIn() {
        return getCurrentUserSafe() != null;
    }

    public Boolean ownsPoll(Long id) {
        return isLoggedIn() && pollRepository.existsByPollIdAndUser(id, getCurrentUser());
    }

    public Boolean iotCanVote(Long pollId) {
        return isLoggedIn()
                && pollRepository.existsByPollIdAndIotListContainingAndStatus(pollId, getCurrentUser(), PollStatusEnum.ACTIVE);
    }

    public Boolean canVote(Long choiceId) {
        try {
            Choice choice = choiceRepository.findById(choiceId).get();
            Poll poll = choice.getPoll();
            if (poll.getStatus() != PollStatusEnum.ACTIVE) {
                return false;
            }
            if (poll.isNeedLogin()) {
                return isLoggedIn() && !voteRepository.existsByVoterAndChoice(getCurrentUser(), choice);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Boolean iotCanConnect(Long pollId) {
        return pollRepository.existsByPollIdAndStatus(pollId, PollStatusEnum.NOT_INITIALISED);
    }
}
