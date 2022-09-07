package com.company.Messages;

public class ChangeVariableMessage extends Message{

    public String variable;
    public int value;
    public int rank;

    public ChangeVariableMessage(String variable, int value, int rank) {

        this.variable = variable;
        this.value = value;
        this.rank = rank;
    }

    @Override
    public String toString() {
        return "Process " + rank + " changed variable " + variable + "; new value = " + value;
    }
}

