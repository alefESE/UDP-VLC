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
public class Consumidor extends Thread{
    Buffer buffer;
    DatagramSocket cliente;
    DatagramPacket pacote;
    boolean prioridade;
    Semaphore sem;
    Semaphore prod;
    boolean suicida;
    InetAddress addr;
    int porta;
    
    public Consumidor(int porta,InetAddress addr,Buffer buffer, boolean prioridade, Semaphore sem, Semaphore prod) throws SocketException{
        this.buffer = buffer;
        cliente = new DatagramSocket();
        this.prioridade = prioridade;
        this.sem = sem;
        suicida = false;
        this.addr = addr;
        this.porta = porta;
        
    }
    
    @Override
    public void run(){
        byte data[] = new byte[1316];
        pacote = new DatagramPacket(data, data.length, addr, porta);
        while(true){
            try{
                System.out.println("Entrei");
                pacote.setData(buffer.Ler(prioridade));
                cliente.send(pacote);
                System.out.println("enviei");
                //sem.acquire();
                if(suicida) break;
            } catch (InterruptedException ex) {
                Logger.getLogger(Consumidor.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Consumidor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        try {
            buffer.RemoveConsumidor(prioridade);
        } catch (InterruptedException ex) {
            Logger.getLogger(Consumidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void Suicida(){
        suicida = true;
    }
}
