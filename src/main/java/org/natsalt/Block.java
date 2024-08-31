package org.natsalt;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class Block {
    
    public String hash;
    public String previousHash;
    public String merkleRoot;
    public ArrayList<Transaction> transactions = new ArrayList<Transaction>();
    public long timeStamp;
    public int nonce;
    
    // Block Constructor
    public Block(String previousHash) {
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();
        this.hash = calculateHash();
    }
    
    public String calculateHash() {
        return StringUtil.applySha256(
                previousHash +
                        Long.toString(timeStamp) +
                        Integer.toString(nonce) +
                        merkleRoot
        );
    }
    
    public void mineBlock(int difficulty) {
        merkleRoot = StringUtil.getMerkleRoot(transactions);
        String target = StringUtil.getDifficultyString(difficulty);
        while (!hash.substring(0, difficulty).equals(target)) {
            nonce ++;
            hash = calculateHash();
        }
        System.out.println("Block Mined: " + hash);
    }
    
    public boolean addTransaction(Transaction transaction){
        if (null == transaction) return false;
        if (!Objects.equals(previousHash, "0")) {
            if (!transaction.processTransaction()) {
                System.out.println("Transaction filed to process, Discarded");
                return false;
            }
        }
        transactions.add(transaction);
        System.out.println("Transaction successfully added to Block");
        return true;
    }
}
