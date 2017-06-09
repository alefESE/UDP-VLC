/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vlcproxy;
import java.util.concurrent.Semaphore;
/**
 *
 * @author aluno
 */
public class Buffer {
    private byte[][] buffer;
    private int n;
    private static int rear;
    private static int front;
    public static boolean nr;
    public static boolean nw;
    public static int leram;
    public static int consumidores;
    boolean vazio;
    
    public Buffer(int tamanho, int n){
        buffer = new byte[n][tamanho];
        rear = 0;
        front = 0;
        this.n = n;
        nr = false;
        nw = false;
        consumidores = 0;
        leram = 0;
        vazio = true;
    }
    
    public void Insere(byte[] data) throws InterruptedException{
        nw = true;
        buffer[rear] = data;
        System.err.println("Escrevendo:"+buffer[rear].toString()+"na posicao:"+rear);
        rear = (rear + 1) % n;
        if(rear == front) vazio = false;
        nw = false;
    }
    
    public byte[] Ler() throws InterruptedException{
        nr = true;
        byte[] temp = buffer[front];
        System.err.println("lendo:"+temp.toString()+"na posicao:"+front);
        leram++;
        if(leram == consumidores){ 
            leram = 0;
            front = (front + 1) % n;
        }
        if(rear == front) vazio = true;
        nr = false;
        return temp;
    }
}
