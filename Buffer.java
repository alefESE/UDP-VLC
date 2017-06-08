/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vlcproxy;
import java.net.DatagramPacket;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author aluno
 */
public class Buffer {
    int consumidores;
    int leram;
    int prioridadeLeram;
    int nw;
    int nr;
    int frente;
    int tras;
    int numPrioridade;
    boolean cheio;
    boolean vazio;
    boolean temPrioridade;
    byte[][] buffer;
    byte[] temp;
    Semaphore prod;
    Semaphore con = new Semaphore(0);
    Semaphore test = new Semaphore(1);
    Semaphore conPriori = new Semaphore(0);
    
    public Buffer(int tamanho, Semaphore prod){
        buffer = new byte[8][tamanho];
        temp = new byte[tamanho];
        vazio = true;
        temPrioridade = false;
        numPrioridade = 0;
        consumidores = 0;
        leram = 0;
        nr = 0;
        nw = 0;
        tras = 0;
        frente = 0;
        prioridadeLeram = 0;
        cheio = false;
        this.prod = prod;
    }
    
    public void Insere(DatagramPacket pacote) throws InterruptedException{
        while(nr != 0) prod.acquire();
        nw ++;
        buffer[frente] = pacote.getData();
        frente = (frente + 1) % 8;
        vazio = false;
        if(frente == tras) cheio = true;
        con.release();
        nw --;
    }
    
    public byte[] Ler(boolean prioridade) throws InterruptedException{
        while(nw != 0 || vazio) con.acquire();
        while(temPrioridade && prioridadeLeram < numPrioridade && !prioridade) con.acquire();
        while(temPrioridade && prioridade) conPriori.acquire();
        if(prioridade) temPrioridade = true;
        nr++;
        temp = buffer[tras];
        nr--;
        leram++;
        if(leram == consumidores){
            tras = (tras + 1) % 8;
            if(tras == frente){
                vazio = true;
                cheio = false;
                prod.release(0);
            }          
        }
        if(prioridade){
            temPrioridade = false;
            prioridadeLeram++;
        }
        conPriori.release(0);
        con.release(0);
        return temp;
    }
    
    public void AddConsumidor(boolean prioridade) throws InterruptedException{
        test.acquire();
        if(prioridade) numPrioridade++;
        consumidores++;
        test.release();
    }
    
    public void RemoveConsumidor(boolean prioridade) throws InterruptedException{
        test.acquire();
        if(prioridade) numPrioridade--;
        consumidores--;
        test.release();
    }
    
    public boolean todosLeram() throws InterruptedException{
        return leram == consumidores;
    }
    
    public boolean isVazio(){
        return vazio;
    }
    
    public boolean isCheio(){
        return cheio;
    }
}
