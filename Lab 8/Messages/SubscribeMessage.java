package com.company.Messages;

public class SubscribeMessage extends Message{
    public String variable;
    public int rank;

    public SubscribeMessage(String variable, int rank) {
        this.variable = variable;
        this.rank = rank;
    }

    @Override
    public String toString() {
        return "Process " + rank + " subscribed to variable " + variable;
    }
}
