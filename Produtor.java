/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vlcproxy;
import java.io.IOException;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.concurrent.Semaphore;

/**
 *
 * @author aluno
 */
public class Produtor extends Thread implements Sincronizador{
    
    DatagramSocket server;
    DatagramPacket pacote;
    public Produtor(int porta/*, InetAddress addr*/) throws SocketException{
        this.server = new DatagramSocket(porta/*, addr*/);
    }
    
    @Override
    public void run(){
        byte data[] = new byte[1316];
        pacote = new DatagramPacket(data,data.length);
        while(true){
            try{
                while(!buffer.vazio) produtor.acquire(); //P(cheio) await
                server.receive(pacote);
                System.err.println("Recebi:" + pacote.toString());
                buffer.Insere(pacote.getData());
                if(!buffer.vazio) consumidor.release(consumidor.getQueueLength());//V(cheio)
            } catch (IOException | InterruptedException ex) {
                Logger.getLogger(Produtor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }


}