package no.haugalandplus.val.mock;

import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ClockMock {

    private Date currentDate;

    public Date clock() {
        return currentDate;
    }

    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
    }
}
