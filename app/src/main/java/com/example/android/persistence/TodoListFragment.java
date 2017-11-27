package com.example.android.persistence;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.persistence.databinding.TodoListFragmentBinding;
import com.example.android.persistence.db.entity.TodoTaskEntity;
import com.example.android.persistence.model.TodoTask;
import com.example.android.persistence.ui.TodoTaskAdapter;

import com.example.android.persistence.ui.TodoTaskClickCallback;
import com.example.android.persistence.viewmodel.TodoTaskListViewModel;

import java.util.List;

public class TodoListFragment extends LifecycleFragment {
    public static final String TAG = "TodoTaskListViewModel";

    private TodoTaskAdapter mTodoItemAdapter;

    private TodoListFragmentBinding mBinding;

    public TodoListFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.todo_list_fragment, container, false);
        mTodoItemAdapter = new TodoTaskAdapter(mTodoTaskClickCallback);
        mBinding.todoList.setAdapter(mTodoItemAdapter);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final TodoTaskListViewModel viewModel = ViewModelProviders.of(this).get(TodoTaskListViewModel.class);
        subscribeUi(viewModel);
    }

    private void subscribeUi(TodoTaskListViewModel viewModel) {
        // Update the list when the data changes
        viewModel.getTodoTasks().observe(this, new Observer<List<TodoTaskEntity>>() {
            @Override
            public void onChanged(@Nullable List<TodoTaskEntity> myTodos) {
                if (myTodos != null) {
                    mBinding.setIsLoading(false);
                    mTodoItemAdapter.setTodoList(myTodos);
                } else {
                    mBinding.setIsLoading(true);
                }
            }
        });
    }

    private final TodoTaskClickCallback mTodoTaskClickCallback = new TodoTaskClickCallback() {
        @Override
        public void onClick(TodoTask todoTask) {

            if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
                Boolean complete = todoTask.isCompleted();
                Log.i("onClick TodoTask", complete.toString());
                todoTask.setCompleted(!complete);
            }
        }
    };


}
