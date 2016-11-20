package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
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
        SQLiteDatabase mydatabase = ctx.openOrCreateDatabase("expenses5", ctx.MODE_PRIVATE, null);

        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS account(" +
                "account_no VARCHAR PRIMARY KEY," +
                "bank VARCHAR," +
                "holder VARCHAR," +
                "initial_amt REAL" +
                " );");

        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS transactionLog(" +
                "transaction_id INTEGER PRIMARY KEY," +
                "account_no VARCHAR," +
                "type INT," +
                "amt REAL," +
                "log_date DATE," +
                "FOREIGN KEY (account_no) REFERENCES account(account_no)" +
                ");");

        AccountDAO accountDAO = new PersistentAccountDAO(mydatabase);

        setAccountsDAO(accountDAO);

        setTransactionsDAO(new PersistentTransactionDAO(mydatabase));
    }
}