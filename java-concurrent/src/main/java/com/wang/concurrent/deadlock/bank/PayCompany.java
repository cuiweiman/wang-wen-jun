package com.wang.concurrent.deadlock.bank;

/**
 * 模拟支付公司转账的动作
 *
 * @author weiman cui
 * @date 2020/5/17 18:05
 */
public class PayCompany {

    private static class TransferThread extends Thread {

        // 线程名
        private String name;

        private UserAccount from;

        private UserAccount to;

        private int amount;

        private Transfer transfer;

        public TransferThread(String name,
                              UserAccount from,
                              UserAccount to,
                              int amount,
                              Transfer transfer) {
            this.name = name;
            this.from = from;
            this.to = to;
            this.amount = amount;
            this.transfer = transfer;
        }

        @Override
        public void run() {
            Thread.currentThread().setName(name);
            try {
                transfer.transferAccount(from, to, amount);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        UserAccount jack = new UserAccount("杰克", 20000);
        UserAccount rose = new UserAccount("Rose", 20000);
        // Transfer transferUnSafe = new TransferUnSafe();
        // Transfer transferSafe = new TransferSafe();
        Transfer transferSafe2 = new TransferSafe2();

        TransferThread jack2Rose = new TransferThread("Jack——>Rose", jack, rose, 2000, transferSafe2);
        TransferThread rose2Jack = new TransferThread("Rose——>Jack", rose, jack, 4000, transferSafe2);
        jack2Rose.start();
        rose2Jack.start();
    }

}
