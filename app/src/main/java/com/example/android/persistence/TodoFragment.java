package com.example.android.persistence;

import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.persistence.databinding.TodoFragmentBinding;
import com.example.android.persistence.db.entity.TodoTaskEntity;
import com.example.android.persistence.viewmodel.TodoTaskViewModel;


public class TodoFragment  extends LifecycleFragment {

    private static final String KEY_TODO_TASK_ID = "todo_task_id";

    private TodoFragmentBinding mBinding;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate this data binding layout
        mBinding = DataBindingUtil.inflate(inflater, R.layout.todo_fragment, container, false);
        return mBinding.getRoot();
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        TodoTaskViewModel.Factory factory = new TodoTaskViewModel.Factory(
                getActivity().getApplication(), getArguments().getInt(KEY_TODO_TASK_ID));

        final TodoTaskViewModel model = ViewModelProviders.of(this, factory)
                .get(TodoTaskViewModel.class);

        mBinding.setTodoTaskViewModel(model);

        subscribeToModel(model);
    }

    private void subscribeToModel(final TodoTaskViewModel model) {

        // Observe product data
        model.getObservableTodoTask().observe(this, new Observer<TodoTaskEntity>() {
            @Override
            public void onChanged(@Nullable TodoTaskEntity todoTaskEntity) {
                model.setTodoTask(todoTaskEntity);
            }
        });


    }

    /** Creates product fragment for specific product ID */
    public static TodoFragment forTodo(int todoTaskId) {
        TodoFragment fragment = new TodoFragment();
        Bundle args = new Bundle();
        args.putInt(KEY_TODO_TASK_ID, todoTaskId);
        fragment.setArguments(args);
        return fragment;
    }
}
