package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.PersistentExpenseManager;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;


public class PersistentAccountDAO implements AccountDAO {
    private SQLiteDatabase database;

    public PersistentAccountDAO(SQLiteDatabase db) {
        this.database = db;
    }

    @Override
    public List<String> getAccountNumbersList() {
        Cursor resultSet = database.rawQuery("SELECT account_no FROM account", null);
        resultSet.moveToFirst();
        List<String> accounts = new ArrayList<String>();

        while (resultSet.moveToNext()) {
            accounts.add(resultSet.getString(resultSet.getColumnIndex("account_no")));
        }

        return accounts;
    }

    @Override
    public List<Account> getAccountsList() {
        Cursor resultSet = database.rawQuery("SELECT * FROM account", null);
        resultSet.moveToFirst();
        List<Account> accounts = new ArrayList<Account>();

        while (resultSet.moveToNext()) {
            Account account = new Account(resultSet.getString(resultSet.getColumnIndex("account_no")),
                    resultSet.getString(resultSet.getColumnIndex("bank")),
                    resultSet.getString(resultSet.getColumnIndex("holder")),
                    resultSet.getDouble(resultSet.getColumnIndex("initial_amt")));
            accounts.add(account);
        }

        return accounts;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        Cursor resultSet = database.rawQuery("SELECT * FROM account WHERE account_no = " + accountNo, null);
        resultSet.moveToFirst();
        Account account = null;
        while (resultSet.moveToNext()) {
            account = new Account(resultSet.getString(resultSet.getColumnIndex("account_no")),
                    resultSet.getString(resultSet.getColumnIndex("bank")),
                    resultSet.getString(resultSet.getColumnIndex("holder")),
                    resultSet.getDouble(resultSet.getColumnIndex("initial_amt")));
        }

        return account;
    }

    @Override
    public void addAccount(Account account) {
        String sql = "INSERT INTO account (account_no,bank,holder,initial_amt) VALUES (?,?,?,?)";
        SQLiteStatement statement = database.compileStatement(sql);

        statement.bindString(1, account.getAccountNo());
        statement.bindString(2, account.getBankName());
        statement.bindString(3, account.getAccountHolderName());
        statement.bindDouble(4, account.getBalance());

        statement.executeInsert();


    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        String sql = "DELETE FROM account WHERE account_no = ?";
        SQLiteStatement statement = database.compileStatement(sql);

        statement.bindString(1, accountNo);

        statement.executeUpdateDelete();
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        String sql = "UPDATE account SET initial_amt = initial_amt + ?";
        SQLiteStatement statement = database.compileStatement(sql);
        if (expenseType == ExpenseType.EXPENSE) {
            statement.bindDouble(1, -amount);
        } else {
            statement.bindDouble(1, amount);
        }

        statement.executeUpdateDelete();
    }
}