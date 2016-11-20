package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentTransactionDAO;


public class PersistentExpenseManager extends ExpenseManager {
    private Context ctx;

    public PersistentExpenseManager(Context ctx) {
        this.ctx = ctx;
        setup();
    }

    @Override
    public void setup() {
        SQLiteDatabase myDatabase = ctx.openOrCreateDatabase("140321X", ctx.MODE_PRIVATE, null);

        myDatabase.execSQL("CREATE TABLE IF NOT EXISTS Account(" +
                "Account_no VARCHAR PRIMARY KEY," +
                "Bank VARCHAR," +
                "Holder VARCHAR," +
                "Initial_amt REAL" +
                " );");

        myDatabase.execSQL("CREATE TABLE IF NOT EXISTS TransactionLog(" +
                "Transaction_id INTEGER PRIMARY KEY," +
                "Account_no VARCHAR," +
                "Type INT," +
                "Amt REAL," +
                "Log_date DATE," +
                "FOREIGN KEY (Account_no) REFERENCES Account(Account_no)" +
                ");");

//        AccountDAO accountDAO = new PersistentAccountDAO(myDatabase);
        PersistentAccountDAO accountDAO = new PersistentAccountDAO(myDatabase);
        setAccountsDAO(accountDAO);


        PersistentTransactionDAO transactionDAO = new PersistentTransactionDAO(myDatabase);
        setTransactionsDAO(transactionDAO);
    }
}