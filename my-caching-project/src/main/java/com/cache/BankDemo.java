package com.cache;



class BankAccount{
    private volatile int balance=0;
    BankAccount(){
        balance=0;
    }
    public synchronized void deposit(int amount){
        balance+=amount;
        System.out.println("Deposited: "+amount+" New Balance: "+balance);
    }
    public synchronized void withdraw(int amount){
        if(balance>=amount){
            balance-=amount;
            System.out.println("Withdrawn: "+amount+" New Balance: "+balance);
        }else{
            System.out.println("Insufficient balance");
        }
    }
    public int getBalance(){
        return balance;
    }
}
public class BankDemo {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Bank Demo");
        BankAccount account = new BankAccount();
        Thread depositThread = new Thread(() -> {
            for(int i=0;i<5;i++){
                account.deposit(100);
                
            }
        },"Deposit Thread");
        Thread withdrawThread = new Thread(() -> {
            for(int i=0;i<5;i++){
                account.withdraw(80);
                try{
                    Thread.sleep(100);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
            }
        },"Withdraw Thread");
        depositThread.start();
        Thread.sleep(10);
        withdrawThread.start();

        try{
            Thread.sleep(2000);
        }catch(InterruptedException e){
            e.printStackTrace();
        }
        System.out.println("Final Balance: "+account.getBalance());
    }
}

