package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class PersistentTransactionDAO implements TransactionDAO {

    private SQLiteDatabase database;

    public PersistentTransactionDAO(SQLiteDatabase db) {
        this.database = db;
    }

    // this is use to maintain logging using Transaction database
    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        String sql = "INSERT INTO TransactionLog (Account_no,Type,Amt,Log_date) VALUES (?,?,?,?)";
        SQLiteStatement statement = database.compileStatement(sql);

        statement.bindString(1, accountNo);
        statement.bindLong(2, (expenseType == ExpenseType.EXPENSE) ? 0 : 1);
        statement.bindDouble(3, amount);
        statement.bindLong(4, date.getTime());

        statement.executeInsert();
    }
    // this is use to get stored transaction data from the in-memory database
    @Override
    public List<Transaction> getAllTransactionLogs() {
        Cursor resultSet = database.rawQuery("SELECT * FROM TransactionLog", null);
        resultSet.moveToFirst();
        List<Transaction> transactions = new ArrayList<>();

        while (resultSet.moveToNext()) {
            Transaction t = new Transaction(new Date(resultSet.getLong(resultSet.getColumnIndex("Log_date"))),
                    resultSet.getString(resultSet.getColumnIndex("Account_no")),
                    (resultSet.getInt(resultSet.getColumnIndex("Type")) == 0) ? ExpenseType.EXPENSE : ExpenseType.INCOME,
                    resultSet.getDouble(resultSet.getColumnIndex("Amt")));
            transactions.add(t);
        }
        return transactions;
    }
    //
    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        Cursor resultSet = database.rawQuery("SELECT * FROM TransactionLog LIMIT " + limit, null);
        resultSet.moveToFirst();
        List<Transaction> transactions = new ArrayList<>();

        while (resultSet.moveToNext()) {
            Transaction t = new Transaction(new Date(resultSet.getLong(resultSet.getColumnIndex("Log_date"))),
                    resultSet.getString(resultSet.getColumnIndex("Account_no")),
                    (resultSet.getInt(resultSet.getColumnIndex("Type")) == 0) ? ExpenseType.EXPENSE : ExpenseType.INCOME,
                    resultSet.getDouble(resultSet.getColumnIndex("Amt")));
            transactions.add(t);
        }
        return transactions;
    }
}