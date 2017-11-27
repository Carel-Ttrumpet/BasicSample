/*
 * Copyright 2017, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.persistence.ui;

import android.databinding.DataBindingUtil;

import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.android.persistence.R;
import com.example.android.persistence.databinding.TodoItemBinding;
import com.example.android.persistence.model.TodoTask;


import java.util.List;
import java.util.Objects;

public class TodoTaskAdapter extends RecyclerView.Adapter<TodoTaskAdapter.TodoTaskViewHolder> {

    List<? extends TodoTask> mTodoList;

    @Nullable
    private final TodoTaskClickCallback mTodoClickCallback;

    public TodoTaskAdapter(@Nullable TodoTaskClickCallback clickCallback) {
        mTodoClickCallback = clickCallback;
    }

    public void setTodoList(final List<? extends TodoTask> todoList) {
        if (mTodoList == null) {
            mTodoList = todoList;
            notifyItemRangeInserted(0, todoList.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return mTodoList.size();
                }

                @Override
                public int getNewListSize() {
                    return todoList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return mTodoList.get(oldItemPosition).getId() ==
                            todoList.get(newItemPosition).getId();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    TodoTask todoTask = todoList.get(newItemPosition);
                    TodoTask old = todoList.get(oldItemPosition);
                    return todoTask.getId() == old.getId()
                            && Objects.equals(todoTask.getDescription(), old.getDescription())
                            && todoTask.isCompleted() == old.isCompleted();
                }
            });
            mTodoList = todoList;
            result.dispatchUpdatesTo(this);
        }
    }

    @Override
    public TodoTaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TodoItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.todo_item,
                        parent, false);
        binding.setCallback(mTodoClickCallback);
        return new TodoTaskViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(TodoTaskViewHolder holder, int position) {
        holder.binding.setTodoTask(mTodoList.get(position));
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mTodoList == null ? 0 : mTodoList.size();
    }

    static class TodoTaskViewHolder extends RecyclerView.ViewHolder {

        final TodoItemBinding binding;

        public TodoTaskViewHolder(TodoItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
