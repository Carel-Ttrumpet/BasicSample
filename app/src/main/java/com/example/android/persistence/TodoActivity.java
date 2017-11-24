package com.example.android.persistence;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class TodoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todo_activity);

        if (savedInstanceState == null) {
            TodoListFragment fragment = new TodoListFragment();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, fragment, TodoListFragment.TAG).commit();
        }
    }
}
