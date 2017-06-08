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
public class Produtor extends Thread{
    
    DatagramSocket server;
    DatagramPacket pacote;
    Buffer buffer;
    Semaphore sem;
    Semaphore con;
    public Produtor(int porta, Buffer buffer, Semaphore sem, Semaphore con) throws SocketException{
        this.server = new DatagramSocket(porta);
        this.buffer = buffer;
        this.sem = sem;
        this.con = con;
    }
    
    @Override
    public void run(){
        byte data[] = new byte[1316];
        pacote = new DatagramPacket(data,data.length);
        while(true){
            try{
                System.out.println("Cheguei");
                server.receive(pacote);
                buffer.Insere(pacote);
                //while(buffer.isCheio()){
                   con.release();
                //    sem.acquire();
                //}
            } catch (IOException ex) {
                Logger.getLogger(Produtor.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(Produtor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }


}