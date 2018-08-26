package quoters;

import javax.annotation.PostConstruct;

public class TerminatorQuoter implements Quoter {

    TerminatorQuoter() {
        System.err.println("PRHASE ONE");
    }

    @PostConstruct
    public void init(){
        System.err.println("PHASE TWO");
        System.err.println(repeat);
    }

    private String message;

    @InjectRandomBean(min = 2, max = 7)
    private int repeat;

    @Override
    public void sayQuote() {

        for (int x = 0; x < repeat; x++) {
            System.out.println(message);
        }
    }

    public void setMessage(String message) {this.message = message;}

    public String getMessage() { return message; }
}