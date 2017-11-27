package com.example.android.persistence;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class TodoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todo_activity);

        if (savedInstanceState == null) {
            TodoListFragment fragment = new TodoListFragment();
            FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fragment_container, fragment, TodoListFragment.TAG);
            transaction.commit();
        }
    }
}
