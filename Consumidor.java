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
public class Consumidor extends Thread implements Sincronizador{
    DatagramSocket cliente;
    DatagramPacket pacote;
    InetAddress addr;
    int porta;
    boolean prioridade;
    static boolean temPrioridade;
    static Semaphore exclusao = new Semaphore(1);
    boolean suicida;
    
    public Consumidor(int porta, InetAddress addr, boolean prioridade) throws SocketException{
        cliente = new DatagramSocket(porta);
        this.addr = addr;
        this.porta = porta;
        this.prioridade = prioridade;
        if(prioridade) temPrioridade = true;
        suicida = false;
        Buffer.consumidores++;
    }
    
    @Override
    public void run(){
        byte data[] = new byte[1316];
        pacote = new DatagramPacket(data, data.length, addr, porta);
        while(true){
            try{
                while((temPrioridade && !prioridade) || buffer.vazio) consumidor.acquire(); //P(vazio) await
                exclusao.acquire();//<
                pacote.setData(buffer.Ler()); //P(mutex) buffer = ler() v(mutex)
                exclusao.release();//>
                cliente.send(pacote);//envia
                System.err.println("Enviei:" + pacote.toString());
                if(buffer.vazio) produtor.release();//V(vazio)
                if(suicida) break;//se mata
            } catch (InterruptedException | IOException ex) {
                Logger.getLogger(Consumidor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        try {
            exclusao.acquire();//<
            Buffer.consumidores--;
            if(prioridade) temPrioridade = false;
            exclusao.release();//>
        } catch (InterruptedException ex) {
            Logger.getLogger(Consumidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void Suicida(){
        suicida = true;
    }
    
}
