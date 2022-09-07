package com.company.DSM;

import com.company.Messages.ChangeVariableMessage;
import com.company.Messages.CloseMessage;
import com.company.Messages.Message;
import com.company.Messages.SubscribeMessage;
import mpi.MPI;

public class DSMObserver implements Runnable {

    private DSM dsm;

    public DSMObserver(DSM dsm) {
        this.dsm = dsm;
    }

    @Override
    public void run() {
        boolean exit = false;

        while(true){
            Message[] buffer = new Message[1];
            MPI.COMM_WORLD.Recv(buffer, 0, 1, MPI.OBJECT, MPI.ANY_SOURCE, MPI.ANY_TAG);
            Message message = buffer[0];

            if(message instanceof SubscribeMessage){
                SubscribeMessage m = (SubscribeMessage) message;
                System.out.println(MPI.COMM_WORLD.Rank() + " received: " + m);
                dsm.addSubscription(m.variable, m.rank);
            }
            else if(message instanceof ChangeVariableMessage){
                ChangeVariableMessage m = (ChangeVariableMessage) message;
                System.out.println(MPI.COMM_WORLD.Rank() + " received: " + m);
                dsm.updateVariable(m.variable, m.value);

            }
            else if(message instanceof CloseMessage){
                CloseMessage m = (CloseMessage) message;
                System.out.println(MPI.COMM_WORLD.Rank() + " received: " + m);
                System.out.println("DSM Now: " + dsm);
                return;
            }

            System.out.println("DSM Now: " + dsm);
        }
    }
}
