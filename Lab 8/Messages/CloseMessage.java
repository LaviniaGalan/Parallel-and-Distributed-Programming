package com.company.Messages;

public class CloseMessage extends Message{

    public int rank;

    public CloseMessage(int rank) {
        this.rank = rank;
    }

    @Override
    public String toString() {
        return "Process " + rank + " has been closed.";
    }
}
