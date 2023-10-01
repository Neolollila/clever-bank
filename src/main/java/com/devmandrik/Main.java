package com.devmandrik;

import com.devmandrik.service.ReceiptService;

public class Main {
    private static final ReceiptService receiptService= new ReceiptService();
    public static void main(String[] args) {
//        receiptService.depositReceipt(1L, 1000F);
//        receiptService.withdrawReceipt(1L, 1F);
        receiptService.transferReceipt(1L, 39L, 1F);
    }
}